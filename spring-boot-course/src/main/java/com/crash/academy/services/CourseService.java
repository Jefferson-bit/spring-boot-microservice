package com.crash.academy.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.crash.domain.Course;
import com.crash.repositories.CourseRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CourseService {

	private static final Logger LOG = LoggerFactory.getLogger(CourseService.class);
	@Autowired
	private CourseRepository courseRepository;

	public Iterable<Course> list(Pageable pageable) {
		LOG.info("Listing all courses");
		return courseRepository.findAll(pageable);
	}
}
