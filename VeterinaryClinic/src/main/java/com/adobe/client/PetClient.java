package com.adobe.client;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.http.HttpHeaders;

import com.adobe.entity.Pet;
import com.adobe.security.JwtTokenService;

@Configuration
public class PetClient {

	@Autowired
	private JwtTokenService tokenService;

	private void withManagerToken(HttpHeaders headers) {
		String token = tokenService.generateToken("banu", 
				List.of(() -> "MANAGER"),
				Instant.now().plus(1, ChronoUnit.HOURS));
		headers.setBearerAuth(token);
	}

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
			List<Pet> pets = client.mutate().headers(this::withManagerToken).build().document(query).retrieve("pets")
					.toEntityList(Pet.class).block();

			pets.forEach(pet -> {
				System.out.println(pet);
				System.out.println(pet.getType());
				System.out.println(pet.getVisits());
			});
		};
	}
}
