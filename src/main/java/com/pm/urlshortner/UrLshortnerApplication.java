package com.pm.urlshortner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class UrLshortnerApplication {

    public static void main(String[] args) {
        SpringApplication.run(UrLshortnerApplication.class, args);
    }

}
