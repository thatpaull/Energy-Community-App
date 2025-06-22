package com.energy.community;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.energy.community")
@EnableJpaRepositories(basePackages = "com.energy.community")
@EntityScan(basePackages = "com.energy.community")
public class PercentageApplication {
    public static void main(String[] args) {
        SpringApplication.run(PercentageApplication.class, args);
    }
}