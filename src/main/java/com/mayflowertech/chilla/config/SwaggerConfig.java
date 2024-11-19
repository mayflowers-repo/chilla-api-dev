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
    public Docket metadataApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Metadata API v1.0")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.mayflowertech.chilla.controllers"))
                .paths(PathSelectors.ant("/karuthal/api/v1/metadata/**"))
                .build()
                .globalOperationParameters(
                        Lists.newArrayList(new ParameterBuilder()
                                .name("Authorization")
                                .description("Access Token")
                                .modelRef(new ModelRef("string"))
                                .parameterType("header")
                                .required(true)  // Set to true if the authorization is required for all endpoints
                                .build()));
    }
    
    @Bean
    public Docket bookingRequestsApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Booking Requests API v1.0")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.mayflowertech.chilla.controllers"))
                .paths(PathSelectors.ant("/karuthal/api/v1/bookingrequest/**"))
                .build()
                .globalOperationParameters(
                        Lists.newArrayList(new ParameterBuilder()
                                .name("Authorization")
                                .description("Access Token")
                                .modelRef(new ModelRef("string"))
                                .parameterType("header")
                                .required(true)  // Set to true if the authorization is required for all endpoints
                                .build()));
    }
    
    @Bean
    public Docket personaApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Persona API v1.0")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.mayflowertech.chilla.controllers"))
                .paths(PathSelectors.ant("/karuthal/api/v1/persona/**"))
                .build()
                .globalOperationParameters(
                        Lists.newArrayList(new ParameterBuilder()
                                .name("Authorization")
                                .description("Access Token")
                                .modelRef(new ModelRef("string"))
                                .parameterType("header")
                                .required(true)  // Set to true if the authorization is required for all endpoints
                                .build()));
    }
    
    @Bean
    public Docket staffApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Staff API v1.0")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.mayflowertech.chilla.controllers"))
                .paths(PathSelectors.ant("/karuthal/api/v1/staff/**"))
                .build()
                .globalOperationParameters(
                        Lists.newArrayList(new ParameterBuilder()
                                .name("Authorization")
                                .description("Access Token")
                                .modelRef(new ModelRef("string"))
                                .parameterType("header")
                                .required(true)  // Set to true if the authorization is required for all endpoints
                                .build()));
    }

    
    @Bean
    public Docket roleRequestsApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Role Requests API v1.0")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.mayflowertech.chilla.controllers"))
                .paths(PathSelectors.ant("/karuthal/api/v1/rolerequest/**"))
                .build()
                .globalOperationParameters(
                        Lists.newArrayList(new ParameterBuilder()
                                .name("Authorization")
                                .description("Access Token")
                                .modelRef(new ModelRef("string"))
                                .parameterType("header")
                                .required(true)  // Set to true if the authorization is required for all endpoints
                                .build()));
    }
    
    @Bean
    public Docket workLogRequestsApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Work Log API v1.0")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.mayflowertech.chilla.controllers"))
                .paths(PathSelectors.ant("/karuthal/api/v1/worklog/**"))
                .build()
                .globalOperationParameters(
                        Lists.newArrayList(new ParameterBuilder()
                                .name("Authorization")
                                .description("Access Token")
                                .modelRef(new ModelRef("string"))
                                .parameterType("header")
                                .required(true)  // Set to true if the authorization is required for all endpoints
                                .build()));
    }

    @Bean
    public Docket userMgmtApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("User Management API v1.0")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.mayflowertech.chilla.controllers"))
                .paths(PathSelectors.ant("/karuthal/api/v1/usermanagement/**"))
                .build()
                .globalOperationParameters(
                        Lists.newArrayList(new ParameterBuilder()
                                .name("Authorization")
                                .description("Access Token")
                                .modelRef(new ModelRef("string"))
                                .parameterType("header")
                                .required(true)  // Set to true if the authorization is required for all endpoints
                                .build()));
    }
    
    @Bean
    public Docket documentMgmtApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Document Management API v1.0")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.mayflowertech.chilla.controllers"))
                .paths(PathSelectors.ant("/karuthal/api/v1/documents/**"))
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
    public Docket otpMgmtApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("OTP Management API v1.0")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.mayflowertech.chilla.controllers"))
                .paths(PathSelectors.ant("/karuthal/api/v1/email/**"))
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
    public SecurityConfiguration securityInfo() {
        return new SecurityConfiguration(null, null, null, null, "Bearer", ApiKeyVehicle.HEADER, HttpHeaders.AUTHORIZATION, "");
    }

    private ApiKey apiKey() {
        return new ApiKey("Authorization", "Bearer", "header");
    }
}
