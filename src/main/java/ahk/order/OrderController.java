package ahk.order;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    public record Delivery(int actualMinutes) {}

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderEntity create(@RequestBody OrderService.NewOrder body) {
        return service.create(body);
    }

    @GetMapping
    public List<OrderEntity> list() {
        return service.list();
    }

    @GetMapping("/{id}")
    public OrderEntity get(@PathVariable Long id) {
        return service.get(id);
    }

    /** Marca el pedido como entregado e informa cuántos minutos tardó realmente. */
    @PostMapping("/{id}/deliver")
    public OrderEntity deliver(@PathVariable Long id, @RequestBody Delivery body) {
        return service.deliver(id, body.actualMinutes());
    }
}
