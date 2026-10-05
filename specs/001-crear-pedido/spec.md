# 001 · Crear un pedido

- **Estado:** Implementado
- **Servicio:** order-svc
- **Requisitos de sistema:** RS-01, RS-02, RS-03, RS-04, RS-05

## Historia de usuario

Como cliente, quiero hacer un pedido a un restaurante, para recibir comida y saber cuánto va a tardar.

## Requisitos funcionales

- **RF-1** `POST /orders` recibe `restaurantId`, `lat`, `lon`, `raining` e `items` (`sku`, `qty`) y responde 201 con el pedido creado.
- **RF-2** Consulta el restaurante a `catalog-svc`; si no existe responde 404.
- **RF-3** Cada `sku` debe estar en el menú del restaurante; si no, responde 400 con el sku inexistente. `total` = suma de `price * qty`; `itemsCount` = suma de `qty`.
- **RF-4** Calcula `distanceKm` (haversine) entre el restaurante y el cliente, y toma la hora actual como `hourOfDay`.
- **RF-5** Pide el ETA al predictor. Si el predictor falla o no responde (timeout 3 s), el pedido se crea igual con `etaMinutes = null`.
- **RF-6** Pide a `courier-svc` el repartidor libre más cercano al restaurante. Si no hay o el servicio falla, el pedido se crea con `courierId = null`.
- **RF-7** Guarda el pedido en estado `CREATED` y publica `order.created`.

## Escenarios de aceptación

1. **Dado** un restaurante y platos válidos, **cuando** se crea el pedido, **entonces** responde 201 con `status = CREATED`, `etaMinutes` y `courierId` completos.
2. **Dado** que el predictor no responde, **cuando** se crea el pedido, **entonces** responde 201 con `etaMinutes = null`.
3. **Dado** que no hay repartidores libres, **cuando** se crea el pedido, **entonces** responde 201 con `courierId = null`.
4. **Dado** un `restaurantId` inexistente, **cuando** se crea el pedido, **entonces** responde 404 y no se guarda ni se publica nada.
5. **Dado** un `sku` que no está en el menú, **cuando** se crea el pedido, **entonces** responde 400 y no se guarda ni se publica nada.

## Contrato

```http
POST /orders
{"restaurantId": "rest-pizza", "lat": -34.58, "lon": -58.42, "raining": true,
 "items": [{"sku": "PIZZA-MUZA", "qty": 2}]}

201 Created
{"id": 1, "restaurantId": "rest-pizza", "total": 18000.0, "distanceKm": 4.1, "itemsCount": 2,
 "hourOfDay": 13, "raining": true, "prepMinutes": 15, "etaMinutes": 49, "actualMinutes": null,
 "courierId": "a1b2c3d4", "status": "CREATED", "createdAt": "2026-10-04T13:05:00Z"}
```

## Fuera de alcance / notas

- Si `catalog-svc` está caído, hoy el error no se captura y responde 500.
- Orden de las operaciones: catálogo, validación, distancia, ETA, repartidor, guardado, evento. El repartidor se asigna antes de guardar: si falla el guardado o la publicación, queda `BUSY` sin pedido (ver 009).
- Si RabbitMQ está caído, el pedido ya quedó guardado pero la respuesta es 500 (ver 009).
