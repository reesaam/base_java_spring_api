# Development Plan

Working plan for building the platform described in `docs/Cursor Master Prompt.md`.
One chat per phase. The owner starts each phase with its start command.

---

# Owner Decisions

These override the master prompt and the docs wherever they conflict.

| # | Decision |
|---|---|
| D1 | Rules copied from Flutter were converted to Java and still apply (`oop`, `no-hardcodes`, `comment-why`). |
| D2 | Patterns (factory, builder, provider) are allowed when they simplify. Overengineering is strictly forbidden: code must stay easy to understand. |
| D3 | At least a one-line comment on every class, interface, enum, record, service, use case and method, explaining what it does (and why when not obvious). |
| D4 | No Flutter, Dart or GetX references anywhere in rules or code. |
| D5 | One chat per phase. The owner starts each phase with the start command below. |
| D6 | Source docs are only the ones the master prompt names, excluding `AI Implementation Spec.md`: `Manifest.md`, `Architecture.md`, `Dev Guide.md`, `Feature guide.md`, plus `Cursor Master Prompt.md`. `Implementation Brief.md`, `Executive Architecture Proposal.md` and `Manifest Summary.md` are not read during development. |
| D7 | Database = connection module only: `DatabaseConfiguration` (connection string, username, password, pool settings) and a `DatabaseConnector` that provides the `DataSource`, behind a `database.enabled` flag. No JPA entities, schemas, migrations, H2, Oracle tests or `DatabaseConfigurationProvider`. |
| D8 | Heavy checks are run by the owner: Oracle, Docker, Redis, Kafka, RabbitMQ, MinIO, Vault, Elastic, and running the app against real infrastructure. The agent only runs `mvn -q compile`, unit tests, and ArchUnit. |
| D9 | The owner picks the chat model per phase from the recommendation below. The agent does not spawn subagents (they re-read context and cost more). |

## Derived decisions (confirm or change)

- **Persistence without a database:** repository ports stay in `domain/`. UserManagement and refresh tokens use in-memory adapters in `repository/`, so a real database adapter can replace them later without touching business code.
- **Base package:** `site.resam` (owner decision; replaces `io.template` from the master prompt; the bootstrap script renames it).
- **Feature folders:** the master prompt layout (`api/`, `dto/`, `domain/`, `repository/`, `mapper/`, `usecase/`, `events/`, `permissions/`, `validators/`, `enums/`) wins over the `controller/` and `port/` layout in `spring-base-project.mdc`.
- **Docker assets:** a minimal `Dockerfile` and `docker-compose.yml` are written (the bootstrap script renames them) but never run by the agent.

---

# Phase Workflow

1. Owner opens a new chat with the recommended model and sends the start command.
2. Agent reads only this file and the docs listed for the phase.
3. Agent implements the phase, then runs light checks (D8).
4. Agent appends a handoff entry (max 10 lines) to **Handoff Log** at the bottom.
5. Owner reviews, runs heavy checks if needed, and starts the next phase.

Model guide:

- **Claude Opus 5.5:** design-critical phases (base framework, security, reference feature).
- **Claude Sonnet 5.5:** framework phases that follow the established design.
- **Composer 2.5:** boilerplate (adapters, YAML, scripts, docs).

---

# Phases

| Phase | Scope | Model | Docs to read | Tokens | Time |
|---|---|---|---|---|---|
| 1 | Project skeleton: `pom.xml`, application class, profile YAML (local/dev/test/stage/prod), package layout | Composer 2.5 | Master Prompt | 40–70k | 0.5 h |
| 2 | Core framework: base classes, `AggregateRoot`, annotations, `ApiResponse`/`ApiError`/`BaseResult`, exceptions, `GlobalExceptionHandler`, mapping + `adapt()` | Claude Opus 5.5 | Master Prompt, Architecture | 140–210k | 1.5–2 h |
| 3 | Configuration framework: `AppConfiguration` + modules, providers (YAML, environment, Vault, remote), feature flags, database connection module (D7) | Claude Sonnet 5.5 | Master Prompt, Manifest | 110–170k | 1–1.5 h |
| 4 | Security framework: JWT, refresh tokens (in-memory store), permissions, `@RequirePermission`, `CurrentUser`, `TokenData`, auth endpoints | Claude Opus 5.5 | Master Prompt, Manifest | 140–220k | 1.5–2 h |
| 5 | Logging and audit: `AppLogger`, `LogCategory`, `LogEvent`, `LogContext`, `LogData`, request context filter, `AuditService` | Claude Sonnet 5.5 | Master Prompt | 80–130k | 1 h |
| 6 | Event framework: `DomainEvent`, `BaseEvent`, `EventPublisher`, `EventHandler` | Claude Sonnet 5.5 | Master Prompt | 40–70k | 0.5 h |
| 7 | Localization: `MessageKey`, `LocalizationService`, `en.yml`/`fa.yml`/`ko.yml`, exception and log translation | Claude Sonnet 5.5 | Master Prompt | 50–90k | 0.5–1 h |
| 8 | Cache: `CacheProvider`, `CacheService`, memory and Redis providers | Composer 2.5 | Master Prompt | 35–60k | 0.5 h |
| 9 | Storage: `FileStorage`, local, MinIO and S3 adapters | Composer 2.5 | Master Prompt | 40–70k | 0.5 h |
| 10 | Messaging and mail: `MessagePublisher`/`MessageConsumer`, Kafka and RabbitMQ adapters, `MailProvider`, SMTP adapter | Composer 2.5 | Master Prompt | 50–90k | 0.5–1 h |
| 11 | CRUD framework: `CrudController`, `BaseRepository` defaults, paging model | Claude Sonnet 5.5 | Master Prompt, Dev Guide | 60–100k | 0.5–1 h |
| 12 | Architecture enforcement: ArchUnit rules for folders, dependencies, inheritance, domain isolation | Claude Sonnet 5.5 | Master Prompt, Architecture | 50–90k | 0.5–1 h |
| 13 | UserManagement reference feature: every framework capability demonstrated, with tests | Claude Opus 5.5 | Master Prompt, Feature guide, Dev Guide | 140–220k | 1.5–2.5 h |
| 14 | Documentation and bootstrap: `README.md`, update existing docs only where implementation differs, `init-project.sh`/`.bat`, JavaDoc check | Composer 2.5 | Master Prompt, Dev Guide | 60–100k | 1 h |
| **Total** | | | | **~1.0–1.6M** | **~10–15 h** |

## Start commands

Send exactly this in a new chat, replacing `N`:

```text
Start Phase N. Follow docs/Development Plan.md.
```

---

# Handoff Log

Each phase appends: what was built, key types created, deviations from the plan, open items.

_(empty — Phase 1 not started)_
