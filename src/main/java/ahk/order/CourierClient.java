package ahk.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Component
public class CourierClient {

    public record Courier(String id, String name) {}

    private static final Logger LOG = LoggerFactory.getLogger(CourierClient.class);

    private final RestClient http;

    public CourierClient(@Value("${app.courier-url}") String url) {
        this.http = Http.client(url);
    }

    /** Pide el repartidor libre más cercano al punto. null si no hay o si el servicio falla. */
    public Courier assignNearest(double lat, double lon) {
        try {
            return http.post()
                    .uri("/couriers/assign")
                    .body(Map.of("lat", lat, "lon", lon))
                    .retrieve()
                    .body(Courier.class);
        } catch (Exception e) {
            LOG.warn("no se pudo asignar repartidor: {}", e.getMessage());
            return null;
        }
    }

    public void release(String courierId) {
        try {
            http.put()
                    .uri("/couriers/{id}/status", courierId)
                    .body(Map.of("status", "AVAILABLE"))
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception e) {
            LOG.warn("no se pudo liberar al repartidor {}: {}", courierId, e.getMessage());
        }
    }
}
