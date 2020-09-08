package com.crash.academy.token;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import com.crash.domain.ApplicationUser;
import com.crash.property.JWTConfiguration;
import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

@Service
public class TokenCreator {
	
	private static final Logger LOG = LoggerFactory.getLogger(TokenCreator.class);

	@Autowired
	private JWTConfiguration jwtConfiguration;
	
	public SignedJWT createSignedJWT(Authentication auth) throws NoSuchAlgorithmException, JOSEException {
		LOG.info("Starting to create the signed JWT");
		ApplicationUser applicationUser = (ApplicationUser) auth.getPrincipal();
		JWTClaimsSet claimsSet = createJWTClaims(auth, applicationUser);
		KeyPair rsakey = gereateKeyPair();
		LOG.info("Building JWK from the RSA keys");
		
		JWK jwk = new RSAKey.Builder((RSAPublicKey)rsakey.getPublic()).keyID(UUID.randomUUID().toString()).build();
		SignedJWT signedJWT = new SignedJWT(
				 new JWSHeader.Builder(JWSAlgorithm.RS256)
				.jwk(jwk)
				.type(JOSEObjectType.JWT)
				.build(), claimsSet);
				
		LOG.info("Signing the token with the private RSA Key");
		RSASSASigner signer = new RSASSASigner(rsakey.getPrivate());
		signedJWT.sign(signer);
		LOG.info("Serialized token '{}'", signedJWT.serialize());
		return signedJWT;
	}
	
	private KeyPair gereateKeyPair() throws NoSuchAlgorithmException {
		LOG.info("Generating RSA 2048 KEYS");
		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
		generator.initialize(2048);
		return generator.genKeyPair();	
	}	
	
	public String encryptToken(SignedJWT signedJWT) throws JOSEException {
		LOG.info("Starting the encryptToken method");
		DirectEncrypter directEncrypter = new DirectEncrypter(jwtConfiguration.getPrivateKey().getBytes());
		JWEObject jweObject = new JWEObject(new JWEHeader.Builder(JWEAlgorithm.DIR, EncryptionMethod.A128CBC_HS256)
				.contentType("JWT")
				.build(), new Payload(signedJWT));
		LOG.info("Encrypting token with system's private key");
		jweObject.encrypt(directEncrypter);
		LOG.info("token encrypted");
		return jweObject.serialize(); 
	}
	
	private JWTClaimsSet createJWTClaims(Authentication auth, ApplicationUser applicationUser) {
		LOG.info("Creating the jwtClaims Object for");
		return new JWTClaimsSet.Builder()
				.subject(applicationUser.getUsername())
				.claim("authorities", auth.getAuthorities()
						.stream()
						.map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
				.issuer("http://academy.devdojo")
				.claim("userId", applicationUser.getId())
				.issueTime(new Date())
				.expirationTime(new Date(System.currentTimeMillis() + (jwtConfiguration.getExpiration() * 1000)))
				.build();
	}
}