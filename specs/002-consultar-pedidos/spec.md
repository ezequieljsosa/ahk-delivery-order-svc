# 002 · Consultar pedidos

- **Estado:** Implementado
- **Servicio:** order-svc
- **Requisitos de sistema:** RS-01

## Historia de usuario

Como cliente u operador, quiero ver un pedido o la lista de pedidos, para seguir su estado.

## Requisitos funcionales

- **RF-1** `GET /orders` devuelve todos los pedidos.
- **RF-2** `GET /orders/{id}` devuelve un pedido; 404 si no existe.

## Escenarios de aceptación

1. **Dado** un pedido existente, **cuando** se pide por id, **entonces** responde 200 con todos sus campos.
2. **Dado** un id inexistente, **cuando** se pide por id, **entonces** responde 404.

## Fuera de alcance / notas

- Sin paginación ni filtros (tarea sugerida: paginar y filtrar por `status`).
