package site.resam.basespringapi.api.grpc;

import io.grpc.protobuf.services.ProtoReflectionService;
import net.devh.boot.grpc.server.serverfactory.GrpcServerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Enables gRPC reflection (grpcurl / grpcui) and documents gRPC-Web fronting.
 * Browser clients should talk gRPC-Web via the included Envoy proxy (see docker/envoy.yaml).
 */
@Configuration
public class GrpcServerConfiguration {

    @Bean
    GrpcServerConfigurer grpcServerConfigurer() {
        return serverBuilder -> serverBuilder.addService(ProtoReflectionService.newInstance());
    }
}
