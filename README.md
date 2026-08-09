# basespringapi

Production-ready **modular Spring Boot 3.5** starter for enterprise backends (CRM, ERP, GRC, SaaS, HR, Finance, Inventory, Workflow, …).

- **Java 17** · **Maven multi-module** · **Spring MVC**
- **REST + gRPC** share the same DTOs, enums, domain model, and application services
- **`.proto` files are generated from Java** (`@ProtoContract` / `@ProtoService`) — you do not write protos by hand
- **gRPC-Web** via Envoy (`docker/`)

## Modules

| Module | Responsibility |
|--------|----------------|
| `basespringapi-proto-codegen` | Annotations + Java→proto generator |
| `basespringapi-shared` | `Result`, errors, pagination, time |
| `basespringapi-domain` | Aggregates, enums, ports |
| `basespringapi-application` | Services, DTOs, validators (REST & gRPC contract source) |
| `basespringapi-infrastructure` | JPA, Flyway, security |
| `basespringapi-api` | REST controllers, gRPC adapters, proto compile |
| `basespringapi-bootstrap` | Runnable Spring Boot app |

## Quick start

```bash
mvn clean install
mvn -pl basespringapi-bootstrap spring-boot:run
```

- REST: `http://localhost:8080/api/v1/users`
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- gRPC: `localhost:9090`
- gRPC-Web (Envoy): `docker compose -f docker/docker-compose.yml up` → `http://localhost:8081`

### Sample REST calls

```bash
curl -s -X POST http://localhost:8080/api/v1/users \
  -H "Content-Type: application/json" \
  -d "{\"email\":\"ada@example.com\",\"displayName\":\"Ada Lovelace\"}"

curl -s http://localhost:8080/api/v1/users
```

### Sample gRPC (grpcurl)

```bash
grpcurl -plaintext localhost:9090 list
grpcurl -plaintext -d "{\"email\":\"ada@example.com\",\"displayName\":\"Ada\"}" \
  localhost:9090 site.resam.basespringapi.v1.UserService/CreateUser
```

## Java-first protobuf workflow

1. Annotate DTOs / enums / service interfaces:

```java
@ProtoContract(name = "CreateUserRequest")
public record CreateUserRequest(
    @ProtoField(number = 1) String email,
    @ProtoField(number = 2) String displayName
) {}

@ProtoService(name = "UserService")
public interface UserApplicationService {
    @ProtoRpc(name = "CreateUser")
    UserResponse createUser(CreateUserRequest request);
}
```

2. On `mvn compile` of `basespringapi-api`:
   - `ProtoGeneratorMain` scans annotated types → writes `.proto`
   - `protobuf-maven-plugin` runs `protoc` + `protoc-gen-grpc-java`
3. Implement a thin gRPC adapter that maps proto ↔ DTO and calls the same application service as REST.

Generated artifacts live under:

`basespringapi-api/target/generated-sources/proto-from-java/`  
`basespringapi-api/target/generated-sources/protobuf/`

## Adding a new bounded context

1. Domain aggregate + port in `basespringapi-domain`
2. DTOs + `@ProtoService` facade in `basespringapi-application`
3. JPA adapter in `basespringapi-infrastructure`
4. REST controller + gRPC service + mapper in `basespringapi-api`
5. Rebuild — protos regenerate automatically

## Profiles

| Profile | Purpose |
|---------|---------|
| `local` (default) | H2 in-memory, Flyway, debug logs |
| `prod` | PostgreSQL via `DB_URL` / `DB_USER` / `DB_PASSWORD` |

## Stack

- Spring Boot **3.5.3**
- `net.devh:grpc-server-spring-boot-starter` **3.1.0.RELEASE**
- `protobuf-java` **3.25.5** · gRPC **1.69.1**
- `org.xolstice.maven.plugins:protobuf-maven-plugin`
- Spring Data JPA · Flyway · Spring Security (stateless starter) · springdoc OpenAPI · Actuator

## Design notes

- Clean architecture dependency rule: `bootstrap → api/infrastructure → application → domain → shared`
- Transport-agnostic `AppError` / `Result` mapped to Problem Details (REST) and gRPC `Status`
- Keep `@ProtoField(number=…)` stable once published — numbers are your wire contract
