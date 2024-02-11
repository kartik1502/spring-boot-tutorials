package com.example.springschedular;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpringSchedularApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringSchedularApplication.class, args);
    }

}
