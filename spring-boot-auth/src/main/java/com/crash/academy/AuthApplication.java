package com.crash.academy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.crash.property.JWTConfiguration;

@SpringBootApplication
@EnableConfigurationProperties(value = JWTConfiguration.class)
@EntityScan({"com.crash.domain"})
@EnableJpaRepositories({"com.crash.repositories"})
@EnableEurekaClient	
@ComponentScan("com.crash.academy")

public class AuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthApplication.class, args);
	}
}
