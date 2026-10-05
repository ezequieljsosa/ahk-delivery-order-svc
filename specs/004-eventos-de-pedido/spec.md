# 004 · Eventos de pedido (contrato con el worker)

- **Estado:** Implementado
- **Servicio:** order-svc
- **Requisitos de sistema:** RS-05, RS-06

## Historia de usuario

Como worker, quiero recibir un evento por cada cambio relevante de un pedido, para procesarlo de forma asincrónica sin que `order-svc` espere.

## Requisitos funcionales

- **RF-1** Se publican a un exchange `orders` de tipo *topic* y durable, con las routing keys `order.created` y `order.delivered`.
- **RF-2** El cuerpo es JSON con los campos del contrato de abajo; en `order.created` `actualMinutes` es nulo.
- **RF-3** `order-svc` declara el exchange, pero **no** las colas: cada consumidor declara y enlaza la suya.

## Escenarios de aceptación

1. **Dado** un pedido creado, **cuando** se inspecciona RabbitMQ, **entonces** hay un mensaje `order.created` con el mismo `orderId`.
2. **Dado** un pedido entregado, **cuando** se inspecciona RabbitMQ, **entonces** hay un mensaje `order.delivered` con `actualMinutes` completo.

## Contrato

```json
{"orderId": 1, "restaurantId": "rest-pizza", "distanceKm": 4.1, "itemsCount": 2, "hourOfDay": 13,
 "raining": true, "prepMinutes": 15, "etaMinutes": 49, "actualMinutes": null,
 "courierId": "a1b2c3d4", "total": 18000.0, "status": "CREATED"}
```

## Fuera de alcance / notas

- Agregar campos es compatible; renombrar o quitar campos rompe al worker (hay que coordinar el cambio).
