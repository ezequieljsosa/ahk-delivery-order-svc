# 005 · ETA de reserva cuando falla el predictor

- **Estado:** Propuesto
- **Servicio:** order-svc
- **Requisitos de sistema:** RS-02

## Historia de usuario

Como cliente, quiero ver siempre un tiempo estimado, aunque el predictor esté caído.

## Requisitos funcionales

- **RF-1** Si el predictor falla, `etaMinutes = prepMinutes + round(4 * distanceKm)`.
- **RF-2** El pedido guarda de dónde salió el ETA: `etaSource` = `MODEL` o `FALLBACK`; el evento `order.created` lo incluye.
- **RF-3** Se registra una advertencia en el log.

## Escenarios de aceptación

1. **Dado** que el predictor no responde, **cuando** se crea un pedido, **entonces** `etaMinutes` no es nulo y `etaSource = FALLBACK`.
2. **Dado** que el predictor responde, **cuando** se crea un pedido, **entonces** `etaSource = MODEL`.

## Fuera de alcance / notas

- Coordinar con el worker: la columna nueva en `orders_history` evita mezclar ETAs de reserva al reentrenar.
