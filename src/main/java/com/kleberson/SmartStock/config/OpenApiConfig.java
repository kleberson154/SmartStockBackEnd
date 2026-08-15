package com.kleberson.SmartStock.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI smartStockOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                    .title("Smart Stock API")
                    .description("API REST para gerenciamento de estoque")
                    .version("1.0.0")
                            .contact(new Contact()
                                    .name("Kleberson")
                                    .email("kleberson55@hotmail.com")))
                .externalDocs(new ExternalDocumentation()
                    .description("Repositório do projeto")
                    .url("https://github.com/kleberson154/SmartStockBackEnd"));
    }
}
