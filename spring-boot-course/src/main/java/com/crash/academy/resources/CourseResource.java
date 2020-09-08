package com.crash.academy.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crash.academy.services.CourseService;
import com.crash.domain.Course;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "v1/admin/course")
@Api(value = "Endpoints to manage course")
public class CourseResource {
	
	@Autowired
	private CourseService courseService;
	
	@SuppressWarnings("deprecation")
	@GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ApiOperation(value = "List all available course", response = Course[].class )
	public ResponseEntity<Iterable<Course>> list(Pageable pageable) {
		return ResponseEntity.ok().body(courseService.list(pageable));
	}
	
}
