package com.example.CouponSystem;

import com.example.CouponSystem.login.LoginParameters;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.HashMap;
import java.util.function.Predicate;

@SpringBootApplication
public class CouponSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(CouponSystemApplication.class, args);
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .paths(Predicate.not(PathSelectors.regex("/error.*")))
                .build();
    }
    @Bean
    public HashMap <String, LoginParameters> sessions(){
        return new HashMap();
    }

}
