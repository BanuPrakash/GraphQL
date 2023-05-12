package com.adobe.graphql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.adobe.dao.PetRepository;
import com.adobe.entity.Pet;

@Controller
public class PetController {
	@Autowired
	PetRepository petRepository;
	
	@QueryMapping
	public List<Pet> pets() {
		return petRepository.findAll();
	}
}
