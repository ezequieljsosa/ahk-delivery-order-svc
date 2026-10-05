package ahk.order;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class OrderService {

    public record NewOrder(
            String restaurantId, double lat, double lon, boolean raining, List<OrderItem> items) {}

    private final OrderRepository repository;
    private final CatalogClient catalog;
    private final CourierClient couriers;
    private final PredictorClient predictor;
    private final RabbitTemplate rabbit;

    public OrderService(
            OrderRepository repository,
            CatalogClient catalog,
            CourierClient couriers,
            PredictorClient predictor,
            RabbitTemplate rabbit) {
        this.repository = repository;
        this.catalog = catalog;
        this.couriers = couriers;
        this.predictor = predictor;
        this.rabbit = rabbit;
    }

    public OrderEntity create(NewOrder req) {
        var restaurant = catalog.restaurant(req.restaurantId());
        Map<String, CatalogClient.MenuItem> menu =
                restaurant.menu().stream()
                        .collect(
                                Collectors.toMap(CatalogClient.MenuItem::sku, Function.identity()));

        double total = 0;
        int itemsCount = 0;
        for (OrderItem item : req.items()) {
            var dish = menu.get(item.sku());
            if (dish == null) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "plato inexistente: " + item.sku());
            }
            total += dish.price() * item.qty();
            itemsCount += item.qty();
        }

        double distanceKm = Geo.km(restaurant.lat(), restaurant.lon(), req.lat(), req.lon());
        int hour = LocalTime.now().getHour();

        var order = new OrderEntity();
        order.restaurantId = restaurant.id();
        order.items = req.items();
        order.total = total;
        order.distanceKm = distanceKm;
        order.itemsCount = itemsCount;
        order.hourOfDay = hour;
        order.raining = req.raining();
        order.prepMinutes = restaurant.prepMinutes();
        order.etaMinutes =
                predictor.eta(
                        distanceKm, itemsCount, hour, req.raining(), restaurant.prepMinutes());

        var courier = couriers.assignNearest(restaurant.lat(), restaurant.lon());
        order.courierId = courier == null ? null : courier.id();

        order.status = "CREATED";
        order.createdAt = Instant.now();
        order = repository.save(order);

        rabbit.convertAndSend(MessagingConfig.EXCHANGE, "order.created", OrderEvent.of(order));
        return order;
    }

    public OrderEntity deliver(Long id, int actualMinutes) {
        var order = get(id);
        order.actualMinutes = actualMinutes;
        order.status = "DELIVERED";
        order = repository.save(order);

        if (order.courierId != null) {
            couriers.release(order.courierId);
        }
        rabbit.convertAndSend(MessagingConfig.EXCHANGE, "order.delivered", OrderEvent.of(order));
        return order;
    }

    public OrderEntity get(Long id) {
        return repository
                .findById(id)
                .orElseThrow(
                        () ->
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND, "pedido inexistente"));
    }

    public List<OrderEntity> list() {
        return repository.findAll();
    }
}
