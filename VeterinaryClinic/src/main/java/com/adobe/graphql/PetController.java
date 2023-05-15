package com.adobe.graphql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import com.adobe.dao.PetRepository;
import com.adobe.dao.VisitRepository;
import com.adobe.entity.Pet;
import com.adobe.entity.Visit;

import graphql.schema.DataFetchingEnvironment;

@Controller
public class PetController {
	@Autowired
	PetRepository petRepository;
	@Autowired
	VisitRepository visitRepository;
	
	@QueryMapping
	public List<Pet> pets(DataFetchingEnvironment env) {
		System.out.println(env);
		return petRepository.findAll();
	}
	
	/*
	 {
		pet(id:1) {
		  name
		  type {
		    name
		  }
		}
	 }
	*/
	@QueryMapping
	public Pet pet(@Argument Integer id) {
		return petRepository.findById(id).get();
	}
	
	/*
		{
		  pets {
		    name
		    type {
		      name
		    }
		    visits {
		      description
		    }
		  }
		}
	*/
	// Field Resolver
	@SchemaMapping
	List<Visit> visits(Pet pet) {
		// can be MicroService call
		System.out.println("Pet " + pet.getId());
		return visitRepository.getVisits(pet.getId());
	}
	
//	@SchemaMapping
//	String name(Pet pet) {
//		return "Dummy";
//	}
}
