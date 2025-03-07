package project.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


/**
 * Configuration class for Swagger documentation using Swagger 2.
 * This class is responsible for configuring Swagger to generate API documentation for the controllers
 * located in the specified base package.
 * It utilizes the {@link springfox.documentation.spring.web.plugins.Docket} class to define the behavior of the Swagger API documentation.
 */
@EnableSwagger2
@Configuration
public class SwaggerConfig {

  /**
   * Configures and returns a Docket bean for Swagger API documentation.
   * The Docket is configured to include API documentation for the controllers located in the specified base package.
   * @return A configured Docket bean for Swagger API documentation.
   */
  @Bean
  public Docket employeeApi()
  {
    return new Docket(DocumentationType.SWAGGER_2).select()
        .apis(RequestHandlerSelectors.basePackage("project.web.controllers"))
        .build();
  }
}
