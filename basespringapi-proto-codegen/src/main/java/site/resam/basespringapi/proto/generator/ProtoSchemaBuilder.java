package site.resam.basespringapi.proto.generator;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.RecordComponent;
import java.lang.reflect.Type;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import site.resam.basespringapi.proto.annotation.ProtoContract;
import site.resam.basespringapi.proto.annotation.ProtoEnum;
import site.resam.basespringapi.proto.annotation.ProtoField;
import site.resam.basespringapi.proto.annotation.ProtoRpc;
import site.resam.basespringapi.proto.annotation.ProtoService;
import site.resam.basespringapi.proto.model.ProtoSchema;
import site.resam.basespringapi.proto.model.ProtoSchema.EnumDef;
import site.resam.basespringapi.proto.model.ProtoSchema.EnumValueDef;
import site.resam.basespringapi.proto.model.ProtoSchema.FieldDef;
import site.resam.basespringapi.proto.model.ProtoSchema.MessageDef;
import site.resam.basespringapi.proto.model.ProtoSchema.RpcDef;
import site.resam.basespringapi.proto.model.ProtoSchema.ServiceDef;

/**
 * Builds an in-memory protobuf schema from annotated Java types on the classpath.
 */
public final class ProtoSchemaBuilder {

    private final Set<String> scanPackages;
    private final String protoPackage;
    private final String javaPackage;
    private final ClassLoader classLoader;

    public ProtoSchemaBuilder(
            Set<String> scanPackages,
            String protoPackage,
            String javaPackage,
            ClassLoader classLoader
    ) {
        this.scanPackages = Set.copyOf(scanPackages);
        this.protoPackage = protoPackage;
        this.javaPackage = javaPackage;
        this.classLoader = classLoader;
    }

    public ProtoSchema build() {
        ProtoSchema schema = new ProtoSchema(protoPackage, javaPackage);
        Set<Class<?>> contracts = new LinkedHashSet<>();
        Set<Class<?>> enums = new LinkedHashSet<>();
        Set<Class<?>> services = new LinkedHashSet<>();

        try (io.github.classgraph.ScanResult scan = new io.github.classgraph.ClassGraph()
                .enableAnnotationInfo()
                .enableClassInfo()
                .enableMethodInfo()
                .ignoreClassVisibility()
                .overrideClassLoaders(classLoader)
                .acceptPackages(scanPackages.toArray(String[]::new))
                .scan()) {

            scan.getClassesWithAnnotation(ProtoContract.class.getName())
                    .forEach(ci -> contracts.add(ci.loadClass()));
            scan.getClassesWithAnnotation(ProtoEnum.class.getName())
                    .forEach(ci -> enums.add(ci.loadClass()));
            scan.getClassesWithAnnotation(ProtoService.class.getName())
                    .forEach(ci -> services.add(ci.loadClass()));
        }

        for (Class<?> enumType : enums) {
            schema.putEnum(toEnumDef(enumType));
        }
        for (Class<?> contract : contracts) {
            schema.putMessage(toMessageDef(contract, schema));
        }
        for (Class<?> service : services) {
            schema.putService(toServiceDef(service, schema));
        }
        return schema;
    }

    private EnumDef toEnumDef(Class<?> enumType) {
        ProtoEnum annotation = enumType.getAnnotation(ProtoEnum.class);
        String name = annotation.name().isBlank() ? enumType.getSimpleName() : annotation.name();
        List<EnumValueDef> values = new ArrayList<>();
        values.add(new EnumValueDef(toSnakeCase(name).toUpperCase() + "_UNSPECIFIED", 0));
        Object[] constants = enumType.getEnumConstants();
        int number = 1;
        for (Object constant : constants) {
            values.add(new EnumValueDef(((Enum<?>) constant).name(), number++));
        }
        return new EnumDef(name, values);
    }

    private MessageDef toMessageDef(Class<?> type, ProtoSchema schema) {
        ProtoContract annotation = type.getAnnotation(ProtoContract.class);
        String name = annotation.name().isBlank() ? type.getSimpleName() : annotation.name();
        List<FieldDef> fields = new ArrayList<>();

        if (type.isRecord()) {
            for (RecordComponent component : type.getRecordComponents()) {
                ProtoField protoField = component.getAnnotation(ProtoField.class);
                if (protoField == null) {
                    throw new IllegalStateException(
                            "Record component " + type.getName() + "#" + component.getName()
                                    + " requires @ProtoField(number=...)"
                    );
                }
                fields.add(toFieldDef(component.getName(), component.getGenericType(), protoField, schema));
            }
        } else {
            List<Field> declared = Arrays.stream(type.getDeclaredFields())
                    .filter(f -> f.isAnnotationPresent(ProtoField.class))
                    .sorted(Comparator.comparingInt(f -> f.getAnnotation(ProtoField.class).number()))
                    .toList();
            for (Field field : declared) {
                ProtoField protoField = field.getAnnotation(ProtoField.class);
                fields.add(toFieldDef(field.getName(), field.getGenericType(), protoField, schema));
            }
        }

        fields.sort(Comparator.comparingInt(FieldDef::number));
        validateUniqueFieldNumbers(type, fields);
        return new MessageDef(name, fields);
    }

    private FieldDef toFieldDef(String javaName, Type javaType, ProtoField protoField, ProtoSchema schema) {
        String fieldName = protoField.name().isBlank() ? toSnakeCase(javaName) : protoField.name();
        ResolvedType resolved = resolveType(javaType);
        boolean repeated = protoField.repeated() || resolved.repeated();
        String protoType = mapType(resolved.elementType(), schema);
        return new FieldDef(fieldName, protoType, protoField.number(), repeated);
    }

    private record ResolvedType(Class<?> elementType, boolean repeated) {
    }

    private static ResolvedType resolveType(Type type) {
        if (type instanceof Class<?> clazz) {
            if (clazz.isArray()) {
                return new ResolvedType(clazz.getComponentType(), true);
            }
            return new ResolvedType(clazz, false);
        }
        if (type instanceof ParameterizedType parameterizedType) {
            Type raw = parameterizedType.getRawType();
            if (raw instanceof Class<?> rawClass && Collection.class.isAssignableFrom(rawClass)) {
                Type arg = parameterizedType.getActualTypeArguments()[0];
                if (arg instanceof Class<?> elementClass) {
                    return new ResolvedType(elementClass, true);
                }
            }
        }
        throw new IllegalStateException("Unsupported generic type for proto field: " + type.getTypeName());
    }

    private ServiceDef toServiceDef(Class<?> serviceType, ProtoSchema schema) {
        ProtoService annotation = serviceType.getAnnotation(ProtoService.class);
        String name = annotation.name().isBlank() ? serviceType.getSimpleName() : annotation.name();
        List<RpcDef> rpcs = new ArrayList<>();

        for (Method method : serviceType.getDeclaredMethods()) {
            ProtoRpc rpc = method.getAnnotation(ProtoRpc.class);
            if (rpc == null) {
                continue;
            }
            if (method.getParameterCount() != 1) {
                throw new IllegalStateException(
                        "@ProtoRpc methods must have exactly one request parameter: "
                                + serviceType.getName() + "#" + method.getName()
                );
            }
            Parameter requestParam = method.getParameters()[0];
            Class<?> requestType = requestParam.getType();
            Class<?> responseType = method.getReturnType();
            ensureMessageRegistered(requestType, schema);
            ensureMessageRegistered(responseType, schema);

            String rpcName = rpc.name().isBlank() ? capitalize(method.getName()) : rpc.name();
            rpcs.add(new RpcDef(
                    rpcName,
                    resolveMessageName(requestType),
                    resolveMessageName(responseType)
            ));
        }

        if (rpcs.isEmpty()) {
            throw new IllegalStateException("Service " + serviceType.getName() + " has no @ProtoRpc methods");
        }
        rpcs.sort(Comparator.comparing(RpcDef::name));
        return new ServiceDef(name, rpcs);
    }

    private void ensureMessageRegistered(Class<?> type, ProtoSchema schema) {
        if (type.isAnnotationPresent(ProtoContract.class)) {
            schema.putMessage(toMessageDef(type, schema));
            return;
        }
        throw new IllegalStateException(
                "RPC request/response type must be annotated with @ProtoContract: " + type.getName()
        );
    }

    private String resolveMessageName(Class<?> type) {
        ProtoContract annotation = type.getAnnotation(ProtoContract.class);
        return annotation.name().isBlank() ? type.getSimpleName() : annotation.name();
    }

    private String mapType(Class<?> type, ProtoSchema schema) {
        if (type == String.class || type == UUID.class || type == Instant.class
                || type == LocalDate.class || type == LocalDateTime.class
                || type == CharSequence.class) {
            return "string";
        }
        if (type == int.class || type == Integer.class || type == short.class || type == Short.class) {
            return "int32";
        }
        if (type == long.class || type == Long.class) {
            return "int64";
        }
        if (type == boolean.class || type == Boolean.class) {
            return "bool";
        }
        if (type == float.class || type == Float.class) {
            return "float";
        }
        if (type == double.class || type == Double.class) {
            return "double";
        }
        if (type == byte[].class) {
            return "bytes";
        }
        if (type.isEnum()) {
            if (type.isAnnotationPresent(ProtoEnum.class)) {
                schema.putEnum(toEnumDef(type));
                ProtoEnum annotation = type.getAnnotation(ProtoEnum.class);
                return annotation.name().isBlank() ? type.getSimpleName() : annotation.name();
            }
            throw new IllegalStateException("Enum used in proto contract must be @ProtoEnum: " + type.getName());
        }
        if (type.isAnnotationPresent(ProtoContract.class)) {
            schema.putMessage(toMessageDef(type, schema));
            return resolveMessageName(type);
        }
        throw new IllegalStateException("Unsupported proto field type: " + type.getName());
    }

    private static void validateUniqueFieldNumbers(Class<?> type, List<FieldDef> fields) {
        Set<Integer> seen = new HashSet<>();
        for (FieldDef field : fields) {
            if (!seen.add(field.number())) {
                throw new IllegalStateException(
                        "Duplicate @ProtoField number " + field.number() + " in " + type.getName()
                );
            }
        }
    }

    static String toSnakeCase(String input) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (Character.isUpperCase(c)) {
                if (i > 0) {
                    sb.append('_');
                }
                sb.append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    private static String capitalize(String value) {
        if (value == null || value.isBlank()) {
            return value;
        }
        return Character.toUpperCase(value.charAt(0)) + value.substring(1);
    }
}
