package site.resam.basespringapi.shared.exception;

import site.resam.basespringapi.shared.result.AppError;

public final class ValidationException extends ApplicationException {

    public ValidationException(String code, String message) {
        super(AppError.validation(code, message));
    }
}
