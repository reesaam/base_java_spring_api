package site.resam.base_java_spring_api;

import org.springframework.boot.SpringApplication;

public class TestBaseJavaSpringApiApplication {

    public static void main(String[] args) {
        SpringApplication.from(BaseJavaSpringApiApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
