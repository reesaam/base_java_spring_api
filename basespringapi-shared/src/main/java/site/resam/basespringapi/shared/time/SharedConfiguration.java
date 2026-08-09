package site.resam.basespringapi.shared.time;

import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SharedConfiguration {

    @Bean
    Clock clock() {
        return Clock.systemUTC();
    }
}
