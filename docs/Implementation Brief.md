# AI_IMPLEMENTATION_BRIEF.md

# Resam Spring Base Platform
## AI Implementation Brief

Version: 2.0

---

# PURPOSE

This project is NOT a regular Spring Boot application.

It is a reusable enterprise-grade application platform that will be used as the foundation of future projects.

The platform should provide:

- Architecture
- Security
- Configuration
- Localization
- Logging
- Auditing
- Validation
- Infrastructure
- Documentation

Developers should be able to focus only on business requirements.

---

# TECHNOLOGY STACK

Language:

```text
Java 21
```

Framework:

```text
Spring Boot 3.x
```

Build:

```text
Maven
```

Database:

```text
Oracle
```

Testing:

```text
JUnit 5
Mockito
ArchUnit
```

Libraries:

```text
MapStruct
Lombok
Jackson
```

Documentation:

```text
Swagger
Scalar
JavaDocs
```

---

# ARCHITECTURE

The platform combines:

```text
Vertical Slice
+
Hexagonal Architecture
+
DDD
```

---

# CORE PHILOSOPHY

## Future-Proofing

The application must depend on capabilities rather than technologies.

Example:

Good:

```text
FileStorage
CacheProvider
ConfigurationProvider
EventPublisher
MailProvider
```

Bad:

```text
MinioClient
RedisClient
KafkaProducer
VaultClient
```

Infrastructure must be replaceable without changing business code.

---

## Strong OOP

The platform promotes:

- Rich Domain Models
- Aggregate Roots
- Encapsulation
- Factories
- Value Objects
- Typed Models
- Package Visibility

---

## No Duplication

Framework should centralize:

- CRUD
- Mapping
- Logging
- Responses
- Validation
- Configuration
- Localization
- Exception Handling

---

## Developer Safety

The platform should make incorrect implementations difficult.

Use:

- Base Classes
- Custom Annotations
- ArchUnit
- Folder Rules
- Dependency Rules

---

# PROJECT STRUCTURE

```text
src/main/java/io/template

    core/

    auth/

    usermanagement/

    orders/

    products/

    reports/
```

Feature-first organization is mandatory.

---

# FEATURE STRUCTURE

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

# CORE STRUCTURE

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

# BASE FRAMEWORK

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

Every applicable framework component must inherit from a base implementation.

---

# BUSINESS RULE

Business Services are forbidden.

Do not create:

```text
UserService
OrderService
ProductService
RoleService
```

Business workflows belong in:

```text
UseCases
```

Examples:

```text
CreateUserUseCase

DeleteUserUseCase

ResetPasswordUseCase

LoginUseCase
```

---

# GLOBAL SERVICES

Allowed inside:

```text
core/services/
```

Examples:

```text
AppLogger

CurrentUser

LocalizationService

AppConfigurationService

EnvironmentService

AuditService

TokenService

CacheService

MappingService

EventPublisher
```

---

# DDD

Business logic belongs in Domain.

Good:

```java
order.cancel();

user.changePassword();
```

Bad:

```java
orderService.cancel();

userService.changePassword();
```

---

# AGGREGATE ROOT

Every aggregate extends:

```java
AggregateRoot
```

Must support:

```text
raiseEvent()

pullEvents()

clearEvents()
```

---

# HEXAGONAL

Flow:

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
    ↓

Infrastructure
```

---

# DEPENDENCY RULES

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

Forbidden:

```text
Domain
    ↓
Spring

Domain
    ↓
JPA

Domain
    ↓
Database

Domain
    ↓
Adapters

Controller
    ↓
Repository
```

---

# REPOSITORY FRAMEWORK

Provide:

```java
BaseRepository<T>
```

Built-in methods:

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

Ports belong in:

```text
domain/
```

Implementations belong in:

```text
repository/
```

---

# CRUD FRAMEWORK

Provide:

```java
CrudController
```

Automatically supports:

```text
GET

GET ALL

POST

PUT

DELETE
```

---

# DTO STRATEGY

Use:

```java
@Builder(toBuilder = true)
```

Purpose:

```text
Builder

Copy-With

Immutable Friendly
```

---

# MAPPING FRAMEWORK

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
user.adapt(UserDto.class);

dto.adapt(User.class);
```

---

# CUSTOM ANNOTATIONS

Implement:

```java
@AppRestApi

@AppMapper

@AppUseCase

@AppTransactional

@RequirePermission
```

Framework annotations should be wrapped whenever conventions are required.

---

# RESPONSE FRAMEWORK

Implement:

```java
ApiResponse<T>

ApiError

BaseResult
```

Everything should return standardized structures.

---

# EXCEPTION FRAMEWORK

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

Responsibilities:

- Logging
- Localization
- Audit
- Standard Responses

---

# VALIDATION FRAMEWORK

Request Validation:

```java
@NotNull

@NotBlank

@Email
```

Business Validation:

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

# EVENT FRAMEWORK

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

Notifications

Analytics

Logging

External Integrations
```

Not business workflows.

---

# LOGGING FRAMEWORK

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
```

Support custom metadata.

---

# AUDIT FRAMEWORK

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

Audit logs must be independent from application logs.

---

# SECURITY FRAMEWORK

Authentication:

```text
JWT

Refresh Token
```

Endpoints:

```text
POST /auth/login

POST /auth/refresh

POST /auth/logout

POST /auth/revoke-all
```

Authorization:

```text
Permission Based
```

Roles contain permissions.

---

# TOKEN BUILDER

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

# CURRENT USER

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

Business code must not directly access SecurityContext.

---

# LOCALIZATION FRAMEWORK

Files:

```text
en.yml

fa.yml

ko.yml
```

Use:

```java
MessageKey.USER_NOT_FOUND
```

Never:

```java
"User not found"
```

Supports:

```text
Localization

Parameters

Exception Translation

Log Translation
```

---

# CONFIGURATION FRAMEWORK

Provider based.

Implement:

```java
ConfigurationProvider
```

Implementations:

```java
YamlConfigurationProvider

VaultConfigurationProvider

DatabaseConfigurationProvider

RemoteConfigurationProvider
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

# APP CONFIGURATION

Centralized model:

```java
AppConfiguration
```

Contains:

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

---

# CACHE FRAMEWORK

Contracts:

```java
CacheProvider

CacheService
```

Implementations:

```java
MemoryCacheProvider

RedisCacheProvider
```

---

# STORAGE FRAMEWORK

Contracts:

```java
FileStorage
```

Implementations:

```java
LocalStorage

MinioStorage

S3Storage
```

---

# MESSAGING FRAMEWORK

Contracts:

```java
MessagePublisher

MessageConsumer
```

Implementations:

```java
KafkaPublisher

RabbitMqPublisher
```

---

# MAIL FRAMEWORK

Contracts:

```java
MailProvider
```

Implementation:

```java
SmtpMailProvider
```

---

# SUPPORTED INFRASTRUCTURE

Prepare integrations and configurations for:

```text
Oracle

Redis

Kafka

RabbitMQ

Elastic

MinIO

Vault

JWT

Mail

Swagger

Scalar
```

All optional.

---

# FEATURE FLAGS

Support:

```text
redis.enabled

kafka.enabled

rabbitmq.enabled

elastic.enabled

swagger.enabled

scalar.enabled
```

Infrastructure modules should be optionally enabled.

---

# DOCUMENTATION FRAMEWORK

Support:

```text
Swagger

Scalar

OpenAPI
```

All configurable.

---

# DATE/TIME RULES

Allowed:

```java
Instant

OffsetDateTime
```

Forbidden:

```java
Date

Calendar
```

All timestamps stored in UTC.

---

# SERIALIZATION RULES

Technology:

```text
Jackson
```

Rules:

```text
UTC

ISO8601

Single Global ObjectMapper
```

---

# DOCUMENTATION

Required documentation:

```text
README.md

MANIFEST.md

MANIFEST_SUMMARY.md

ARCHITECTURE.md

DEV_GUIDE.md

FEATURE_GUIDE.md
```

Every public component requires JavaDocs.

---

# ARCHITECTURE ENFORCEMENT

Use:

```text
ArchUnit
```

Validate:

```text
Feature Placement

Folder Placement

Dependency Rules

Inheritance Rules

Domain Isolation
```

Build fails if violated.

---

# BOOTSTRAP SCRIPT

Implement:

```text
init-project.sh

init-project.bat
```

Capabilities:

```text
Rename Project

Rename Package

Rename Maven Coordinates

Rename Configuration Artifacts

Rename Docker Artifacts

Update Swagger

Update Scalar

Update README
```

Project Name Validation:

```regex
^[a-z][a-z0-9-]*$
```

Package Validation:

```regex
^[a-z]+(\.[a-z][a-z0-9]*)*$
```

---

# USERMANAGEMENT FEATURE

Mandatory reference feature.

Must demonstrate:

```text
CRUD

UseCases

Repositories

Events

Logging

Localization

Permissions

JWT

Refresh Tokens

Validation

Configuration

Mapping

Adapt()

Factories

Feature Flags

Tests

JavaDocs
```

Acts as:

```text
Reference Implementation

Developer Guide

Living Documentation
```

---

# SUCCESS CRITERIA

The project is complete only if:

✅ Builds successfully

✅ Unit tests pass

✅ Integration tests pass

✅ ArchUnit tests pass

✅ Oracle integration works

✅ H2 tests work

✅ JWT works

✅ Refresh Token works

✅ Logging works

✅ Auditing works

✅ Events work

✅ Localization works

✅ Configuration Providers work

✅ Redis support implemented

✅ Kafka support implemented

✅ RabbitMQ support implemented

✅ MinIO support implemented

✅ Swagger works

✅ Scalar works

✅ Bootstrap Script works

✅ UserManagement complete

✅ JavaDocs generate successfully

---

# FINAL EXPECTATION

The finished repository must feel like a custom development platform built on top of Spring Boot.

The framework owns:

- Architecture
- Security
- Configuration
- Logging
- Localization
- Validation
- Infrastructure
- Documentation
- Conventions

Developers focus on business requirements only.