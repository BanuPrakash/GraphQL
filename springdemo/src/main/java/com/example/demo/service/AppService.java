package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.example.demo.dao.ProductDao;

@Service
public class AppService {
	
	@Autowired
	private ProductDao productDao;
	
	public void doTask() {
		this.productDao.addProduct();
	}
}
