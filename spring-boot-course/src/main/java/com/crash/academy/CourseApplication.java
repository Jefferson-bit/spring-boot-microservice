package com.crash.academy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.crash.property.JWTConfiguration;

@SpringBootApplication
@EntityScan({"com.crash.domain"})
@EnableJpaRepositories({"com.crash.repositories"})
@EnableConfigurationProperties(value = JWTConfiguration.class)
@ComponentScan("com.crash.academy")
public class CourseApplication {

	public static void main(String[] args) {
		SpringApplication.run(CourseApplication.class, args);
	}

}
