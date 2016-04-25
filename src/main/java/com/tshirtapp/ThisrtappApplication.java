package com.tshirtapp;

import com.tshirtapp.config.TshirtAppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@EnableConfigurationProperties(TshirtAppProperties.class)
@ComponentScan
@EnableAutoConfiguration
public class ThisrtappApplication {

    public static void main(String[] args) {
        SpringApplication.run(ThisrtappApplication.class, args);
    }
}
