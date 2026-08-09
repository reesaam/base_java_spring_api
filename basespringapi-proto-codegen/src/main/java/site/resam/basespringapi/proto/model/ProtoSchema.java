package site.resam.basespringapi.proto.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class ProtoSchema {

    private final String protoPackage;
    private final String javaPackage;
    private final Map<String, MessageDef> messages = new LinkedHashMap<>();
    private final Map<String, EnumDef> enums = new LinkedHashMap<>();
    private final Map<String, ServiceDef> services = new LinkedHashMap<>();

    public ProtoSchema(String protoPackage, String javaPackage) {
        this.protoPackage = Objects.requireNonNull(protoPackage);
        this.javaPackage = Objects.requireNonNull(javaPackage);
    }

    public String getProtoPackage() {
        return protoPackage;
    }

    public String getJavaPackage() {
        return javaPackage;
    }

    public Map<String, MessageDef> getMessages() {
        return messages;
    }

    public Map<String, EnumDef> getEnums() {
        return enums;
    }

    public Map<String, ServiceDef> getServices() {
        return services;
    }

    public void putMessage(MessageDef message) {
        messages.putIfAbsent(message.name(), message);
    }

    public void putEnum(EnumDef enumDef) {
        enums.putIfAbsent(enumDef.name(), enumDef);
    }

    public void putService(ServiceDef service) {
        services.putIfAbsent(service.name(), service);
    }

    public record FieldDef(String name, String type, int number, boolean repeated) {
    }

    public record MessageDef(String name, List<FieldDef> fields) {
        public MessageDef {
            fields = List.copyOf(fields);
        }
    }

    public record EnumValueDef(String name, int number) {
    }

    public record EnumDef(String name, List<EnumValueDef> values) {
        public EnumDef {
            values = List.copyOf(values);
        }
    }

    public record RpcDef(String name, String requestType, String responseType) {
    }

    public record ServiceDef(String name, List<RpcDef> rpcs) {
        public ServiceDef {
            rpcs = new ArrayList<>(rpcs);
        }
    }
}
