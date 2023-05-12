package com.adobe.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.adobe.entity.Pet;

public interface PetRepository extends JpaRepository<Pet, Integer> {

}
