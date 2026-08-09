package site.resam.basespringapi.shared.exception;

import site.resam.basespringapi.shared.result.AppError;

public final class NotFoundException extends ApplicationException {

    public NotFoundException(String code, String message) {
        super(AppError.notFound(code, message));
    }
}
