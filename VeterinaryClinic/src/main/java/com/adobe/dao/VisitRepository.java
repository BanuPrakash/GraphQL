package com.adobe.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.adobe.entity.Visit;

public interface VisitRepository extends JpaRepository<Visit, Integer> {
	
	@Query("from Visit where pet.id = :pid")
	//@Query(name="select * from visits where pet.id = :pid", nativeQuery = true)
	List<Visit> getVisits(@Param("pid") Integer petId);
}
