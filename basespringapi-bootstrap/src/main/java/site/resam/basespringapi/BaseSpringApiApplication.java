package site.resam.basespringapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import site.resam.basespringapi.api.config.ApiConfiguration;
import site.resam.basespringapi.infrastructure.config.InfrastructureConfiguration;

@SpringBootApplication
@Import({InfrastructureConfiguration.class, ApiConfiguration.class})
public class BaseSpringApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(BaseSpringApiApplication.class, args);
    }
}
