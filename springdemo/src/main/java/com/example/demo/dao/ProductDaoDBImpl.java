package com.example.demo.dao;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;


@Profile("dev")
@Repository
public class ProductDaoDBImpl implements ProductDao {

	@Override
	public void addProduct() {
		// SQL code to insert product into database..
		System.out.println("stored in database...");
	}

}
