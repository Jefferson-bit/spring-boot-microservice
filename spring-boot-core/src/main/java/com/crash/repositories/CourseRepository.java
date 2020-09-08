package com.crash.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import com.crash.domain.Course;

public interface CourseRepository extends PagingAndSortingRepository<Course, Long>{

}
