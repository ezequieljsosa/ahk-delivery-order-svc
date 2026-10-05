# 003 · Entregar un pedido

- **Estado:** Implementado
- **Servicio:** order-svc
- **Requisitos de sistema:** RS-07, RS-08

## Historia de usuario

Como repartidor, quiero informar que entregué el pedido y cuánto tardó, para cerrar el pedido y liberar mi disponibilidad.

## Requisitos funcionales

- **RF-1** `POST /orders/{id}/deliver` con `{"actualMinutes": n}` guarda los minutos reales y pasa el pedido a `DELIVERED`; 404 si no existe.
- **RF-2** Si el pedido tenía repartidor, se lo libera en `courier-svc` (`AVAILABLE`); si esa llamada falla, se registra una advertencia y se continúa.
- **RF-3** Publica `order.delivered` con `etaMinutes` y `actualMinutes`, para poder medir el error de la predicción.

## Escenarios de aceptación

1. **Dado** un pedido `CREATED` con repartidor, **cuando** se informa la entrega, **entonces** el pedido queda `DELIVERED` y el repartidor `AVAILABLE`.
2. **Dado** un id inexistente, **cuando** se informa la entrega, **entonces** responde 404.
3. **Dado** un pedido sin repartidor, **cuando** se informa la entrega, **entonces** no se llama a `courier-svc`.

## Fuera de alcance / notas

- No se valida `actualMinutes` (acepta valores negativos o cero).
