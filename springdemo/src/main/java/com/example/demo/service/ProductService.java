package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dao.ProductRepo;
import com.example.demo.entity.Product;

@Service
public class ProductService {
	@Autowired
	private ProductRepo productRepo; // interface 
	
	public Product saveProduct(Product p) {
		return productRepo.save(p);
	}
	
	public List<Product> getProducts() {
		return productRepo.findAll();
	}
	
	public Product getProductById(int id) {
		return productRepo.findById(id).get();
	}
}
