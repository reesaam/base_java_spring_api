package site.resam.basespringapi.shared.exception;

import site.resam.basespringapi.shared.result.AppError;

/**
 * Unchecked exception wrapping {@link AppError} for imperative service boundaries.
 */
public class ApplicationException extends RuntimeException {

    private final AppError error;

    public ApplicationException(AppError error) {
        super(error.getMessage());
        this.error = error;
    }

    public ApplicationException(AppError error, Throwable cause) {
        super(error.getMessage(), cause);
        this.error = error;
    }

    public AppError getError() {
        return error;
    }
}
