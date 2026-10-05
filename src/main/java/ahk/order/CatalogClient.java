package ahk.order;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Component
public class CatalogClient {

    public record MenuItem(String sku, String name, double price) {}

    public record Restaurant(
            String id, String name, double lat, double lon, int prepMinutes, List<MenuItem> menu) {}

    private final RestClient http;

    public CatalogClient(@Value("${app.catalog-url}") String url) {
        this.http = Http.client(url);
    }

    public Restaurant restaurant(String id) {
        try {
            return http.get().uri("/restaurants/{id}", id).retrieve().body(Restaurant.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "restaurante inexistente: " + id);
        }
    }
}
