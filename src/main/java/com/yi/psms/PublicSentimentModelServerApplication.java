package com.yi.psms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class PublicSentimentModelServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(PublicSentimentModelServerApplication.class, args);
    }

}
