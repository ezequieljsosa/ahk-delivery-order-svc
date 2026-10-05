package ahk.order;

import jakarta.persistence.Embeddable;

@Embeddable
public record OrderItem(String sku, int qty) {}
