package rj.com.store.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI getOpenAPI(){
        String schemeName = "bearerScheme";
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement()
                        .addList(schemeName)
                )
                .components(new Components()
                        .addSecuritySchemes(schemeName, 
                                new io.swagger.v3.oas.models.security.SecurityScheme()
                                        .name(schemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .bearerFormat("JWT").scheme("bearer")
                        )
                )
                .info(new Info().title("Shop API")
                        .description("This is developed by Soumojit Makar")
                        .version("v1.0.0")
                        .contact(new Contact().name("Soumojit").email("soumojitmakar1234@gmail.com"))
                        .license(new License().name("Apache 2.0"))
                );
    }
}
