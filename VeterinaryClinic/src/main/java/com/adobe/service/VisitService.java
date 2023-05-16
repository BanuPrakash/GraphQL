package com.adobe.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.adobe.dao.PetRepository;
import com.adobe.dao.VetRepository;
import com.adobe.dao.VisitRepository;
import com.adobe.entity.Pet;
import com.adobe.entity.Vet;
import com.adobe.entity.Visit;

import jakarta.transaction.Transactional;



@Service
public class VisitService {
	private final PetRepository petRepository;
	private final VetRepository vetRepository;
	private final VisitRepository visitRepository;
	
	@Autowired
	ApplicationEventPublisher applicationEventPublisher;

	
	public VisitService(PetRepository petRepository, VetRepository vetRepository, VisitRepository visitRepository) {
		this.petRepository = petRepository;
		this.vetRepository = vetRepository;
		this.visitRepository = visitRepository;
	}
	
	@Transactional
	public Visit addVisit(int petId, int vetId, String description, LocalDate date ) {
			Pet pet = petRepository.findById(petId).get();
			Vet vet = vetRepository.findById(vetId).get();
			Visit visit = new Visit();
			visit.setDescription(description);
			visit.setDate(date);
			visit.setPet(pet);
			visit.setVet(vet);
			visitRepository.save(visit);
			applicationEventPublisher.publishEvent(new VisitCreatedEvent(visit.getId()));

			return visit;
	}
}
