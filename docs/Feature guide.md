# Feature Development Guide

## Purpose

Every feature should follow the same structure.

Example:

```text
usermanagement/
```

---

# Feature Structure

```text
usermanagement/

├── api/
├── dto/
├── domain/
├── repository/
├── mapper/
└── usecase/
```

---

# API Layer

Contains:

```text
Controllers

Request Models

Response Models
```

Responsibilities:

```text
HTTP

Input Validation

Response Generation
```

No business logic.

---

# UseCase Layer

Contains:

```text
Business Workflows
```

Examples:

```text
CreateUserUseCase

DeleteUserUseCase

LoginUseCase
```

---

# Domain Layer

Contains:

```text
Entities

Value Objects

Enums

Ports

Business Rules
```

No Spring dependencies allowed.

---

# Repository Layer

Contains:

```text
Jpa Entities

Persistence Implementations

Database Adapters
```

---

# Mapper Layer

Contains:

```text
MapStruct Mappers
```

Use:

```java
adapt(...)
```

whenever possible.

---

# Events

Feature-specific events belong inside the feature.

Example:

```java
UserCreatedEvent
```

---

# Permissions

Feature permissions belong inside the feature.

Example:

```java
USER_CREATE

USER_DELETE
```

---

# Logging

Feature log events belong inside the feature.

Example:

```java
USER_CREATED

USER_UPDATED
```

---

# Localization

Feature message keys belong inside the feature.

Example:

```java
USER_NOT_FOUND

USER_DISABLED
```

---

# Testing

Each feature must include:

- Domain Tests
- UseCase Tests
- Repository Tests
- API Tests

---

# Best Practice

Every new feature should look similar to:

```text
UserManagement
```

The UserManagement feature is the official reference implementation and should be treated as the source of truth for development patterns.