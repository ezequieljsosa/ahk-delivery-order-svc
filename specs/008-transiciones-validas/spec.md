# 008 · Validar las transiciones de estado

- **Estado:** Propuesto
- **Servicio:** order-svc
- **Requisitos de sistema:** RS-11

## Historia de usuario

Como equipo, queremos que el estado de un pedido no pueda retroceder ni repetirse, para que los datos de analítica sean confiables.

## Requisitos funcionales

- **RF-1** Solo `CREATED → DELIVERED` es válida para entregar; entregar un pedido que no está `CREATED` responde 409.
- **RF-2** `actualMinutes` debe ser mayor que 0.
- **RF-3** Una entrega rechazada no publica eventos ni toca al repartidor.

## Escenarios de aceptación

1. **Dado** un pedido `DELIVERED`, **cuando** se vuelve a entregar, **entonces** responde 409 y no se publica un segundo `order.delivered`.
