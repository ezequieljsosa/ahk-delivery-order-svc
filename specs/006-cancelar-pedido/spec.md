# 006 · Cancelar un pedido

- **Estado:** Propuesto
- **Servicio:** order-svc
- **Requisitos de sistema:** RS-11

## Historia de usuario

Como cliente, quiero cancelar un pedido que todavía no fue entregado.

## Requisitos funcionales

- **RF-1** `POST /orders/{id}/cancel` pasa un pedido `CREATED` a `CANCELLED`.
- **RF-2** Libera al repartidor asignado.
- **RF-3** Publica `order.cancelled`.
- **RF-4** Cancelar un pedido `DELIVERED` o ya `CANCELLED` responde 409.

## Escenarios de aceptación

1. **Dado** un pedido `CREATED` con repartidor, **cuando** se cancela, **entonces** queda `CANCELLED` y el repartidor `AVAILABLE`.
2. **Dado** un pedido `DELIVERED`, **cuando** se cancela, **entonces** responde 409.

## Fuera de alcance / notas

- El worker debe decidir qué hace con `order.cancelled` (por ejemplo, marcarlo en el histórico para no entrenar con él).
