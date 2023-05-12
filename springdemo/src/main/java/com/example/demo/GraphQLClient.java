package com.example.demo;

import java.util.List;
import java.util.function.UnaryOperator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

import com.example.demo.entity.Product;
import com.example.demo.service.ProductService;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.idl.RuntimeWiring.Builder;
import graphql.schema.idl.TypeRuntimeWiring;


@Configuration
public class GraphQLClient {
	
	@Autowired
	ProductService service;
	@Bean
	RuntimeWiringConfigurer runtimeWiringConfigurer() {
		return new RuntimeWiringConfigurer() {

			@Override
			public void configure(Builder builder) {
				builder.type("Query", new UnaryOperator<TypeRuntimeWiring.Builder>() {
					
					@Override
					public graphql.schema.idl.TypeRuntimeWiring.Builder 
						apply(graphql.schema.idl.TypeRuntimeWiring.Builder t) {
						return t.dataFetcher("products", new DataFetcher<List<Product>>() {
							@Override
							public List<Product> get(DataFetchingEnvironment environment) throws Exception {
								return service.getProducts();
							}
						}).dataFetcher("productById",  new DataFetcher<Product>() {
							@Override
							public Product get(DataFetchingEnvironment environment) throws Exception {
								return service.getProductById(environment.getArgument("id"));
							}
					  });
					}
				});
			}
		};
	}

}


