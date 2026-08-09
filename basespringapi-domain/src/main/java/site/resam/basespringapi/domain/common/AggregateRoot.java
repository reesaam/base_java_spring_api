package site.resam.basespringapi.domain.common;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Base aggregate root with identity and optimistic concurrency support.
 */
public abstract class AggregateRoot {

    private final UUID id;
    private long version;
    private Instant createdAt;
    private Instant updatedAt;

    protected AggregateRoot(UUID id) {
        this.id = Objects.requireNonNull(id, "id");
    }

    public UUID getId() {
        return id;
    }

    public long getVersion() {
        return version;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    protected void markCreated(Instant instant) {
        this.createdAt = instant;
        this.updatedAt = instant;
        this.version = 0L;
    }

    protected void markUpdated(Instant instant) {
        this.updatedAt = instant;
        this.version++;
    }

    protected void restoreAudit(long version, Instant createdAt, Instant updatedAt) {
        this.version = version;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
