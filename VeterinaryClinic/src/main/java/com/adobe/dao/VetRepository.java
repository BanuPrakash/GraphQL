package com.adobe.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.adobe.entity.Vet;

public interface VetRepository extends JpaRepository<Vet, Integer> {

}