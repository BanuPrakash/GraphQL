package com.adobe.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.adobe.dto.Book;
import com.adobe.dto.Rating;

@Service
public class RatingService {
    private final HashMap<Integer, Rating> ratingRepository = new HashMap<>();

    public RatingService() {
        ratingRepository.put(1, new Rating(1,1,5,  "C 1", "U 1"));
        ratingRepository.put(2, new Rating(2,1,3,  "C 2", "U 2"));
        ratingRepository.put(3, new Rating(3,1,3,  "C 3", "U 3"));

        ratingRepository.put(4, new Rating(4,3,5,  "C 4", "U 1"));
        ratingRepository.put(5, new Rating(5,3,1,  "C 5", "U 3"));
    }
    
    public List<Rating> ratings(Book book) {
        return ratingRepository.values().stream().filter( r -> r.bookId().equals(book.id())).toList();
    }
    
    public Map<Book, List<Rating>> ratingsForBooks(List<Book> books) {
        return books.stream()
                .collect(
                        Collectors.toMap(
                                Function.identity(),
                                book ->
                                        ratingRepository.values().stream()
                                                .filter(r -> r.bookId().equals(book.id()))
                                                .toList()));
    }

}
