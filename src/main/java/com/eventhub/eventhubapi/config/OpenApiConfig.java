package com.eventhub.eventhubapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("EventHub API")
                        .version("0.0.1-SNAPSHOT")
                        .description("API RESTful para Sistema de Gestão de Eventos (EventHub).\n\n" +
                                     "Documentação gerada automaticamente via SpringDoc OpenAPI.")
                        .termsOfService("http://swagger.io/terms/")
                        .contact(new Contact()
                                .name("EventHub Team")
                                .url("http://eventhub.com.br")
                                .email("suporte@eventhub.com.br"))
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }
}
