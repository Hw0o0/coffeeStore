package com.coffeestore.coffeestore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class CoffeeStoreApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(CoffeeStoreApplication.class, args);
    }
    @Override // 기본 화면 home.html 지정
    public void addViewControllers(ViewControllerRegistry registry){
        registry.addViewController("/home");
    }
}
