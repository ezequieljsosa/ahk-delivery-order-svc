package ahk.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import tools.jackson.databind.JsonNode;

import java.util.Map;

@Component
public class PredictorClient {

    private static final Logger LOG = LoggerFactory.getLogger(PredictorClient.class);

    private final RestClient http;

    public PredictorClient(@Value("${app.predictor-url}") String url) {
        this.http = Http.client(url);
    }

    /** Minutos estimados de entrega. null si el predictor no está disponible. */
    public Integer eta(
            double distanceKm, int itemsCount, int hour, boolean raining, int prepMinutes) {
        try {
            JsonNode res =
                    http.post()
                            .uri("/predict/eta")
                            .body(
                                    Map.of(
                                            "distance_km", distanceKm,
                                            "items_count", itemsCount,
                                            "hour", hour,
                                            "raining", raining ? 1 : 0,
                                            "prep_minutes", prepMinutes))
                            .retrieve()
                            .body(JsonNode.class);
            if (res == null) {
                return null;
            }
            return (int) Math.round(res.path("eta_minutes").asDouble());
        } catch (Exception e) {
            LOG.warn("el predictor no respondió: {}", e.getMessage());
            return null;
        }
    }
}
