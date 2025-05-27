package learn.mavendemo0315;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
public class Mavendemo0315Application {

    public static void main(String[] args) {
        SpringApplication.run(Mavendemo0315Application.class, args);
    }

    @Configuration
    static class AppConfig {
        @Bean
        public String demoBean() {
            return "demo";
        }
    }
}
