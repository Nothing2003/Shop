package rj.com.store.config;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration

@SecurityScheme(
        name = "scheme",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
@OpenAPIDefinition(
        info = @Info(
                title = "Shop API",
                description = "This is developed by Soumojit Makar",
                version = "v1.0.0",
                contact = @Contact(
                        name = "Soumojit",
                        email = "soumojitmakar1234@gmail.com"
                ),
                license = @License(
                        name = "OPEN License"
                )
        )
)
public class SwaggerConfig {
//    @Bean
//    public OpenAPI getOpenAPI(){
//        String schemeName = "bearerScheme";
//        return new OpenAPI()
//                .addSecurityItem(new SecurityRequirement()
//                        .addList(schemeName)
//                )
//                .components(new Components().addSecuritySchemes(schemeName, new SecurityScheme().name(schemeName)
//                                .type(SecurityScheme.Type.HTTP)
//                                .bearerFormat("JWT")
//                                .scheme("bearer")
//                        )
//                )
//                .info(new Info().title("Shop API")
//                        .description("This is developed by Soumojit Makar")
//                        .version("v1.0.0")
//                        .contact(new Contact().name("Soumojit").email("soumojitmakar1234@gmail.com"))
//                        .license(new License().name("Apache 2.0"))
//                );
//    }
}
