package site.resam.basespringapi.api.rest.advice;

import java.net.URI;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import site.resam.basespringapi.shared.exception.ApplicationException;
import site.resam.basespringapi.shared.result.AppError;
import site.resam.basespringapi.shared.result.ErrorType;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApplicationException.class)
    public ProblemDetail handleApplicationException(ApplicationException ex) {
        AppError error = ex.getError();
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(toHttpStatus(error.getType()), error.getMessage());
        detail.setTitle(error.getType().name());
        detail.setType(URI.create("urn:basespringapi:error:" + error.getCode()));
        detail.setProperty("code", error.getCode());
        detail.setProperty("details", error.getDetails());
        return detail;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, message);
        detail.setTitle(ErrorType.VALIDATION.name());
        detail.setType(URI.create("urn:basespringapi:error:VALIDATION"));
        detail.setProperty("code", "VALIDATION");
        return detail;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleUnexpected(Exception ex) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Unexpected server error"
        );
        detail.setTitle(ErrorType.INTERNAL.name());
        detail.setType(URI.create("urn:basespringapi:error:UNEXPECTED"));
        detail.setProperty("code", "UNEXPECTED");
        return detail;
    }

    private static HttpStatus toHttpStatus(ErrorType type) {
        return switch (type) {
            case VALIDATION -> HttpStatus.BAD_REQUEST;
            case NOT_FOUND -> HttpStatus.NOT_FOUND;
            case CONFLICT -> HttpStatus.CONFLICT;
            case UNAUTHORIZED -> HttpStatus.UNAUTHORIZED;
            case FORBIDDEN -> HttpStatus.FORBIDDEN;
            case INTERNAL -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }
}
