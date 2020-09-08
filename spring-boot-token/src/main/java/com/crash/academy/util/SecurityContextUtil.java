package com.crash.academy.util;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.crash.domain.ApplicationUser;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

public class SecurityContextUtil {
	
	private static final Logger LOG = LoggerFactory.getLogger(SecurityContextUtil.class);

	private SecurityContextUtil() {
	}
	
	public static void  setSecurityContext(SignedJWT signedJWT) {
		try {
			JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
			String username = claims.getSubject();
			if(username == null ) {
				throw new JOSEException("Username missing from JWT");
			}
			List<String> authorities = claims.getStringListClaim("authorities");
			ApplicationUser applicationUser = ApplicationUser
                    .builder()
                    .id(claims.getLongClaim("userId"))
                    .username(username)
                    .role(String.join(",", authorities))
                    .build();
			UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(applicationUser, null, createAuthorities(authorities));
			auth.setDetails(signedJWT.serialize());
			SecurityContextHolder.getContext().setAuthentication(auth);
		}catch(Exception e) {
			LOG.error("error setting security context ",e);
			SecurityContextHolder.clearContext();
		}
	}
	private static List<SimpleGrantedAuthority> createAuthorities(List<String> authorities) {
		return authorities.stream()
				.map(SimpleGrantedAuthority::new).collect(Collectors.toList());
	}
}	
