package com.adobe.graphql;

import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureHttpGraphQlTester;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.graphql.test.tester.WebGraphQlTester;

import com.adobe.dao.PetRepository;
import com.adobe.entity.Pet;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureHttpGraphQlTester
public class PetControllerTest {
	@Autowired
	WebGraphQlTester tester;
	
	@MockBean
	PetRepository petRepository;
	
	@Test
	public void petsQuery_AllPets() {
		// mock the data
		List<Pet> pets = Arrays.asList(Pet.builder().id(1).name("A").build(), 
				Pet.builder().id(2).name("B").build());
		
		when(petRepository.findAll()).thenReturn(pets);
		
		tester.document("query { pets { id name }}").execute()
		.path("data.pets[*]").entityList(Object.class).hasSize(2)
		.path("data.pets[0].id").hasValue();
	}
	
	@Test
	public void petById() {
		when(petRepository.findById(1))
		.thenReturn(Optional.of(Pet.builder().id(1).name("Terry").build()));
		
		tester.document("query { pet(id:1) {id name}}").execute()
		.path("data.pet.id").entity(Integer.class).isEqualTo(1)
		.path("data.pet.name").entity(String.class).isEqualTo("Sprinkles");
	}
	
}
