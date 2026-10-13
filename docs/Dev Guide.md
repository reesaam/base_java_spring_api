# Developer Guide

## Setup

### Clone

```bash
git clone <repository>
```

### Initialize Project

```bash
./init-project.sh
```

or

```bat
init-project.bat
```

This script:

- Renames package
- Renames project
- Updates configs
- Updates Docker files
- Updates Swagger metadata
- Updates Scalar metadata

---

# Creating a New Feature

Example:

```text
inventory/
```

Create:

```text
inventory/

    api/

    dto/

    domain/

    repository/

    usecase/

    mapper/

    enums/
```

---

# Step 1

Create Domain.

Example:

```java
public class Inventory
        extends AggregateRoot {
}
```

---

# Step 2

Create DTOs.

```java
@Builder(toBuilder = true)
public class CreateInventoryRequest {
}
```

---

# Step 3

Create Mapper.

```java
@AppMapper
public interface InventoryMapper
        extends BaseMapper<...> {
}
```

---

# Step 4

Create Repository.

```java
public interface InventoryRepository
        extends BaseRepository<Inventory> {
}
```

---

# Step 5

Create UseCases.

```java
public class CreateInventoryUseCase
        extends BaseUseCase {
}
```

---

# Step 6

Create Controller.

```java
public class InventoryController
        extends CrudController {
}
```

---

# Localization

Never:

```java
"User not found"
```

Use:

```java
MessageKey.USER_NOT_FOUND
```

---

# Logging

Never:

```java
logger.info(...)
```

Use:

```java
appLogger.log(...)
```

---

# Configuration

Never:

```java
@Value(...)
```

Use:

```java
config.jwt()

config.database()

config.project()
```

---

# Security

Use:

```java
@RequirePermission(...)
```

Never use raw permission strings.

---

# Events

Events should be used for:

- Notifications
- Logging
- Analytics
- Audit
- Integrations

Not for core business rules.