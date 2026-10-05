package ahk.order;

/**
 * Evento que viaja por RabbitMQ (JSON). Lo consume el worker. Routing keys: order.created y
 * order.delivered (en esta última actualMinutes ya viene completo).
 */
public record OrderEvent(
        Long orderId,
        String restaurantId,
        double distanceKm,
        int itemsCount,
        int hourOfDay,
        boolean raining,
        int prepMinutes,
        Integer etaMinutes,
        Integer actualMinutes,
        String courierId,
        double total,
        String status) {

    static OrderEvent of(OrderEntity o) {
        return new OrderEvent(
                o.id,
                o.restaurantId,
                o.distanceKm,
                o.itemsCount,
                o.hourOfDay,
                o.raining,
                o.prepMinutes,
                o.etaMinutes,
                o.actualMinutes,
                o.courierId,
                o.total,
                o.status);
    }
}
