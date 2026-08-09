package site.resam.basespringapi.infrastructure.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import site.resam.basespringapi.application.common.ApplicationConfiguration;
import site.resam.basespringapi.shared.time.SharedConfiguration;

@Configuration
@Import({SharedConfiguration.class, ApplicationConfiguration.class})
@ComponentScan(basePackages = "site.resam.basespringapi.infrastructure")
@EntityScan(basePackages = "site.resam.basespringapi.infrastructure.persistence.entity")
@EnableJpaRepositories(basePackages = "site.resam.basespringapi.infrastructure.persistence.repository")
public class InfrastructureConfiguration {
}
