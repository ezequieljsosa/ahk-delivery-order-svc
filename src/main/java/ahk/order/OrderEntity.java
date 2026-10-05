package ahk.order;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.List;

/** Un pedido. Los campos son públicos para mantener la clase corta (JPA usa acceso por campo). */
@Entity
@Table(name = "orders")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String restaurantId;

    @ElementCollection(fetch = FetchType.EAGER)
    public List<OrderItem> items;

    public double total;
    public double distanceKm;
    public int itemsCount;
    public int hourOfDay;
    public boolean raining;
    public int prepMinutes;

    /** Minutos estimados por el predictor. null si el predictor no respondió. */
    public Integer etaMinutes;

    /** Minutos reales, se completan al entregar. */
    public Integer actualMinutes;

    public String courierId;

    /** CREATED o DELIVERED. */
    public String status;

    public Instant createdAt;
}
