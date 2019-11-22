package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


//@SpringBootApplication
@SpringBootApplication
public class FrameworkApplication {
    public static void main(String[] args){
        SpringApplication.run(FrameworkApplication.class);
    }
}
