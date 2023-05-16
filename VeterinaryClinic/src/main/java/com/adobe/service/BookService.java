package com.adobe.service;

import java.util.Collection;
import java.util.HashMap;

import org.springframework.stereotype.Service;

import com.adobe.dto.Book;

@Service
public class BookService {
	private final HashMap<Integer, Book> bookRepository = new HashMap<>();

	public BookService() {
		bookRepository.put(1, new Book(1, "B1", "A1", "Pub 1", 16.14));
		bookRepository.put(2, new Book(2, "B2", "A2", "Pub 2", 15.80));
		bookRepository.put(3, new Book(3, "B3", "A3", "Pub 1", 18.75));
		bookRepository.put(4, new Book(4, "B4", "A4", "Pub 3", 11.98));
	}

	public Collection<Book> getBooks() {
		return bookRepository.values();
	}

	public Book bookById(Integer id) {
		return bookRepository.get(id);
	}
}
