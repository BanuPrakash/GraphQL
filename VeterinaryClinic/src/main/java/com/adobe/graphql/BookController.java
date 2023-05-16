package com.adobe.graphql;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.execution.BatchLoaderRegistry;
import org.springframework.stereotype.Controller;

import com.adobe.dto.Book;
import com.adobe.dto.Rating;
import com.adobe.service.BookService;
import com.adobe.service.RatingService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class BookController {
	private final BookService bookService;
	private final RatingService ratingService;

	public BookController(BookService bookService, RatingService ratingService,
			BatchLoaderRegistry batchLoaderRegistry) {
		this.bookService = bookService;
		this.ratingService = ratingService;
		// Data Loader approach
//	    batchLoaderRegistry
//	        .forTypePair(Book.class, List.class)
//	        .registerMappedBatchLoader(
//	            (books, env) -> {
//	              log.info("Calling loader for books {}", books);
//	              Map bookListMap = ratingService.ratingsForBooks(List.copyOf(books));
//	              return Mono.just(bookListMap);
//	            });
	}	
	
	@QueryMapping()
	public Collection<Book> books() {
		log.info("Fetching all books..");
		return bookService.getBooks();
	}

	@BatchMapping
	public Map<Book, List<Rating>> ratings(List<Book> books) {
		log.info("Fetching ratings for books {} ", books);
		return ratingService.ratingsForBooks(books);
	}

}
