package site.resam.basespringapi.proto.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Declares protobuf field metadata for a DTO property / record component.
 */
@Documented
@Target({ElementType.FIELD, ElementType.RECORD_COMPONENT, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ProtoField {

    /** Stable protobuf field number (required for wire compatibility). */
    int number();

    /** Protobuf field name. Defaults to the Java property name in snake_case. */
    String name() default "";

    /** When true, emits {@code repeated}. */
    boolean repeated() default false;
}
