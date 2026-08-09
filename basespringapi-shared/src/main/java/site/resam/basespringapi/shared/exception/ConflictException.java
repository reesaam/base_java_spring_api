package site.resam.basespringapi.shared.exception;

import site.resam.basespringapi.shared.result.AppError;

public final class ConflictException extends ApplicationException {

    public ConflictException(String code, String message) {
        super(AppError.conflict(code, message));
    }
}
