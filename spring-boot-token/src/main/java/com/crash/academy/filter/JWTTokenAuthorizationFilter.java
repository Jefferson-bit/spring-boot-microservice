package com.crash.academy.filter;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.text.ParseException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;

import com.crash.academy.token.convert.TokenConverter;
import com.crash.academy.util.SecurityContextUtil;
import com.crash.property.JWTConfiguration;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;

import lombok.SneakyThrows;

public class JWTTokenAuthorizationFilter extends OncePerRequestFilter{
	
	@Autowired
	protected JWTConfiguration jwtConfiguration;
	
	@Autowired
	protected TokenConverter tokenConverter;
	
	public JWTTokenAuthorizationFilter(JWTConfiguration jwtConfiguration, TokenConverter tokenConverter) {
		this.jwtConfiguration = jwtConfiguration;
		this.tokenConverter = tokenConverter;
	}

	public JWTTokenAuthorizationFilter() {
		super();
	}

	@Override
	@SneakyThrows
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		String header = request.getHeader(jwtConfiguration.getHeader().getName());
		
		
		if(header == null || !header.startsWith(jwtConfiguration.getHeader().getPrefix())) { 
			chain.doFilter(request, response);
			return;
		}
		String token = header.replace(jwtConfiguration.getHeader().getPrefix(), "".trim());
		SecurityContextUtil.setSecurityContext(StringUtils.equalsIgnoreCase("signed",jwtConfiguration.getType())? validate(token): decryptValidating(token));
		chain.doFilter(request, response);
	}

	private SignedJWT decryptValidating(String ecryptedToken) throws ParseException, JOSEException, AccessDeniedException {
		String signedToken = tokenConverter.decryptToken(ecryptedToken);
		tokenConverter.validateTokenSignature(signedToken);
		return SignedJWT.parse(signedToken);
	}
	
	private SignedJWT validate(String signedToken) throws AccessDeniedException, ParseException, JOSEException {
		tokenConverter.validateTokenSignature(signedToken);
		return SignedJWT.parse(signedToken);
	}
	
}