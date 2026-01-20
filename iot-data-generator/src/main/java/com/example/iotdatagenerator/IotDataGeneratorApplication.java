package com.example.iotdatagenerator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.example.common")
@EntityScan(basePackages = "com.example.common")
public class IotDataGeneratorApplication {

    public static void main(String[] args) {
        SpringApplication.run(IotDataGeneratorApplication.class, args);
    }

}
