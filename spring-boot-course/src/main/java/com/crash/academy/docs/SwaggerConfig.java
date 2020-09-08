package com.crash.academy.docs;

import org.springframework.context.annotation.Configuration;

import com.crash.domain.docs.BaseSwaggerConfig;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig extends BaseSwaggerConfig{	

	public SwaggerConfig() {
		super("com.crash.academy.resources");
	}

}
