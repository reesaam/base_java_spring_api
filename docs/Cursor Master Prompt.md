# CURSOR_MASTER_PROMPT.md

# Resam Spring Base Platform
## Master Implementation Prompt

IMPORTANT:

Before implementation, read and follow:

- docs/MANIFEST.md
- docs/ARCHITECTURE.md
- docs/DEV_GUIDE.md
- docs/FEATURE_GUIDE.md
- docs/AI_IMPLEMENTATION_SPEC.md

These documents are the source of truth.

---

# Mission

Implement a reusable Enterprise Spring Boot Starter Platform.

This repository is:

```text
PLATFORM FIRST
APPLICATION SECOND
```

Future projects will be built on top of this repository.

The implementation must prioritize:

1. Future-Proofing
2. Strong OOP
3. No Duplication
4. Developer Safety
5. Convention Over Configuration
6. Architecture Enforcement

---

# Technology Stack

Java:

```text
Java 21
```

Framework:

```text
Spring Boot 3.x
```

Build Tool:

```text
Maven
```

Testing:

```text
JUnit 5
Mockito
ArchUnit
```

Mapping:

```text
MapStruct
```

Boilerplate Reduction:

```text
Lombok
```

Documentation:

```text
Swagger
Scalar
JavaDocs
```

---

# Architecture

The platform combines:

```text
Vertical Slice
+
Hexagonal Architecture
+
Domain Driven Design (DDD)
```

---

# Mandatory Principles

## Future-Proofing

Applications must depend on:

```text
Capabilities
```

not:

```text
Technologies
```

Good:

```text
FileStorage
CacheProvider
EventPublisher
ConfigurationProvider
MailProvider
```

Bad:

```text
MinioClient
RedisClient
KafkaProducer
VaultClient
```

---

## No Duplication

Framework must centralize:

```text
CRUD
Logging
Localization
Validation
Responses
Exceptions
Configuration
```

---

## Developer Safety

The project must make the wrong solution difficult.

Accomplish this through:

```text
Base Classes
Custom Annotations
ArchUnit
Folder Rules
Dependency Validation
```

---

# Forbidden Patterns

Never create:

```text
UserService
OrderService
ProductService
CategoryService
RoleService
```

Business workflows belong in:

```text
UseCases
```

Not services.

---

Never:

```java
@Value(...)
```

Use typed configuration only.

---

Never:

```java
logger.info(...)
```

Use framework logger.

---

Never:

```java
"User not found"
```

Use localization framework.

---

Never:

```java
Date
Calendar
```

Use:

```java
Instant
OffsetDateTime
```

---

# Project Structure

```text
src/main/java/io/template

    core/

    auth/

    usermanagement/

    products/

    orders/

    reports/
```

---

# Core Structure

```text
core/

    annotations/

    config/

    constants/

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

---

# Feature Structure

```text
feature/

    api/

    dto/

    domain/

    repository/

    mapper/

    usecase/

    events/

    permissions/

    validators/

    enums/
```

---

# Base Framework

Implement:

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

Every applicable component must inherit from framework base classes.

---

# AggregateRoot

AggregateRoot extends BaseDomainObject.

Must support:

```text
raiseEvent()

pullEvents()

clearEvents()
```

---

# Hexagonal Rules

Controller
→ UseCase

UseCase
→ Domain

UseCase
→ Ports

Adapters
→ Ports

Allowed:

```text
Controller
    ↓
UseCase
    ↓
Domain
    ↓
Port
    ↓
Adapter
```

Forbidden:

```text
Domain
    ↓
Spring

Domain
    ↓
Database

Domain
    ↓
Infrastructure

Controller
    ↓
Repository
```

---

# Repository Framework

Implement:

```java
BaseRepository<T>
```

Built-in:

```text
getById()

getAll()

create()

update()

delete()

exists()

count()

page()
```

---

# Repository Placement

Port:

```text
domain/UserRepository
```

Implementation:

```text
repository/UserRepositoryImpl
```

---

# CRUD Framework

Create:

```java
CrudController
```

Automatically provides:

```text
GET

GET ALL

POST

PUT

DELETE
```

---

# DTO Framework

Use:

```java
@Builder(toBuilder = true)
```

for:

```text
CopyWith-like functionality
Builder pattern
Immutability-friendly design
```

---

# Mapping Framework

Technology:

```text
MapStruct
```

Implement:

```java
BaseMapper

MappingService

@AppMapper
```

Support:

```java
adapt(...)
```

Examples:

```java
user.adapt(UserDto.class);

dto.adapt(User.class);
```

---

# Response Framework

Implement:

```java
ApiResponse<T>

ApiError

BaseResult
```

Every API endpoint returns standardized responses.

---

# Exception Framework

Implement:

```java
BusinessException

ValidationException

NotFoundException

SecurityException
```

Global:

```java
GlobalExceptionHandler
```

Must:

```text
Log
Audit
Translate
Respond
```

---

# Validation Framework

Request Validation:

```java
@NotNull

@NotBlank

@Email
```

Domain Validation:

```java
BusinessException
```

Rule:

```text
DTO
    ↓
Request Validation

Domain
    ↓
Business Validation
```

---

# Localization Framework

Files:

```text
en.yml

fa.yml

ko.yml
```

Access:

```java
MessageKey.USER_NOT_FOUND
```

Never:

```java
"User not found"
```

Supports:

```text
Multi-language

Parameterized Messages

Log Translation

Exception Translation
```

---

# Logging Framework

Implement:

```java
AppLogger

LogCategory

LogEvent

LogContext

LogData
```

Categories:

```text
AUDIT

BUSINESS

SECURITY

TECHNICAL
```

Automatically capture:

```text
Timestamp

RequestId

TraceId

UserId

Username

IP

Class

Method

Line

Thread

Environment

Application
```

Support custom metadata.

---

# Audit Framework

Implement:

```java
AuditService

AuditRecord

AuditCategory

AuditEvent
```

Examples:

```text
USER_CREATED

USER_UPDATED

USER_DELETED

LOGIN_SUCCESS

LOGIN_FAILED
```

Audit logs must be separated from application logs.

---

# Event Framework

Implement:

```java
DomainEvent

BaseEvent

EventPublisher

EventHandler
```

Used for:

```text
Audit

History

Logging

Analytics

Notifications

External Integrations
```

Not core business workflows.

---

# Security Framework

Authentication:

```text
JWT

Refresh Tokens
```

Required Endpoints:

```text
POST /auth/login

POST /auth/refresh

POST /auth/logout

POST /auth/revoke-all
```

---

# Authorization

Permission Based.

Examples:

```java
USER_CREATE

USER_UPDATE

USER_DELETE

ORDER_APPROVE
```

Roles contain permissions.

---

# Current User

Implement:

```java
CurrentUser
```

Usage:

```java
currentUser.id();

currentUser.username();

currentUser.hasPermission(...);
```

Never use SecurityContext directly in business code.

---

# Token Builder

Implement:

```java
TokenData
```

Supports:

```text
Roles

Permissions

TenantId

BranchId

CompanyId

Custom Claims
```

---

# Configuration Framework

Provider Pattern.

Implement:

```java
ConfigurationProvider
```

Providers:

```java
YamlConfigurationProvider

VaultConfigurationProvider

RemoteConfigurationProvider
```

Priority:

```text
Vault
↓
Environment Variables
↓
Profile YAML
↓
Defaults
```

---

# AppConfiguration

Single source of truth.

Must contain:

```text
Project

Environment

Database

JWT

Redis

Kafka

RabbitMQ

Elastic

MinIO

Mail

Storage

Vault

Documentation

Cors

Localization

Security

Monitoring

Audit

FeatureFlags
```

Usage:

```java
config.project();

config.jwt();

config.database();
```

Never:

```java
@Value(...)
```

---

# Cache Framework

Implement:

```java
CacheProvider

CacheService
```

Implementations:

```java
MemoryCacheProvider

RedisCacheProvider
```

Business code never depends on Redis directly.

---

# Storage Framework

Implement:

```java
FileStorage
```

Implementations:

```java
LocalStorage

MinioStorage

S3Storage
```

Business code never depends on provider SDKs.

---

# Messaging Framework

Implement:

```java
MessagePublisher

MessageConsumer
```

Implementations:

```java
KafkaPublisher

RabbitMqPublisher
```

Business code talks only to contracts.

---

# Mail Framework

Implement:

```java
MailProvider
```

Implementation:

```java
SmtpMailProvider
```

Future providers supported.

---

# Supported Infrastructure

Prepare abstractions, configs and implementations for:

```text
Redis

Kafka

RabbitMQ

Elastic

MinIO

JWT

Mail

Vault

Swagger

Scalar
```

Everything must be optional through feature flags.

---

# Feature Flags

Support:

```text
redis.enabled

kafka.enabled

rabbitmq.enabled

elastic.enabled

swagger.enabled

scalar.enabled
```

---

# Documentation Framework

Support:

```text
Swagger

Scalar

OpenAPI
```

Configurable through configuration.

---

# Documentation Standards

Generate:

```text
README.md

MANIFEST.md

MANIFEST_SUMMARY.md

ARCHITECTURE.md

DEV_GUIDE.md

FEATURE_GUIDE.md

AI_IMPLEMENTATION_SPEC.md
```

Every public class requires JavaDocs.

Every component requires brief comments.

---

# Architecture Enforcement

Use:

```text
ArchUnit
```

Validate:

```text
Folder Placement

Feature Placement

Dependency Rules

Inheritance Rules

Domain Isolation
```

Build must fail on violations.

---

# Bootstrap Script

Implement:

```text
init-project.sh

init-project.bat
```

Responsibilities:

```text
Validate Project Name

Validate Package Name

Rename Package

Rename Artifact

Rename Configurations

Rename Docker Assets

Rename Swagger Metadata

Rename Scalar Metadata

Update README
```

Validation:

Project:

```regex
^[a-z][a-z0-9-]*$
```

Package:

```regex
^[a-z]+(\.[a-z][a-z0-9]*)*$
```

---

# UserManagement Reference Feature

Mandatory.

Must demonstrate:

```text
Controllers

CRUD

UseCases

Repositories

AggregateRoot

Events

Logging

Localization

Permissions

JWT

Refresh Tokens

Validation

Configuration

Adapt()

Mapping

Factories

Feature Flags

Tests

Comments

JavaDocs
```

Acts as:

```text
Reference Implementation

Developer Guide

Living Documentation
```

---

# Implementation Phases

Phase 1
Project Skeleton

Phase 2
Core Framework

Phase 3
Configuration Framework

Phase 4
Security Framework

Phase 5
Logging Framework

Phase 6
Event Framework

Phase 7
Localization Framework

Phase 8
Cache Framework

Phase 9
Storage Framework

Phase 10
Messaging Framework

Phase 11
CRUD Framework

Phase 12
Architecture Enforcement

Phase 13
UserManagement Feature

Phase 14
Documentation

Do not skip phases.

---

# Definition Of Done

Project is complete only if:

✅ Builds successfully

✅ All unit tests pass

✅ All integration tests pass

✅ ArchUnit passes

✅ JWT works

✅ Refresh Tokens work

✅ Logging works

✅ Auditing works

✅ Events work

✅ Localization works

✅ Configuration Providers work

✅ Redis support exists

✅ Kafka support exists

✅ RabbitMQ support exists

✅ MinIO support exists

✅ Swagger works

✅ Scalar works

✅ Bootstrap Script works

✅ UserManagement fully implemented

✅ JavaDocs generate successfully

---

# Final Goal

The final repository must feel like a custom application development platform built on top of Spring Boot.

The platform owns:

- Architecture
- Security
- Configuration
- Logging
- Localization
- Validation
- Infrastructure
- Documentation
- Conventions

Developers focus only on business requirements.