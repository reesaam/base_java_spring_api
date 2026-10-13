# ARCHITECTURE.md

# Resam Spring Base Platform Architecture

Version: 1.0

---

# Purpose

Resam Spring Base Platform is an enterprise-grade Spring Boot starter platform designed to standardize development, enforce architecture, accelerate delivery, and remain future-proof.

The platform combines:

```text
Vertical Slice Architecture
+
Hexagonal Architecture
+
Domain Driven Design (DDD)
+
Strong OOP
+
Convention-Driven Development
```

---

# Design Goals

## Future-Proof

Business code must never depend on infrastructure technologies.

Possible infrastructure replacements:

```text
Local Config      → Vault

PostgreSQL        → SQL Server

MinIO             → S3

Redis             → Another Cache

RabbitMQ          → Kafka

JWT               → External Identity Provider
```

No business code changes should be required.

---

## Developer Safety

The framework should make mistakes difficult.

Implemented using:

```text
Base Classes

Custom Annotations

Architecture Rules

Folder Validation

Dependency Validation

ArchUnit Tests
```

---

## No Duplication

The framework provides reusable implementations for:

```text
CRUD

Logging

Localization

Configuration

Responses

Validation

Exception Handling

Documentation
```

---

# Architecture Style

The platform combines three architectural approaches.

---

## 1. Vertical Slice

Code is organized by feature.

### Traditional Structure

```text
controllers/

services/

repositories/

entities/
```

Problems:

```text
Feature implementation spread everywhere

Hard navigation

High coupling
```

---

### Vertical Slice Structure

```text
usermanagement/

orders/

products/

reports/
```

Everything related to a feature lives together.

---

## 2. Hexagonal Architecture

Business logic communicates through contracts.

### High Level

```text
UseCase
    ↓
Port
    ↓
Adapter
```

### Example

```text
CreateUserUseCase
        ↓
UserRepository
        ↓
JpaUserRepository
        ↓
Database
```

Business logic does not care about the implementation.

---

## 3. Domain Driven Design

Business rules belong inside the domain.

### Good

```java
user.changePassword(...);

order.cancel();
```

### Bad

```java
userService.changePassword(...);

orderService.cancel(...);
```

The domain protects itself.

---

# Architectural Flow

```text
┌──────────────┐
│    Client    │
└──────┬───────┘
       │
       ▼
┌──────────────┐
│  Controller  │
└──────┬───────┘
       │
       ▼
┌──────────────┐
│   UseCase    │
└──────┬───────┘
       │
       ▼
┌──────────────┐
│   Domain     │
└──────┬───────┘
       │
       ▼
┌──────────────┐
│    Ports     │
└──────┬───────┘
       │
       ▼
┌──────────────┐
│   Adapters   │
└──────┬───────┘
       │
       ▼
┌──────────────┐
│Infrastructure│
└──────────────┘
```

---

# Dependency Rules

Allowed:

```text
Controller
    ↓
UseCase
    ↓
Domain
    ↓
Port
```

Allowed:

```text
Adapter
    ↓
Port
```

Forbidden:

```text
Domain
    ↓
Spring
```

Forbidden:

```text
Domain
    ↓
Database
```

Forbidden:

```text
Controller
    ↓
Repository
```

---

# Project Structure

```text
src/main/java/io/project

    core/

    auth/

    usermanagement/

    products/

    orders/

    reports/
```

---

# Core Layer

```text
core/

    annotations/
    config/
    dto/
    entity/
    enums/
    events/
    exceptions/
    logging/
    mapper/
    repository/
    response/
    result/
    security/
    services/
    usecase/
    validation/
    utilities/
```

Responsibilities:

```text
Global Infrastructure

Shared Framework Components

Application Standards

Architecture Enforcement
```

---

# Feature Structure

Example:

```text
usermanagement/

├── api/
│
├── dto/
│
├── domain/
│
├── repository/
│
├── mapper/
│
└── usecase/
```

---

# Layer Responsibilities

## API

Contains:

```text
Controllers

Request Models

Response Models
```

Handles:

```text
HTTP

Validation

Responses
```

Contains no business logic.

---

## UseCase

Contains:

```text
Business Workflows
```

Examples:

```text
Create User

Login

Reset Password

Delete User
```

Coordinates the application.

---

## Domain

Contains:

```text
Entities

Aggregates

Business Rules

Domain Events

Interfaces (Ports)
```

Contains all business knowledge.

---

## Repository

Contains:

```text
JPA Entities

Repository Implementations

Database Adapters
```

Handles persistence.

---

## Mapper

Contains:

```text
MapStruct Mappers

Conversions

DTO Mapping
```

---

# Base Framework

Every major component extends a base implementation.

```text
BaseController

CrudController

BaseUseCase

BaseRepository

BaseMapper

BaseDto

BaseModel

BaseEntity

BaseResult

BaseException

BaseEvent

BaseDomainObject

AggregateRoot
```

---

# AggregateRoot

Every aggregate root extends:

```java
AggregateRoot
```

Capabilities:

```text
raiseEvent()

pullEvents()

clearEvents()
```

Example:

```java
user.raiseEvent(
    new UserCreatedEvent(...)
);
```

---

# Configuration Architecture

All configuration is centralized.

Supported sources:

```text
Vault

Environment Variables

YAML Files

Database

Remote Providers
```

Priority:

```text
Vault
↓
Environment Variables
↓
YAML
↓
Defaults
```

---

# AppConfiguration

Single source of truth.

```java
AppConfiguration
```

Contains:

```text
Project

Database

JWT

Redis

Kafka

RabbitMQ

Elastic

MinIO

Mail

Security

Localization

Documentation

Monitoring

Feature Flags
```

Access:

```java
config.jwt();

config.database();

config.project();
```

---

# Security Architecture

Authentication:

```text
JWT
Refresh Tokens
```

Authorization:

```text
Permissions
```

Example:

```java
@RequirePermission(
    USER_CREATE
)
```

Roles become collections of permissions.

---

# Logging Architecture

Logging is centralized.

Categories:

```text
Audit

Business

Security

Technical
```

Captured automatically:

```text
User

IP

Request

Class

Method

Line

Trace Id

Timestamp
```

---

# Event Architecture

Events are reserved for:

```text
Audit

History

Analytics

Notifications

External Integrations
```

Examples:

```java
UserCreatedEvent

UserDeletedEvent

OrderCreatedEvent
```

---

# Localization Architecture

No hardcoded text.

### Good

```java
MessageKey.USER_NOT_FOUND
```

### Bad

```java
"User not found"
```

Supported:

```text
Multi Language

Parameterized Messages

Localized Exceptions

Localized Logs
```

---

# Mapping Architecture

Technology:

```text
MapStruct
```

Pattern:

```java
adapt(...)
```

Example:

```java
UserDto dto =
    user.adapt(UserDto.class);
```

---

# Architecture Enforcement

Validated using:

```text
ArchUnit
```

Rules:

```text
Folder Structure

File Placement

Feature Placement

Inheritance Rules

Dependency Rules

Domain Isolation
```

Build must fail on violation.

---

# Documentation Strategy

Documentation is part of the platform.

Provided:

```text
README.md

DEV_GUIDE.md

FEATURE_GUIDE.md

ARCHITECTURE.md

Javadocs

Swagger

Scalar
```

---

# Living Documentation

The platform contains a complete:

```text
UserManagement
```

feature.

Used as:

```text
Reference Implementation

Feature Template

Learning Resource

Architecture Example
```

Every framework capability must be demonstrated inside this feature.

---

# Developer Workflow

```text
Clone Repository
        ↓
Run Bootstrap Script
        ↓
Rename Package
        ↓
Run Application
        ↓
Study UserManagement
        ↓
Build New Features
```

---

# Final Outcome

The framework provides:

```text
Architecture

Security

Configuration

Localization

Logging

Documentation