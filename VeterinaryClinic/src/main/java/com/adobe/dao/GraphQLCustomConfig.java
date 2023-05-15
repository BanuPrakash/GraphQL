package com.adobe.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

import com.adobe.graphql.DateCoercing;
import com.adobe.graphql.UpperCaseDirective;

import graphql.schema.GraphQLScalarType;

@Configuration
public class GraphQLCustomConfig {
	// directive
	@Bean
	RuntimeWiringConfigurer runtimeWiringConfigurer() {
		return  builder -> {
				builder.directive("uppercase", new UpperCaseDirective());
				builder.scalar(dateScalar());
			};
	}
	
	// Scalar Type
	  @Bean
	    public GraphQLScalarType dateScalar() {
	        return GraphQLScalarType.newScalar()
	            .name("Date")
	            .description("Java 8 LocalDate as scalar.")
	            .coercing(new DateCoercing()).build();
	  }

}
