# 009 · Publicación confiable de eventos (outbox)

- **Estado:** Propuesto
- **Servicio:** order-svc
- **Requisitos de sistema:** RS-09

## Historia de usuario

Como equipo, queremos que un pedido guardado siempre llegue a publicarse, aunque RabbitMQ esté caído en ese momento.

## Requisitos funcionales

- **RF-1** El evento se guarda en la misma transacción que el pedido (tabla `outbox`).
- **RF-2** Un proceso periódico publica los eventos pendientes y los marca como enviados; reintenta si RabbitMQ falla.
- **RF-3** Un pedido nunca queda guardado sin su evento ni al revés.

## Escenarios de aceptación

1. **Dado** RabbitMQ caído, **cuando** se crea un pedido, **entonces** responde 201 y el evento se publica cuando RabbitMQ vuelve.

## Fuera de alcance / notas

- Tarea avanzada (patrón *transactional outbox*).
