package com.adobe.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

import com.adobe.graphql.UpperCaseDirective;

@Configuration
public class GraphQLCustomConfig {
	@Bean
	RuntimeWiringConfigurer runtimeWiringConfigurer() {
		return  builder -> {
				builder.directive("uppercase", new UpperCaseDirective());
			};
	}
}
