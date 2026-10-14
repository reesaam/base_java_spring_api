# Resam Spring Base Platform Manifest v1.0

## Vision

A reusable enterprise-grade Spring Boot platform designed around:

- Vertical Slice Architecture
- Hexagonal Architecture
- Domain Driven Design (DDD)
- Strong OOP
- Convention-Driven Development
- Developer Safety
- Future-Proofing

Goals:

- Prevent developer mistakes
- Reduce duplication
- Speed up development
- Keep business logic isolated
- Enable infrastructure replacement without refactoring
- Provide a ready-to-use enterprise starter

---

# Core Principles

## Future-Proofing

Depend on capabilities, not technologies.

Examples:

```text
FileStorage
EventPublisher
ConfigurationProvider
CacheProvider
MailProvider
```

Never:

```text
S3Client
KafkaProducer
VaultClient
RedisClient
```

---

## Strong OOP

- Rich domain models
- Aggregate roots
- Encapsulation
- Factories
- Interfaces on boundaries
- Package-private by default
- Enums instead of strings

---

## No Duplication

- Shared CRUD
- Shared logging
- Shared exception handling
- Shared mapping
- Shared validation
- Shared responses

---

## Developer Safety

Architecture should make mistakes difficult.

Implemented with:

- Base classes
- Custom annotations
- ArchUnit rules
- Package restrictions
- Folder restrictions
- Build-time validation

---

# Architecture

```text
Vertical Slice
+
Hexagonal
+
DDD
```

---

# Root Structure

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

# Core Services

Global services available everywhere.

```text
AppLogger

EventPublisher

CurrentUser

TokenService

MappingService

AppConfigurationService

LocalizationService

AuditService

EnvironmentService
```

Flutter equivalent:

```text
GetxService
```

---

# Feature Structure

```text
usermanagement/

    api/

    dto/

    domain/

    repository/

    usecase/

    mapper/

    enums/
```

---

# Base Classes

Every major component extends a base.

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

# Aggregate Root

```java
AggregateRoot
    extends BaseDomainObject
```

Features:

```text
raiseEvent()

pullEvents()

clearEvents()
```

---

# Domain Rules

Domain layer must never depend on:

```text
Spring

Infrastructure

External Services
```

Forbidden:

```java
@Service

@Autowired

@Repository
```

inside domain.

---

# Repository Strategy

## Base Repository

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

## Custom Repository

```java
UserRepository
    extends BaseRepository<User>
```

Custom methods:

```java
findByEmail()

findActiveUsers()

findByUsername()
```

---

# CRUD Framework

Built-in APIs:

```text
Get

GetAll

Create

Update

Delete
```

Provided through:

```java
CrudController
```

---

# UseCase Strategy

Represents business actions.

Examples:

```text
CreateUserUseCase

LoginUseCase

RegisterUserUseCase

DeleteUserUseCase
```

---

# Factory Strategy

## Simple Objects

```java
User.create(...)
```

## Complex Objects

```java
OrderFactory

InvoiceFactory

ReportFactory
```

Rule:

```text
Simple → Static Create

Complex → Factory Class
```

---

# DTO Strategy

Technology:

```text
Lombok
```

Pattern:

```java
@Builder(toBuilder = true)
```

Provides:

```text
Builder

CopyWith-like support

Immutability-friendly design
```

Example:

```java
user.toBuilder()
    .email("new@mail.com")
    .build();
```

---

# Mapping Strategy

Technology:

```text
MapStruct
```

Custom Annotation:

```java
@AppMapper
```

Base:

```java
BaseMapper
```

Supported:

```java
adapt(...)
```

Examples:

```java
UserDto dto =
    user.adapt(UserDto.class);

User user =
    request.adapt(User.class);
```

Custom mapping supported.

---

# Result Strategy

Success:

```java
ApiResponse<T>
```

Failure:

```java
ApiError
```

Exceptions:

```java
BusinessException

ValidationException

NotFoundException

SecurityException
```

Handled globally.

---

# Exception Framework

Global:

```java
GlobalExceptionHandler
```

Responsibilities:

```text
Translate Exceptions

Standardize Responses

Log Errors

Localize Messages

Track Audit Events
```

---

# Logging Framework

Components:

```text
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

Examples:

```java
USER_CREATED

USER_UPDATED

ORDER_CREATED

LOGIN_FAILED
```

Automatically Captures:

```text
Timestamp

RequestId

TraceId

UserId

Username

IP

Class

Method

Line Number

Thread

Environment

Application
```

Usage:

```java
logger.log(
    LogEvent.USER_CREATED,
    data
);
```

---

# Events Framework

Purpose:

```text
Audit

History

Notifications

Analytics

Logging

External Integrations
```

Components:

```java
DomainEvent

BaseEvent

EventPublisher

EventHandler
```

Usage:

```java
raiseEvent(
    new UserCreatedEvent(...)
);
```

---

# Security Architecture

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

---

# Authorization

Permission based.

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

Global Service:

```java
CurrentUser
```

Usage:

```java
currentUser.id();

currentUser.username();

currentUser.hasPermission(...);
```

---

# Token Builder

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

## Supported Sources

```text
Vault

Environment Variables

Profile Yml

Remote Config API

Defaults
```

---

# Provider Pattern

```java
ConfigurationProvider
```

Implementations:

```java
YamlConfigurationProvider

VaultConfigurationProvider

RemoteConfigurationProvider
```

---

# Load Priority

```text
Vault
↓
Environment Variables
↓
Profile YML
↓
Defaults
```

---

# Main Configuration Model

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

Minio

Mail

Storage

Vault

Swagger

Scalar

Documentation

Cors

Localization

Security

Monitoring

Audit

Feature Flags
```

---

# Configuration Access

```java
config.project();

config.jwt();

config.database();

config.redis();

config.kafka();

config.documentation();
```

Never:

```java
@Value(...)
```

---

# AppConfiguration Modules

```text
ProjectConfiguration

EnvironmentConfiguration

DatabaseConfiguration

JwtConfiguration

RedisConfiguration

KafkaConfiguration

RabbitMqConfiguration

ElasticConfiguration

MinioConfiguration

MailConfiguration

StorageConfiguration

VaultConfiguration

SwaggerConfiguration

DocumentationConfiguration

CorsConfiguration

LocalizationConfiguration

SecurityConfiguration

MonitoringConfiguration

AuditConfiguration

FeatureConfiguration
```

---

# Documentation Configuration

Supports:

```text
Swagger

Scalar

OpenAPI
```

Example:

```yaml
documentation:

  swagger-enabled: true

  scalar-enabled: true

  swagger-path: /swagger-ui

  scalar-path: /scalar

  title: My API

  version: 1.0.0
```

---

# Feature Flags

Example:

```yaml
features:

  redis:
    enabled: true

  kafka:
    enabled: false

  elastic:
    enabled: false

  swagger:
    enabled: true

  scalar:
    enabled: true
```

---

# Environment Types

```java
LOCAL

DEV

TEST

STAGE

PROD
```

---

# Localization Framework

No hardcoded text allowed.

Supported:

```text
English

Persian

Korean

Future Languages
```

Files:

```text
en.yml

fa.yml

ko.yml
```

Structure:

```yaml
user:
  errors:
    notFound:

order:
  success:
    created:
```

---

# Localization Access

Enums only.

```java
MessageKey.USER_NOT_FOUND
```

Never:

```java
"User not found"
```

Supports:

```text
Parameters

Exception Translation

Log Translation

Current User Language
```

---

# Custom Annotations

Examples:

```java
@AppRestApi

@AppMapper

@AppUseCase

@AppTransactional

@RequirePermission
```

Purpose:

```text
Consistency

Convention

Future-Proofing

Centralized Settings
```

---

# Documentation

Every class requires a brief comment.

Example:

```java
/**
 * Handles user registration workflow.
 */
```

Required for:

```text
Controllers

UseCases

Repositories

Services

Public Methods

Domain Objects
```

---

# Documentation Stack

```text
README.md

DEV_GUIDE.md

ARCHITECTURE.md

FEATURE_GUIDE.md

Javadocs

Swagger

Scalar
```

---

# Javadocs

Equivalent:

```text
dartdoc
```

Generation:

```bash
mvn javadoc:javadoc
```

---

# Architecture Enforcement

Technology:

```text
ArchUnit
```

Enforces:

```text
Folder Placement

File Placement

Dependency Rules

Base Class Inheritance

Feature Structure

Domain Isolation

Package Rules
```

---

# Forbidden Structures

```text
helpers/

common/

utils/

services/
```

outside approved architecture.

---

# Bootstrap Script

Files:

```text
init-project.sh

init-project.bat
```

Responsibilities:

```text
Validate Project Name

Validate Package Name

Rename Package

Rename Artifacts

Rename Configurations

Update Readme

Update Docker Files

Update Swagger Settings

Update Scalar Settings
```

---

# Naming Validation

Project:

```regex
^[a-z][a-z0-9-]*$
```

Package:

```regex
^[a-z]+(\.[a-z][a-z0-9]*)*$
```

---

# Sample Feature

Feature:

```text
UserManagement
```

Purpose:

```text
Living Documentation
```

Demonstrates:

```text
Controllers

CRUD

Repositories

UseCases

Events

Logging

Localization

Permissions

Security

JWT

Refresh Tokens

Audit

Validation

Configuration

Mapping

adapt()

Exceptions

Tests

AggregateRoot

Factories

Feature Flags
```

---

# Project Goal

A developer should be able to:

```text
Clone Project
↓
Run Bootstrap Script
↓
Rename Package
↓
Run Application
↓
Read UserManagement Feature
↓
Build New Features
```

Without needing architectural guidance from another developer.