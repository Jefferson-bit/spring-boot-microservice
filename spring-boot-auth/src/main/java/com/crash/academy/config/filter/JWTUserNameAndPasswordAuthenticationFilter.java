package com.crash.academy.config.filter;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.crash.academy.token.TokenCreator;
import com.crash.domain.ApplicationUser;
import com.crash.property.JWTConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;

public class JWTUserNameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private static final Logger LOG = LoggerFactory.getLogger(JWTUserNameAndPasswordAuthenticationFilter.class);
	
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JWTConfiguration jwtConfiguration;
	@Autowired
	private TokenCreator tokenCreator;

	public JWTUserNameAndPasswordAuthenticationFilter(AuthenticationManager authenticationManager,
			JWTConfiguration jwtConfiguration, TokenCreator tokenCreator) {
		setAuthenticationFailureHandler(new JWTAuthenticationFailureHandler());
		this.authenticationManager = authenticationManager;
		this.jwtConfiguration = jwtConfiguration;
		this.tokenCreator = tokenCreator;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, 
																			HttpServletResponse response)
																			throws AuthenticationException {
		try {
		LOG.info("Attemping authentication. . .");
		ApplicationUser applicationUser = new ObjectMapper().readValue(request.getInputStream(), ApplicationUser.class);
			if(applicationUser == null) {
				throw new UsernameNotFoundException("Unable to retrieve the usernames or password");
			}
		LOG.info("Creating the authentication object for the user '{}' and calling userDetailsServiceImpl", applicationUser.getUsername());
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(applicationUser.getUsername(), applicationUser.getPassword(), new ArrayList<>());
		authToken.setDetails(applicationUser);
		return authenticationManager.authenticate(authToken);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
																Authentication auth) throws IOException, ServletException {
		try {
		LOG.info("Authentication was sucessful for the user '{}' generating  JWE token", auth.getName());
		SignedJWT signedJWT = tokenCreator.createSignedJWT(auth);
		String encryptedToken = tokenCreator.encryptToken(signedJWT);
		LOG.info("Token generated sucessfully, adding it to the response header");
		response.addHeader("Access-Control-Expose-eaders", "XSRF-TOKEN, " +jwtConfiguration.getHeader().getName());
		response.addHeader(jwtConfiguration.getHeader().getName(), jwtConfiguration.getHeader().getPrefix() + encryptedToken);
		} catch (NoSuchAlgorithmException | JOSEException e) {
			e.printStackTrace();
		}
	}

	private class JWTAuthenticationFailureHandler implements AuthenticationFailureHandler {

		@Override
		public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
			response.setStatus(401);
			response.setContentType("application/json");
			response.getWriter().append(json());
		}

		private String json() {
			long date = new Date().getTime();
			return "{\"Timestamp\": " + date + ","
					+ "\"status\": 401, " 
					+ "\"error\": \"Não Autorizado\", "
					+ "\"message\": \"Usuario ou Senha inválidos\"," 
					+ "\"path\": \"/login\"}";
		}
	}
}






































