package com.tshirtapp.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Date;

import static springfox.documentation.builders.PathSelectors.regex;


@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    private final Logger log = LoggerFactory.getLogger(SwaggerConfiguration.class);

    public static final String DEFAULT_INCLUDE_PATTERN = "/api/.*";
    private RelaxedPropertyResolver propertyResolver;


    @Bean
    public Docket swaggerSpringfoxDocket(TshirtAppProperties tshirtAppProperties) {
        log.debug("Starting Swagger");
        StopWatch watch = new StopWatch();
        watch.start();
        ApiInfo apiInfo = new ApiInfo(
                tshirtAppProperties.getSwagger().getTitle(),
                tshirtAppProperties.getSwagger().getDescription(),
                tshirtAppProperties.getSwagger().getVersion(),
                tshirtAppProperties.getSwagger().getTermsOfServiceUrl(),
                tshirtAppProperties.getSwagger().getContact(),
                tshirtAppProperties.getSwagger().getLicense(),
                tshirtAppProperties.getSwagger().getLicenseUrl());
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo)
                .genericModelSubstitutes(ResponseEntity.class)
                .forCodeGeneration(true)
                .genericModelSubstitutes(ResponseEntity.class)
                .directModelSubstitute(java.time.LocalDate.class, String.class)
                .directModelSubstitute(java.time.ZonedDateTime.class, Date.class)
                .directModelSubstitute(java.time.LocalDateTime.class, Date.class)
                .select()
                .paths(regex(DEFAULT_INCLUDE_PATTERN))
                .build();
        watch.stop();
        log.debug("Started Swagger in {} ms", watch.getTotalTimeMillis());
        return docket;
    }

}
