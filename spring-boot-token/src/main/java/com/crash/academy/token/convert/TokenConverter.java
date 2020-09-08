package com.crash.academy.token.convert;

import java.nio.file.AccessDeniedException;
import java.text.ParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crash.property.JWTConfiguration;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.SignedJWT;

@Service
public class TokenConverter {
	
	private static final Logger LOG = LoggerFactory.getLogger(TokenConverter.class);
	
	@Autowired
	private JWTConfiguration jwtConfiguration;
	
	public String decryptToken(String encryptedToken) throws ParseException, JOSEException {
		LOG.info("Decrypting token");
		JWEObject jweObject = JWEObject.parse(encryptedToken);
		DirectDecrypter directDecrypter = new DirectDecrypter(jwtConfiguration.getPrivateKey().getBytes());
		
		jweObject.decrypt(directDecrypter);
		LOG.info("Token decrypted, returning signed token");
		return jweObject.getPayload().toSignedJWT().serialize();
	}
	public void validateTokenSignature (String signedToken) throws ParseException, AccessDeniedException, JOSEException {
		LOG.info("Starting methods to validate token signature...");
		SignedJWT signedJWT = SignedJWT.parse(signedToken);
		LOG.info("Token parsed! retrieving public key from signed token");
		RSAKey publicKey = RSAKey.parse(signedJWT.getHeader().getJWK().toJSONObject());
		LOG.info("public key retrieved, validating signature...");
		if(!signedJWT.verify(new RSASSAVerifier(publicKey))) {
			throw new AccessDeniedException("invalid token signature");
		}
		LOG.info("The token has a valid signature");
	}
	
}








