# ahk-delivery-order-svc

Servicio de pedidos. Crea y entrega pedidos. Orquesta a `catalog-svc`, `predictor` y `courier-svc`, guarda en PostgreSQL y publica eventos `order.created` y `order.delivered` en RabbitMQ.

Forma parte de la maqueta de delivery **ahk-delivery**. La arquitectura, los requisitos del sistema y cómo levantar todo con Docker Compose están en [ahk-delivery-infra](https://github.com/ezequieljsosa/ahk-delivery-infra).

- **Stack:** Java 21, Spring Boot 4.1, Spring Data JPA, Spring AMQP
- **Datos:** PostgreSQL (base `orders`) y RabbitMQ (exchange `orders`)
- **Imagen:** `ezequieljsosa/ahk-delivery-order-svc`

## API

| Método | Ruta | Descripción |
|---|---|---|
| POST | `/orders` | Crea un pedido (valida, estima ETA, asigna repartidor, publica evento) |
| GET | `/orders` | Lista los pedidos |
| GET | `/orders/{id}` | Un pedido |
| POST | `/orders/{id}/deliver` | Informa la entrega con `actualMinutes` |

## Ejecutar localmente

Dependencias: PostgreSQL con la base `orders` (usuario/clave `ahk`), RabbitMQ, y los otros tres servicios (o usar `ahk-delivery-infra` para levantar todo).

Configuración (variables de entorno): `DB_URL`, `DB_USER`, `DB_PASSWORD`, `RABBIT_HOST`, `CATALOG_URL`, `COURIER_URL`, `PREDICTOR_URL`.

```bash
./mvnw spring-boot:run
```

El servicio escucha en el puerto **8082**.

Imagen de Docker:

```bash
docker build -t ezequieljsosa/ahk-delivery-order-svc .
```

## Specs

Las features están descritas en [`specs/`](specs/README.md) (desarrollo guiado por specs). Las marcadas *Propuesto* son tareas para los alumnos.

## Calidad de código

```bash
pre-commit install                                  # una sola vez: los hooks corren en cada commit
pre-commit run --all-files                          # formato + análisis estático
mvn checkstyle:check pmd:check spotbugs:check       # solo el análisis estático
```

Necesita un JDK 21 declarado en `~/.m2/toolchains.xml` (plugin de toolchains).

## Licencia

[MIT](LICENSE)
