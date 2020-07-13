package az.ibar.payday.ms.stock.config;

import com.google.common.collect.ImmutableSet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Configuration for swagger - rest services documentation framework
 * Entry point url - http://localhost:8000/swagger-ui.html
 */
@Configuration
@EnableSwagger2
@Profile("!default")
public class SwaggerConfig {

    @Bean
    public Docket swaggerSettings() {
        return new Docket(DocumentationType.SWAGGER_2)
                .produces(ImmutableSet.of(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .select()
                .apis(RequestHandlerSelectors.basePackage("az.ibar.payday.ms.stock.controller"))
                .paths(PathSelectors.any())
                .build();
    }
}