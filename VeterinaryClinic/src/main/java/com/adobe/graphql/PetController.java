package com.adobe.graphql;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import com.adobe.dao.PetRepository;
import com.adobe.dao.VisitRepository;
import com.adobe.entity.Pet;
import com.adobe.entity.Visit;

import graphql.relay.Connection;
import graphql.relay.DefaultConnection;
import graphql.relay.DefaultEdge;
import graphql.relay.DefaultPageInfo;
import graphql.relay.Edge;
import graphql.schema.DataFetchingEnvironment;

@Controller
public class PetController {
	@Autowired
	PetRepository petRepository;
	@Autowired
	VisitRepository visitRepository;

	@Autowired
	CursorUtil cursorUtil;

	@QueryMapping
	public List<Pet> pets(DataFetchingEnvironment env) {
		System.out.println(env);
		return petRepository.findAll();
	}

	/*
	 * { pet(id:1) { name type { name } } }
	 */
//	@QueryMapping
//	public Pet pet(@Argument Integer id) {
//		return petRepository.findById(id).get();
//	}
	
	@QueryMapping
	public Pet pet(@Argument Integer id) throws EntityNotFoundException {
		Optional<Pet> pet = petRepository.findById(id);
		if (pet.isPresent()) {
			return pet.get();
		} else
			throw new EntityNotFoundException("Pet with id " + id + " doesn't exist!!!");
	}

	/*
	 * { pets { name type { name } visits { description } } }
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

	// pagination

	@QueryMapping
	public Connection<Pet> petPage(@Argument int first, @Argument("after") String cursor) {
		List<Edge<Pet>> edges = getPets(cursor).stream()
				.map(pet -> new DefaultEdge<>(pet, cursorUtil.createCursorWith(pet.getId()))).limit(first)
				.collect(Collectors.toList());
		var pageInfo = new DefaultPageInfo(cursorUtil.getFirstCursorFrom(edges), cursorUtil.getLastCursorFrom(edges),
				cursor != null, edges.size() >= first);
		return new DefaultConnection<>(edges, pageInfo);
	}

	public List<Pet> getPets(String cursor) {
		if (cursor == null) {
			return petRepository.findAll();
		}
		return petRepository.findAll().stream().dropWhile(pet -> pet.getId() != cursorUtil.decode(cursor))
				.collect(Collectors.toList());
	}
}
