package com.adobe.dto;

public record Rating(Integer id, Integer bookId, Integer rating, String comment, String user) {
}

