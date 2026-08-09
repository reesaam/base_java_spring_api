package site.resam.basespringapi.domain.identity;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
import site.resam.basespringapi.domain.common.AggregateRoot;

/**
 * Sample identity aggregate demonstrating the reusable domain pattern.
 */
public final class User extends AggregateRoot {

    private String email;
    private String displayName;
    private UserStatus status;

    private User(UUID id, String email, String displayName, UserStatus status) {
        super(id);
        this.email = Objects.requireNonNull(email, "email");
        this.displayName = Objects.requireNonNull(displayName, "displayName");
        this.status = Objects.requireNonNull(status, "status");
    }

    public static User create(String email, String displayName, Instant now) {
        User user = new User(UUID.randomUUID(), normalizeEmail(email), displayName.trim(), UserStatus.ACTIVE);
        user.markCreated(now);
        return user;
    }

    public static User rehydrate(
            UUID id,
            String email,
            String displayName,
            UserStatus status,
            long version,
            Instant createdAt,
            Instant updatedAt
    ) {
        User user = new User(id, email, displayName, status);
        user.restoreAudit(version, createdAt, updatedAt);
        return user;
    }

    public void rename(String newDisplayName, Instant now) {
        this.displayName = Objects.requireNonNull(newDisplayName, "displayName").trim();
        markUpdated(now);
    }

    public void changeStatus(UserStatus newStatus, Instant now) {
        this.status = Objects.requireNonNull(newStatus, "status");
        markUpdated(now);
    }

    public String getEmail() {
        return email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public UserStatus getStatus() {
        return status;
    }

    private static String normalizeEmail(String email) {
        return Objects.requireNonNull(email, "email").trim().toLowerCase();
    }
}
