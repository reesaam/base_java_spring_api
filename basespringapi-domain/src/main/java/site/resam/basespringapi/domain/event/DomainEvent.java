package site.resam.basespringapi.domain.event;

import java.time.Instant;
import java.util.UUID;

/**
 * Marker for domain events emitted by aggregates.
 */
public interface DomainEvent {

    UUID eventId();

    Instant occurredAt();

    String eventType();
}
