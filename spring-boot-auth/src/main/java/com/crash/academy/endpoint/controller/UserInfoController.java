package com.crash.academy.endpoint.controller;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crash.domain.ApplicationUser;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value ="user")
@Api(value = "Endpoints to manage User")
public class UserInfoController {
	
	@SuppressWarnings("deprecation")
	@GetMapping(path = "info", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ApiOperation(value = "will retrieve the information from the user available in the token", response = ApplicationUser.class )
	public ResponseEntity<ApplicationUser> getUserInfo(Principal principal){		
		ApplicationUser applicationUser = (ApplicationUser)((UsernamePasswordAuthenticationToken)principal).getPrincipal();
		return new ResponseEntity<>(applicationUser, HttpStatus.OK);
	}
}
