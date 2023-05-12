package com.adobe.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.adobe.entity.PetType;

public interface PetTypeRepository extends JpaRepository<PetType, Integer> {

}
