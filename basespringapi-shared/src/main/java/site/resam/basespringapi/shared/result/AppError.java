package site.resam.basespringapi.shared.result;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

/**
 * Structured application error independent of transport (REST / gRPC).
 */
public final class AppError {

    private final String code;
    private final String message;
    private final ErrorType type;
    private final Map<String, String> details;

    private AppError(String code, String message, ErrorType type, Map<String, String> details) {
        this.code = Objects.requireNonNull(code, "code");
        this.message = Objects.requireNonNull(message, "message");
        this.type = Objects.requireNonNull(type, "type");
        this.details = details == null ? Map.of() : Collections.unmodifiableMap(details);
    }

    public static AppError of(String code, String message, ErrorType type) {
        return new AppError(code, message, type, Map.of());
    }

    public static AppError of(String code, String message, ErrorType type, Map<String, String> details) {
        return new AppError(code, message, type, details);
    }

    public static AppError notFound(String code, String message) {
        return of(code, message, ErrorType.NOT_FOUND);
    }

    public static AppError validation(String code, String message) {
        return of(code, message, ErrorType.VALIDATION);
    }

    public static AppError conflict(String code, String message) {
        return of(code, message, ErrorType.CONFLICT);
    }

    public static AppError unauthorized(String code, String message) {
        return of(code, message, ErrorType.UNAUTHORIZED);
    }

    public static AppError forbidden(String code, String message) {
        return of(code, message, ErrorType.FORBIDDEN);
    }

    public static AppError unexpected(String message) {
        return of("UNEXPECTED", message, ErrorType.INTERNAL);
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public ErrorType getType() {
        return type;
    }

    public Map<String, String> getDetails() {
        return details;
    }

    @Override
    public String toString() {
        return type + "[" + code + "]: " + message;
    }
}
