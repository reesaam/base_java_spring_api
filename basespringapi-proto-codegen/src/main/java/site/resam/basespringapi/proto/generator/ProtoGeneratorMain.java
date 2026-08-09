package site.resam.basespringapi.proto.generator;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import site.resam.basespringapi.proto.model.ProtoSchema;

/**
 * CLI entry point invoked by Maven {@code exec-maven-plugin} during {@code generate-sources}.
 *
 * <pre>
 * --packages=com.example.app,com.example.shared
 * --protoPackage=com.example.v1
 * --javaPackage=com.example.grpc.v1
 * --outputDir=target/generated-sources/proto-from-java
 * --fileName=api.proto
 * </pre>
 */
public final class ProtoGeneratorMain {

    private ProtoGeneratorMain() {
    }

    public static void main(String[] args) throws Exception {
        Map<String, String> options = parseArgs(args);
        require(options, "packages");
        require(options, "protoPackage");
        require(options, "javaPackage");
        require(options, "outputDir");

        Set<String> packages = new LinkedHashSet<>(Arrays.asList(options.get("packages").split(",")));
        String protoPackage = options.get("protoPackage");
        String javaPackage = options.get("javaPackage");
        Path outputDir = Path.of(options.get("outputDir"));
        String fileName = options.getOrDefault("fileName", "generated.proto");

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        ProtoSchema schema = new ProtoSchemaBuilder(packages, protoPackage, javaPackage, classLoader).build();
        String content = new ProtoFileWriter().write(schema);

        Files.createDirectories(outputDir);
        Path outputFile = outputDir.resolve(fileName);
        Files.writeString(outputFile, content, StandardCharsets.UTF_8);

        System.out.printf(
                "Generated %s (%d messages, %d enums, %d services)%n",
                outputFile.toAbsolutePath(),
                schema.getMessages().size(),
                schema.getEnums().size(),
                schema.getServices().size()
        );
    }

    private static Map<String, String> parseArgs(String[] args) {
        Map<String, String> options = new LinkedHashMap<>();
        for (String arg : args) {
            if (!arg.startsWith("--") || !arg.contains("=")) {
                throw new IllegalArgumentException("Invalid argument: " + arg);
            }
            String body = arg.substring(2);
            int idx = body.indexOf('=');
            options.put(body.substring(0, idx), body.substring(idx + 1));
        }
        return options;
    }

    private static void require(Map<String, String> options, String key) {
        if (!options.containsKey(key) || options.get(key).isBlank()) {
            throw new IllegalArgumentException("Missing required argument --" + key);
        }
    }
}
