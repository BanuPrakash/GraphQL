package com.adobe.graphql;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;

public class DateCoercing implements Coercing<LocalDate, String> {
	
	@Override
	public String serialize(Object dataFetcherResult) throws CoercingSerializeException {
		if(dataFetcherResult instanceof LocalDate)
			return DateTimeFormatter.ofPattern("yyyy/MM/dd").format((LocalDate)dataFetcherResult);
		return null;
	}

	@Override
	public LocalDate parseValue(Object input) throws CoercingParseValueException {
		if(input instanceof LocalDate) 
		return (LocalDate) input;
		else if(input instanceof String) {
			return LocalDate.parse((String) input,DateTimeFormatter.ofPattern("yyyy/MM/dd") );
		}
		return null;
	}

	@Override
	public LocalDate parseLiteral(Object input) throws CoercingParseLiteralException {
		if(input instanceof StringValue) 
			return LocalDate.parse(((StringValue) input).getValue(),DateTimeFormatter.ofPattern("yyyy/MM/dd") );
			
		return null;
	}

}
