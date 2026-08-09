package site.resam.basespringapi;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("local")
class BaseSpringApiApplicationTests {

    @Test
    void contextLoads() {
        // Smoke test: application context, REST, gRPC, JPA and Flyway wire up.
    }
}
