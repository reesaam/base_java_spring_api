# Resam Spring Base Platform

## Overview

Resam Spring Base Platform is a reusable enterprise-grade Spring Boot starter designed to accelerate development while enforcing architecture, conventions, and best practices.

The platform combines:

- Vertical Slice Architecture
- Hexagonal Architecture
- Domain Driven Design (DDD)
- Strong OOP Principles
- Convention-Driven Development
- Developer Safety

The goal is to create a codebase where:

- Developers move faster
- Business logic remains clean
- Infrastructure is replaceable
- Architecture is enforced
- Future refactoring is minimized

---

# Key Principles

## Future-Proofing

Applications depend on contracts rather than technologies.

Examples:

- ConfigurationProvider
- FileStorage
- EventPublisher
- CacheProvider
- MailProvider

Infrastructure can be replaced without changing business code.

---

## Strong OOP

The platform promotes:

- Rich Domain Models
- Aggregate Roots
- Encapsulation
- Factories
- Typed Configuration
- Enum-Driven Design

---

## No Duplication

Built-in support for:

- CRUD operations
- Exception handling
- Logging
- Mapping
- Response handling
- Validation
- Localization

---

## Developer Safety

The platform actively prevents architectural mistakes using:

- Base classes
- Custom annotations
- ArchUnit rules
- Folder validation
- Dependency validation
- Build-time architecture checks

---

# Architecture

```text
Vertical Slice
+
Hexagonal
+
DDD
```

Feature-first organization:

```text
core/

auth/

usermanagement/

orders/

products/
```

---

# Built-In Infrastructure

The platform includes:

- Security Framework
- JWT Authentication
- Refresh Tokens
- Permission-Based Authorization
- Logging Framework
- Event Framework
- Localization Framework
- Configuration Framework
- CRUD Framework
- Mapping Framework
- Documentation Framework

---

# Configuration System

Supports:

- Local Configuration Files
- Environment Variables
- Vault
- Database Configuration
- Remote Configuration Providers

Everything is exposed through a single:

```java
AppConfiguration
```

object.

Examples:

```java
config.jwt()

config.database()

config.kafka()

config.project()
```

---

# Security

Features:

- JWT Access Tokens
- Refresh Tokens
- Permission-Based Authorization
- Role Management
- CurrentUser Service
- Custom Security Annotations

Example:

```java
@RequirePermission(USER_CREATE)
```

---

# Events

Events are used for:

- Audit Trails
- Notifications
- Analytics
- Logging
- External Integrations

Examples:

```java
UserCreatedEvent

OrderCreatedEvent
```

---

# Logging

Logging is centralized and structured.

Automatic information:

- Timestamp
- User
- Request
- Trace Id
- IP Address
- Class
- Method
- Line Number
- Environment

Categories:

- Audit
- Business
- Security
- Technical

---

# Localization

Multi-language support is built-in.

Features:

- YAML Translation Files
- Enum-Based Message Keys
- Parameterized Messages
- Localized Exceptions
- Localized Logs

No hardcoded texts allowed.

---

# Mapping

Uses:

- MapStruct
- BaseMapper
- Custom Mapper Annotations

Convenience Mapping:

```java
user.adapt(UserDto.class);
```

---

# CRUD Framework

Every feature receives built-in CRUD support.

Provided through:

```java
CrudController
```

and

```java
BaseRepository
```

Standard operations:

- Get
- Get All
- Create
- Update
- Delete

---

# Architecture Enforcement

ArchUnit validates:

- Folder Structure
- Dependency Direction
- Base Class Usage
- Domain Isolation
- Feature Structure

Builds fail when rules are violated.

---

# Documentation

Generated and maintained through:

- README
- Developer Guide
- Architecture Guide
- Feature Guide
- Javadocs
- Swagger
- Scalar

---

# Bootstrap Process

A project bootstrap script provides:

- Package Renaming
- Project Renaming
- Configuration Updates
- Documentation Updates

Creating a new project should take only a few minutes.

---

# Reference Feature

The platform includes a complete:

```text
UserManagement
```

feature demonstrating:

- CRUD
- Security
- JWT
- Logging
- Events
- Localization
- Validation
- Mapping
- Configuration
- Testing
- Best Practices

This feature acts as:

- Living Documentation
- Feature Template
- Implementation Guide

---

# Final Goal

A developer should be able to:

Clone the repository
→ Run the bootstrap script
→ Start the application
→ Review UserManagement
→ Build new features

without needing additional architectural guidance.