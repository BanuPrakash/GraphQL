package com.adobe.graphql;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.dataloader.BatchLoaderEnvironment;
import org.dataloader.DataLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.graphql.execution.BatchLoaderRegistry;
import org.springframework.stereotype.Controller;

import com.adobe.dao.VetRepository;
import com.adobe.dao.VisitRepository;
import com.adobe.entity.Vet;
import com.adobe.entity.Visit;
import com.adobe.service.VisitService;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Data
class AddVisitInput {
	private int petId;
	private int vetId;
	private LocalDate date;
	String description;
}

@Slf4j
@Controller
public class VisitController {

	@Autowired
	VisitService visitService;

	@Autowired
	VetRepository vetRepository;

	@Autowired
	VisitRepository visitRepository;

	@Autowired
	VisitPublisher visitPublisher;


	final BatchLoaderRegistry batchLoaderRegistry;

	public VisitController(BatchLoaderRegistry batchLoaderRegistry) {
		this.batchLoaderRegistry = batchLoaderRegistry;
		batchLoaderRegistry.forTypePair(Integer.class, Vet.class)
				.registerBatchLoader((List<Integer> keys, BatchLoaderEnvironment env) -> {
					log.info("Loading vets with keys {}", keys);
					Flux<Vet> vetsWithIds = this.vetsWithIds(keys);
					return vetsWithIds;
				});
	}

	@QueryMapping
	public List<Visit> visits(@Argument Optional<Integer> petId) {
		log.info("Loading Visits from database");
		List<Visit> visits = petId.map(visitRepository::getVisits).orElseGet(visitRepository::findAll);
		return visits;
	}

	@SchemaMapping(typeName = "Visit")
	public CompletableFuture<Vet> treatingVet(Visit visit, DataLoader<Integer, Vet> dataLoader) {
		if (visit.getVet() == null) {
			return null;
		}

		log.info("Delegating loading of Vet with id {} from REST", visit.getVet().getId());
		return dataLoader.load(visit.getVet().getId());
	}

	public Mono<Vet> vetById(Integer id) {
		log.info("Start Http Request for Vet with id '{}'", id);
		Vet vet = vetRepository.findById(id).get();
		log.info("Finished Http Request for vet id '{}'", id);
		return Mono.just(vet);
	}

	public Flux<Vet> vetsWithIds(Collection<Integer> ids) {
		// return vetRepository.getMeVetsFor(ids); // use IN operator
		return Flux.fromStream(ids.stream()).flatMap(this::vetById);
	}

	@MutationMapping
	public Visit addVisit(@Argument AddVisitInput input) {
		Visit visit = visitService.addVisit(input.getPetId(), input.getVetId(), input.getDescription(),
				input.getDate());

		return visit;
	}

	@SubscriptionMapping
	public Flux<Visit> onNewVisit() {
		return visitPublisher.getPublisher();
	}
}
