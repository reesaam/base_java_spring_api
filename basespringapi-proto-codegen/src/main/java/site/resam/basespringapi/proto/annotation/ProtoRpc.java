package site.resam.basespringapi.proto.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a service method as a unary gRPC RPC.
 * Request type = first parameter type; response type = return type.
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ProtoRpc {

    /** RPC method name. Defaults to capitalized Java method name. */
    String name() default "";
}
