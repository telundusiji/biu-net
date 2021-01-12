package site.teamo.biu.net.web.config;

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
 * @create 2020/11/13
 */

@Configuration
@EnableOpenApi
public class Swagger3Config {

    @Value("${swagger.enable}")
    private boolean swaggerEnable;

    @Value("${lemmer.project.version}")
    private String version;

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .enable(swaggerEnable)
                .select()
                .apis(RequestHandlerSelectors.basePackage("site.teamo.biu.net.web.api"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Lemmer Blog Platform")
                .description("lemmer blog platform")
                .termsOfServiceUrl("http://do-vis.site")
                .version(version)
                .build();
    }

}
