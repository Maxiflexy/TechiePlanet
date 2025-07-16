package com.studentscoringapp.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Student Scoring System API")
                        .description("API for managing student scores and generating reports")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Student Scoring System")
                                .email("admin@studentscoringapp.com")));
    }
}