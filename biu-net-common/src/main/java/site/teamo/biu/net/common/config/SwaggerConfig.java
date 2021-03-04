package site.teamo.biu.net.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author 爱做梦的锤子
 * @create 2020/8/4
 */
@Configuration
@EnableOpenApi
public class SwaggerConfig {

    @Value("${biu-net.swagger.enable:false}")
    private boolean swaggerEnable;

    @Value("${biu-net.project.version}")
    private String version;

    @Value("${biu-net.swagger.base-package}")
    private String basePackage;

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .enable(swaggerEnable)
                .select()
                .apis(RequestHandlerSelectors.basePackage(basePackage))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Biu Net Proxy")
                .description("Biu Net Proxy")
                .termsOfServiceUrl("http://te-amo.site")
                .version(version)
                .build();
    }

}
