	package com.crash.repositories;


import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import com.crash.domain.ApplicationUser;

public interface ApplicationUserRepository extends PagingAndSortingRepository<ApplicationUser, Long>{
	
	@Transactional(readOnly = true)
	ApplicationUser findByUsername(String username);	
}
