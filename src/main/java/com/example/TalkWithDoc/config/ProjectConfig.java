package com.example.TalkWithDoc.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProjectConfig {
    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI()
                .info(
                        new Info()
                                .title("DocMind - AI Document Intelligence & RAG backend ")
                                .description("REST API for DocMind")
                                .version("1.0.0")
                                .contact(new Contact()
                                        .name("Substring Technologies")
                                        .email("suport@substringtechnolies.com")
                                        .url("https://www.substringtechnolies.com")
                                )
                );
    }
}

