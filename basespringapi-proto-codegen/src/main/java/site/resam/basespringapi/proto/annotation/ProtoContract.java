package site.resam.basespringapi.proto.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a Java type (record/class) as a protobuf message source.
 * The build-time generator emits a corresponding {@code message} in .proto.
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ProtoContract {

    /** Protobuf message name. Defaults to the simple class name. */
    String name() default "";

    /** Optional proto package override for this message. */
    String protoPackage() default "";
}
