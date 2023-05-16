package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

	@GetMapping("/hello")
	public String getHello() {
		return "Hello Spring boot!!!";
	}
	
	@GetMapping("/posts")
	public String getPosts() {
		return "Posts!!!";
	}
	
	@GetMapping("/authors")
	public String getAuthors() {
		return "Authors!!!";
	}
}
