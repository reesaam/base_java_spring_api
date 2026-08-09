package site.resam.basespringapi.api.grpc;

import io.grpc.Status;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;
import site.resam.basespringapi.shared.exception.ApplicationException;
import site.resam.basespringapi.shared.result.AppError;

@GrpcAdvice
public class GrpcExceptionAdvice {

    @GrpcExceptionHandler(ApplicationException.class)
    public Status handleApplicationException(ApplicationException ex) {
        return toStatus(ex.getError()).withDescription(ex.getMessage()).withCause(ex);
    }

    @GrpcExceptionHandler(Exception.class)
    public Status handleUnexpected(Exception ex) {
        return Status.INTERNAL.withDescription("Unexpected server error").withCause(ex);
    }

    private static Status toStatus(AppError error) {
        return switch (error.getType()) {
            case VALIDATION -> Status.INVALID_ARGUMENT;
            case NOT_FOUND -> Status.NOT_FOUND;
            case CONFLICT -> Status.ALREADY_EXISTS;
            case UNAUTHORIZED -> Status.UNAUTHENTICATED;
            case FORBIDDEN -> Status.PERMISSION_DENIED;
            case INTERNAL -> Status.INTERNAL;
        };
    }
}
