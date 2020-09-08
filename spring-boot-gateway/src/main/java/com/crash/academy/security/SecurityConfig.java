package com.crash.academy.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.crash.academy.config.SecurityTokenConfig;
import com.crash.academy.security.filter.GatewayJwtTokenAuthorizationFilter;
import com.crash.academy.token.convert.TokenConverter;
import com.crash.property.JWTConfiguration;

@EnableWebSecurity
public class SecurityConfig extends SecurityTokenConfig {

	@Autowired
	private TokenConverter tokenConverter;

	public SecurityConfig(JWTConfiguration jwtConfiguration, TokenConverter tokenConverter) {
		super(jwtConfiguration);
		this.tokenConverter = tokenConverter;
	}
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.addFilterAfter(new GatewayJwtTokenAuthorizationFilter(jwtConfiguration, tokenConverter), UsernamePasswordAuthenticationFilter.class);
		super.configure(http);
	}
}
