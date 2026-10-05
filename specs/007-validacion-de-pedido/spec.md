# 007 · Validar el pedido y errores 400 claros

- **Estado:** Propuesto
- **Servicio:** order-svc
- **Requisitos de sistema:** RS-04

## Historia de usuario

Como cliente, quiero un error claro cuando mi pedido es inválido, en vez de un 500.

## Requisitos funcionales

- **RF-1** `items` no puede ser nulo ni vacío; `qty` mayor que 0; `restaurantId` obligatorio.
- **RF-2** `lat` y `lon` dentro de rango.
- **RF-3** Se rechaza con 400 un plato `available = false` (ver spec 005 de `catalog-svc`).
- **RF-4** Si `catalog-svc` no responde, el error es 503 y no 500.

## Escenarios de aceptación

1. **Dado** un pedido con `qty = 0`, **cuando** se crea, **entonces** responde 400 indicando el campo.
2. **Dado** que `catalog-svc` está caído, **cuando** se crea un pedido, **entonces** responde 503.
