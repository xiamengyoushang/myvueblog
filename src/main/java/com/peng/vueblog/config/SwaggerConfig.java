package com.peng.vueblog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

@Configuration
@EnableSwagger2 // 开启Swagger2
public class SwaggerConfig {
    // 引入swagger2及swagger-ui
    // 访问：http://localhost:8081/swagger-ui.html

    // 配置了swagger的bean实例
    @Bean
    public Docket docket1(Environment environment){
        // 获取项目环境
        Profiles profiles = Profiles.of("dev","test");
        boolean flag = environment.acceptsProfiles(profiles);
        System.out.println("是否是开发环境---->"+flag);
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .groupName("Group 1")  // 配置多个分组，方便多人协助
                .enable(flag) // 是否启用
                .select()
                // 配置要扫描接口的方式, 基于包扫描
                .apis(RequestHandlerSelectors.basePackage("com.peng.vueblog.controller"))
                .build(); // 建造者模式
    }

    @Bean
    public Docket docket2(){
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Group 2");
    }

    // 配置Swagger信息=apiInfo
    private ApiInfo apiInfo(){
        Contact contact = new Contact("peng.lei", "peng.lei", "peng.lei@qq.com");
        return new ApiInfo(
                "我的SwaggerApi接口",
                "peng.lei",
                "1.0",
                "http://localhost:8080",
                contact,
                "Apache 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0",
                new ArrayList());
    }

}
