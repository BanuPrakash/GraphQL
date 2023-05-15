package com.adobe.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.adobe.entity.Owner;

public interface OwnerRepository extends JpaRepository<Owner, Integer> {

}
