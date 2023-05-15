package com.adobe.graphql;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import com.adobe.entity.Visit;
import com.adobe.service.VisitService;

import lombok.Data;

@Data
class AddVisitInput {
		private int petId;
		private int vetId;
		private LocalDate date;
		String description;
}

@Controller
public class VisitController {

	@Autowired
	VisitService visitService;
	
	@MutationMapping
	public Visit addVisit(@Argument AddVisitInput input) {
		Visit visit = visitService.addVisit(input.getPetId(), input.getVetId(),
				input.getDescription(), input.getDate());
		return visit;
	}
}
