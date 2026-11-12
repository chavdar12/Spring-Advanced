package bg.softuni.docs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// springdoc-openapi replaces the Springfox/Swagger 2 setup from the lecture (Springfox does not support Spring Boot 3+).
// Swagger UI: /swagger-ui.html, OpenAPI JSON: /v3/api-docs
@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI coursesOpenApi() {
        return new OpenAPI().info(new Info()
                .title("Courses API")
                .description("Spring Advanced - Containerization & Documentation demo")
                .version("v1"));
    }
}
