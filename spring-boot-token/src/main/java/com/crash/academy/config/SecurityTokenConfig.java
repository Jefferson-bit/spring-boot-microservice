package com.crash.academy.config;

import java.util.Arrays;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;

import com.crash.property.JWTConfiguration;

public class SecurityTokenConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private Environment env;
	
	@Autowired
	protected JWTConfiguration jwtConfiguration;
	
	public SecurityTokenConfig(JWTConfiguration jwtConfiguration) {
		this.jwtConfiguration = jwtConfiguration;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		if (Arrays.asList(env.getActiveProfiles()).contains("test")) {
            http.headers().frameOptions().disable();
        }
			
		http.csrf().disable()
		.cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues())
		.and()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and()
			.exceptionHandling().authenticationEntryPoint((req,resp,e)-> resp.sendError(HttpServletResponse.SC_UNAUTHORIZED))
		.and()
		.authorizeRequests()
			.antMatchers(jwtConfiguration.getLoginUrl()).permitAll()
			.antMatchers(jwtConfiguration.getLoginUrl(), "/**/swagger-ui.html").permitAll()
			.antMatchers(HttpMethod.GET, "/**/swagger-resources/**","/**/webjars/springfox-swagger-ui/**","/**/v2/api-docs/**").permitAll()
			.antMatchers("/course/v1/admin/**").hasRole("ADMIN")
			.antMatchers("/auth/user/**").hasAnyRole("ADMIN", "USER")
			.anyRequest().authenticated();
	}	
}
