package com.adobe.graphql;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetcherFactories;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLFieldsContainer;
import graphql.schema.GraphQLObjectType;
import graphql.schema.idl.SchemaDirectiveWiring;
import graphql.schema.idl.SchemaDirectiveWiringEnvironment;

// @uppercase
public class UpperCaseDirective implements SchemaDirectiveWiring {
	
	@Override
	public GraphQLObjectType onObject(SchemaDirectiveWiringEnvironment<GraphQLObjectType> environment) {
		// TODO Auto-generated method stub
		return SchemaDirectiveWiring.super.onObject(environment);
	}
	
	@Override
	public GraphQLFieldDefinition onField(SchemaDirectiveWiringEnvironment<GraphQLFieldDefinition> env) {
		GraphQLFieldDefinition field = env.getElement();
		GraphQLFieldsContainer parentType = env.getFieldsContainer();
		
		DataFetcher<?> originalFetcher = env.getCodeRegistry().getDataFetcher(parentType, field);
		
		 DataFetcher<?> dataFetcher = DataFetcherFactories.wrapDataFetcher(originalFetcher, (((dataFetchingEnvironment, value) -> {
		      if (value instanceof String) {
		        return  ((String) value).toUpperCase();
		      }
		      return value;
		    })));
		 
		 env.getCodeRegistry().dataFetcher(parentType, field, dataFetcher);
		 return field;
	}
}
