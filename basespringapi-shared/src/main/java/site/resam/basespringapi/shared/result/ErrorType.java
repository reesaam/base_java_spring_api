package site.resam.basespringapi.shared.result;

/**
 * Transport-agnostic error classification mapped to HTTP and gRPC status codes.
 */
public enum ErrorType {
    VALIDATION,
    NOT_FOUND,
    CONFLICT,
    UNAUTHORIZED,
    FORBIDDEN,
    INTERNAL
}
