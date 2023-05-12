package com.adobe.client;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.client.HttpGraphQlClient;

import com.adobe.entity.Pet;

@Configuration
public class PetClient {

	@Bean
	HttpGraphQlClient httpGraphQlClient() {
		return HttpGraphQlClient.builder().url("http://localhost:8080/graphql").build();
	}
	
	@Bean
	public CommandLineRunner commandLineRunner(HttpGraphQlClient client) {
		return (args) -> {
			String query = """
					query {
						  pets {
						    id
						    name
						    type {
						      name
						    }
						    visits {
						      description
						    }
						  }
						}
					""";
			List<Pet> pets = client.document(query)
					.retrieve("pets")
					.toEntityList(Pet.class).block();
			pets.forEach(pet -> {
				System.out.println(pet);
				System.out.println(pet.getType());
				System.out.println(pet.getVisits());
			});
		};
	}
}
