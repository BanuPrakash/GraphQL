package com.example.demo.dao;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Profile("prod")
@Repository
public class ProductDaoJpaImpl implements ProductDao {

	@Override
	public void addProduct() {
		// JPA code to save
		System.out.println("JPA save...");
	}

}
