package com.crash.academy.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.crash.academy.filter.JWTTokenAuthorizationFilter;
import com.crash.academy.token.convert.TokenConverter;
import com.crash.property.JWTConfiguration;

@EnableWebSecurity
public class SecurityCredentialsConfig extends SecurityTokenConfig {

	@Autowired
	private TokenConverter tokenConverter;

	public SecurityCredentialsConfig(JWTConfiguration jwtConfiguration, 
			TokenConverter  tokenConverter) {
		super(jwtConfiguration);
		
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.addFilterAfter(new JWTTokenAuthorizationFilter(jwtConfiguration, tokenConverter),	UsernamePasswordAuthenticationFilter.class);
		super.configure(http);
	}


}
