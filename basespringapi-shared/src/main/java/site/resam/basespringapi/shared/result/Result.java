package site.resam.basespringapi.shared.result;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * Strongly typed operation result used across application and API layers.
 */
public final class Result<T> {

    private final T value;
    private final AppError error;

    private Result(T value, AppError error) {
        this.value = value;
        this.error = error;
    }

    public static <T> Result<T> success(T value) {
        return new Result<>(Objects.requireNonNull(value, "value"), null);
    }

    public static <T> Result<T> failure(AppError error) {
        return new Result<>(null, Objects.requireNonNull(error, "error"));
    }

    public boolean isSuccess() {
        return error == null;
    }

    public boolean isFailure() {
        return error != null;
    }

    public T getValue() {
        if (isFailure()) {
            throw new IllegalStateException("Result is failure: " + error);
        }
        return value;
    }

    public AppError getError() {
        if (isSuccess()) {
            throw new IllegalStateException("Result is success");
        }
        return error;
    }

    public Optional<T> toOptional() {
        return Optional.ofNullable(value);
    }

    public <U> Result<U> map(Function<T, U> mapper) {
        if (isFailure()) {
            return Result.failure(error);
        }
        return Result.success(mapper.apply(value));
    }

    public <U> Result<U> flatMap(Function<T, Result<U>> mapper) {
        if (isFailure()) {
            return Result.failure(error);
        }
        return mapper.apply(value);
    }
}
