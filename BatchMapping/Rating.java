package com.adobe.entity;

public record Rating(Integer id, Integer bookId, Integer rating, String comment, String user) {
}
