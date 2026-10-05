# AGENTS.md

Guía para quien trabaje en este repositorio, humano o asistente de IA.

## Qué es

`ahk-delivery-order-svc`: servicio de pedidos de la maqueta ahk-delivery. Crea y entrega pedidos. Orquesta a `catalog-svc`, `predictor` y `courier-svc`, guarda en PostgreSQL y publica eventos `order.created` y `order.delivered` en RabbitMQ.
El contexto del sistema completo está en [ahk-delivery-infra](https://github.com/ezequieljsosa/ahk-delivery-infra): `docs/requirements.md` (requisitos y diagramas de secuencia) y `docs/architecture.md` (C4).

## Comandos

- Compilar y correr: `./mvnw spring-boot:run`
- Tests: `./mvnw test` (todavía no hay: cada escenario de aceptación nuevo debe tener su test)
- Calidad: `pre-commit run --all-files` o `mvn checkstyle:check pmd:check spotbugs:check`

## Cómo trabajar: specs primero (SDD)

1. Antes de tocar código, leer el spec de la feature en `specs/NNN-nombre/spec.md`. Si no existe, crearlo (formato en `specs/README.md`).
2. Escribir `plan.md` y `tasks.md` en la carpeta del spec y recién después implementar.
3. Cada escenario de aceptación del spec debe tener su test.
4. Si el cambio altera un contrato entre servicios (REST o eventos), actualizar también los requisitos en [ahk-delivery-infra](https://github.com/ezequieljsosa/ahk-delivery-infra/blob/main/docs/requirements.md) y avisar a los otros repos afectados.
5. Al terminar, marcar el spec como *Implementado*.

## Reglas de este servicio

- Controllers explícitos con `@RestController`; no usar Spring Data REST.
- `order-svc` es el dueño del estado del pedido: solo él lo cambia. Ver `specs/000-ciclo-de-vida-del-pedido`.
- Si cambia el JSON de un evento (`OrderEvent`) se rompe al worker: coordinar el cambio y actualizar el spec 004.
- Las llamadas a otros servicios van con timeout y, cuando el spec lo dice, toleran fallas.
- Cada servicio es dueño de su base: nunca acceder a la base de otro servicio.

## Estilo y calidad

- Formato automático con `pre-commit` (google-java-format AOSP): no discutir el formato, dejar que lo aplique el hook.
- `checkstyle.xml` es liviano a propósito (imports, nombres, buenas prácticas básicas, líneas hasta 120). No endurecerlo sin que lo pidan.
- Identificadores en inglés; mensajes de log y documentación en español.
- Sin imports con comodín en código de producción.

## Antes de dar por terminada una tarea

- Correr `pre-commit run --all-files`, dejar que los formatters reescriban archivos y revisar el diff.
- Verificar que el spec refleja lo implementado.
- No commitear secretos ni artefactos generados (`target/`, `*.joblib`, `__pycache__/`).
