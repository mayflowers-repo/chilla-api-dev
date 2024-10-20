package com.mayflowertech.chilla.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.google.common.collect.Lists;
import com.google.common.net.HttpHeaders;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    
    @Bean
    public Docket SystemConfigurationApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("System Configuration API v1.0")
//                .apiInfo(metaData())
                .select()
                .paths(PathSelectors.ant("/teamweb/api/v1/systemmanagement/**"))
                .build()
                .globalOperationParameters(
                        Lists.newArrayList(new ParameterBuilder()
                                .name("Authorization")
                                .description("Access Token")
                                .modelRef(new ModelRef("string"))
                                .parameterType("header")
                                .required(true)
                                .build()));
    }

    @Bean
    public Docket WorkflowManagementApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Workflow Management API v1.0")
//                .apiInfo(metaData())
                .select()
                .paths(PathSelectors.ant("/teamweb/api/v1/workflowmgmt/**"))
                .build()
                .globalOperationParameters(
                        Lists.newArrayList(new ParameterBuilder()
                                .name("Authorization")
                                .description("Access Token")
                                .modelRef(new ModelRef("string"))
                                .parameterType("header")
                                .required(false)
                                .build()));
    }

    
    @Bean
    public Docket ClientContactManagement() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Client contact Management API v1.0")
//                .apiInfo(metaData())
                .select()
                .paths(PathSelectors.ant("/teamweb/api/v1/clientcontactmanagement/**"))
                .build()
                .globalOperationParameters(
                        Lists.newArrayList(new ParameterBuilder()
                                .name("Authorization")
                                .description("Access Token")
                                .modelRef(new ModelRef("string"))
                                .parameterType("header")
                                .required(false)
                                .build()));
    }
  
    
    @Bean
    public Docket UserManagementApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("User Management API v1.0")
//                .apiInfo(metaData())
                .select()
                .paths(PathSelectors.ant("/teamweb/api/v1/usermanagement/**"))
                .build()
                .globalOperationParameters(
                        Lists.newArrayList(new ParameterBuilder()
                                .name("Authorization")
                                .description("Access Token")
                                .modelRef(new ModelRef("string"))
                                .parameterType("header")
                                .required(false)
                                .build()));
    }
    
    
    @Bean
    public Docket ExecutionManagementApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Workflow Execution API v1.0")
//                .apiInfo(metaData())
                .select()
                .paths(PathSelectors.ant("/teamweb/api/v1/executewk/**"))
                .build()
                .globalOperationParameters(
                        Lists.newArrayList(new ParameterBuilder()
                                .name("Authorization")
                                .description("Access Token")
                                .modelRef(new ModelRef("string"))
                                .parameterType("header")
                                .required(false)
                                .build()));
    }
    
    @Bean
    public Docket TinyUrlManagementApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Tiny URL Execution API v1.0")
//                .apiInfo(metaData())
                .select()
                .paths(PathSelectors.ant("/ec/**"))
                .build()
                .globalOperationParameters(
                        Lists.newArrayList(new ParameterBuilder()
                                .name("Authorization")
                                .description("Access Token")
                                .modelRef(new ModelRef("string"))
                                .parameterType("header")
                                .required(false)
                                .build()));
    }
    
    @Bean
    public Docket ScreenManagementApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Screen Management API v1.0")
//                .apiInfo(metaData())
                .select()
                .paths(PathSelectors.ant("/teamweb/api/v1/screenmgmt/**"))
                .build()
                .globalOperationParameters(
                        Lists.newArrayList(new ParameterBuilder()
                                .name("Authorization")
                                .description("Access Token")
                                .modelRef(new ModelRef("string"))
                                .parameterType("header")
                                .required(false)
                                .build()));
    }
    

    
 /*
    @Bean
    public Docket apiDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(metadata())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.mayflowertech.easycampaigns"))
                .build();
    }
    */
    

    
//    private ApiInfo metaData() {
//        ApiInfo apiInfo = new ApiInfo(
//                "Inflow API for Workflow Management System",
//                "REST API for Inflow - Workflow Management System",
//                "1.0",
//                "Terms of service",
//                new Contact("Inginim", "http://www.inginim.com", "support@inginim.com"),
//               "Apache License Version 2.0",
//                "https://www.apache.org/licenses/LICENSE-2.0");
//
//        return apiInfo;
//
//    }
    
    @Bean
    public SecurityConfiguration securityInfo() {
        return new SecurityConfiguration(null, null, null, null, "Bearer", ApiKeyVehicle.HEADER, HttpHeaders.AUTHORIZATION, "");
    }

    private ApiKey apiKey() {
        //return new ApiKey("Authorization", "token", "header");
        return new ApiKey("Authorization", "Bearer", "header");
    }

}
