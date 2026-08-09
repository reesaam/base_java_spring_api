package site.resam.basespringapi.proto.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks an application service interface as a gRPC service contract.
 * Methods annotated with {@link ProtoRpc} become RPC methods.
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ProtoService {

    /** Protobuf service name. Defaults to the interface simple name. */
    String name() default "";
}
