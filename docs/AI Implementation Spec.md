# AI_IMPLEMENTATION_SPEC.md

# Resam Spring Base Platform
## Complete AI Implementation Specification

Version: 1.0

---

# IMPORTANT

This repository is a PLATFORM first and an APPLICATION second.

It is NOT a normal Spring Boot application.

The goal is to build a reusable enterprise-grade framework that future projects can use as a base.

When making decisions:

1. Prefer future-proofing over simplicity.
2. Prefer convention over configuration.
3. Prefer preventing mistakes over flexibility.
4. Prefer reusable framework features over project-specific solutions.
5. Never bypass architecture for convenience.

If a framework decision and a project decision conflict, choose the framework decision.

---

# TECHNOLOGY STACK

Java: 21

Framework:

- Spring Boot 3.x

Build Tool:

- Maven

Database:

- Oracle

Tests:

- JUnit 5
- Mockito
- ArchUnit

Documentation:

- Swagger
- Scalar
- JavaDocs

Framework Utilities:

- Lombok
- MapStruct

---

# CORE DESIGN PRINCIPLES

## Future-Proofing

Code must depend on contracts.

Never on technologies.

Examples:

Good:

```text
FileStorage
CacheProvider
ConfigurationProvider
MailProvider
EventPublisher
```

Bad:

```text
MinioClient
S3Client
RedisClient
KafkaProducer
VaultClient
```

---

## Strong OOP

Use:

- Aggregate Roots
- Rich Domain Models
- Encapsulation
- Factories
- Value Objects
- Enums
- Package Visibility

Avoid:

- Anemic Models
- Giant Service Classes
- Global Static State

---

## No Duplication

Centralize:

- CRUD
- Responses
- Exceptions
- Logging
- Configuration
- Mapping
- Validation
- Localization

---

## Developer Safety

The framework should force developers into the correct implementation.

Use:

- Base Classes
- Custom Annotations
- ArchUnit
- Folder Rules
- Dependency Rules

---

# ARCHITECTURE

The architecture combines:

```text
Vertical Slice
+
Hexagonal
+
DDD
```

---

# VERTICAL SLICE

Structure by feature.

Correct:

```text
auth/

usermanagement/

orders/

products/
```

Wrong:

```text
controllers/

services/

repositories/
```

---

# HEXAGONAL

Flow:

```text
Client
 ↓
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

Business code talks only to:

```text
Ports
```

Infrastructure implements:

```text
Adapters
```

---

# DDD

Business logic belongs inside:

```text
Domain
```

Example:

```java
order.cancel();

user.changePassword();
```

Not:

```java
orderService.cancel();

userService.changePassword();
```

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

Everything extends framework base classes.

---

# AGGREGATE ROOT

```java
AggregateRoot
    extends BaseDomainObject
```

Capabilities:

```text
raiseEvent()

pullEvents()

clearEvents()
```

Domain events originate here.

---

# BUSINESS SERVICES

Forbidden:

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

LoginUseCase

ResetPasswordUseCase
```

---

# GLOBAL SERVICES

Allowed only in:

```text
core/services/
```

Examples:

```text
AppLogger

EventPublisher

CurrentUser

TokenService

MappingService

LocalizationService

AppConfigurationService

EnvironmentService

AuditService

CacheService
```

---

# REPOSITORY FRAMEWORK

Implement:

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

---

# REPOSITORY PLACEMENT

Inside Domain:

```text
UserRepository
```

Port only.

Inside Repository:

```text
UserRepositoryImpl

UserJpaEntity
```

Adapter implementation.

---

# CRUD FRAMEWORK

Implement:

```java
CrudController
```

Provide:

```text
GET

GET ALL

POST

PUT

DELETE
```

automatically.

---

# DTO STRATEGY

Technology:

```text
Lombok
```

Pattern:

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
@AppMapper

BaseMapper

MappingService
```

Support:

```java
user.adapt(UserDto.class);
```

and

```java
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

Framework annotations should be wrapped.

---

# RESPONSE FRAMEWORK

Implement:

```java
ApiResponse<T>

ApiError

BaseResult
```

All API responses must use wrappers.

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

Must:

```text
Log

Audit

Translate

Respond
```

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
Request Validation
    → DTO

Business Validation
    → Domain
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

Analytics

Logging

Notifications

External Integration
```

Not business workflows.

---

# LOGGING FRAMEWORK

Implement:

```java
AppLogger

LogCategory

LogEvent

LogData

LogContext
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

Developers should never call:

```java
logger.info(...)
```

directly.

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

Separate from normal logs.

---

# SECURITY FRAMEWORK

Authentication:

```text
JWT

Refresh Tokens
```

Endpoints:

```text
POST /auth/login

POST /auth/refresh

POST /auth/logout

POST /auth/revoke-all
```

---

# AUTHORIZATION

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

Never access SecurityContext directly.

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

# CACHE FRAMEWORK

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

# STORAGE FRAMEWORK

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

# MESSAGING FRAMEWORK

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

Business code depends only on contracts.

---

# MAIL FRAMEWORK

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

# LOCALIZATION FRAMEWORK

Files:

```text
en.yml

fa.yml

ko.yml
```

No hardcoded text.

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

Exception Translation

Log Translation

Parameters

User Language
```

---

# CONFIGURATION FRAMEWORK

Provider Pattern.

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

---

# CONFIGURATION PRIORITY

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

Implement:

```java
AppConfiguration
```

Containing:

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

Access:

```java
config.project();

config.jwt();

config.database();
```

Never use:

```java
@Value
```

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

Everything optional.

---

# DOCUMENTATION FRAMEWORK

Support:

```text
Swagger

Scalar

OpenAPI
```

Configurable.

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

Use:

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

# DOCUMENTATION REQUIREMENTS

Every public component needs JavaDoc.

Every major class needs a brief description.

Generate:

```text
README.md

MANIFEST.md

MANIFEST_SUMMARY.md

ARCHITECTURE.md

DEV_GUIDE.md

FEATURE_GUIDE.md
```

---

# ARCHITECTURE ENFORCEMENT

Technology:

```text
ArchUnit
```

Validate:

```text
Folder Placement

Dependency Rules

Inheritance Rules

Feature Structure

Domain Isolation
```

Build fails on violation.

---

# BOOTSTRAP SCRIPT

Implement:

```text
init-project.sh

init-project.bat
```

Responsibilities:

```text
Rename Package

Rename Artifact

Rename Configurations

Rename Docker Assets

Rename Swagger

Rename Scalar

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

# SAMPLE FEATURE

Create:

```text
UserManagement
```

Must demonstrate:

```text
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

This feature is:

```text
Reference Implementation

Developer Template

Living Documentation
```

---

# IMPLEMENTATION PHASES

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
Events Framework

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

---

# DEFINITION OF DONE

Project is complete only if:

✅ Project builds

✅ All tests pass

✅ ArchUnit passes

✅ Oracle works

✅ H2 test database works

✅ JWT works

✅ Refresh Token works

✅ Logging works

✅ Events work

✅ Localization works

✅ Configuration Providers work

✅ Redis support implemented

✅ Kafka support implemented

✅ RabbitMQ support implemented

✅ MinIO support implemented

✅ Swagger works

✅ Scalar works

✅ Bootstrap scripts work

✅ UserManagement feature complete

✅ Javadocs generate successfully

---

# FINAL EXPECTATION

The final repository must feel like a custom application development platform built on top of Spring Boot.

Developers should spend their time building business requirements while the framework owns:

- Architecture
- Security
- Configuration
- Logging
- Localization
- Validation
- Infrastructure
- Documentation
- Conventions

The finished project should be suitable as the foundation for all future Spring Boot projects.