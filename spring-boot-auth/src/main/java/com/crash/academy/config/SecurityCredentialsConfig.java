package com.crash.academy.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.crash.academy.config.filter.JWTUserNameAndPasswordAuthenticationFilter;
import com.crash.academy.filter.JWTTokenAuthorizationFilter;
import com.crash.academy.token.TokenCreator;
import com.crash.academy.token.convert.TokenConverter;
import com.crash.property.JWTConfiguration;

@EnableWebSecurity
public class SecurityCredentialsConfig extends SecurityTokenConfig {

	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private TokenCreator tokenCreator;
	@Autowired
	private TokenConverter tokenConverter;
	
	public SecurityCredentialsConfig(JWTConfiguration jwtConfiguration, UserDetailsService userDetailsService,
			TokenCreator tokenCreator) {
		super(jwtConfiguration);
		this.userDetailsService = userDetailsService;
		this.tokenCreator = tokenCreator;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.addFilter(new JWTUserNameAndPasswordAuthenticationFilter(authenticationManager(), jwtConfiguration, tokenCreator))
			.addFilterAt(new JWTTokenAuthorizationFilter(jwtConfiguration, tokenConverter), UsernamePasswordAuthenticationFilter.class);
		super.configure(http);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());

	}

	@Bean
	BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
