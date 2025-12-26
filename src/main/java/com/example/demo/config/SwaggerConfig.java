package com.example.demo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        // As per PDF Section 10: Enable Swagger/OpenAPI for the Engine
        return new OpenAPI()
                .info(new Info()
                        .title("Digital Credential Verification Engine API")
                        .version("1.0")
                        .description("API documentation for the Academic Integrity Case Tracker"))
                .servers(List.of(
                        // Keeping your specific server environment URL
                        new Server().url("https://9191.408procr.amypo.ai/")
                ));
    }
}