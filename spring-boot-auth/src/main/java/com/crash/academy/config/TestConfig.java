package com.crash.academy.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.crash.domain.ApplicationUser;
import com.crash.domain.Course;
import com.crash.repositories.ApplicationUserRepository;
import com.crash.repositories.CourseRepository;

@Configuration
@Profile(value = "test")
public class TestConfig implements CommandLineRunner{
	@Autowired
	private CourseRepository rep;
	@Autowired
	private ApplicationUserRepository userRepository;

	@Override
	public void run(String... args) throws Exception {
		Course course = new Course(null, "Spring boot development micro-services");
		ApplicationUser user1 = new ApplicationUser(null, "william", new BCryptPasswordEncoder().encode("devdojo"), "ADMIN");
		ApplicationUser user2 = new ApplicationUser(null, "crash", new BCryptPasswordEncoder().encode("123"), "USER");

		userRepository.saveAll(Arrays.asList(user1, user2));
		rep.save(course);
		
	}
}
