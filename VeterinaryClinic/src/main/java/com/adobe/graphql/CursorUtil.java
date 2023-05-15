package com.adobe.graphql;

import java.util.Base64;
import java.util.List;

import org.springframework.stereotype.Component;

import graphql.relay.ConnectionCursor;
import graphql.relay.DefaultConnectionCursor;
import graphql.relay.Edge;

@Component
public class CursorUtil {

	public ConnectionCursor createCursorWith(Integer id) {
		return new DefaultConnectionCursor(Base64.getEncoder().encodeToString(id.toString().getBytes()));
	}
	
	public Integer decode(String cursor) {
		return Integer.parseInt(new String(Base64.getDecoder().decode(cursor)));
	}
	
	public <T> ConnectionCursor getFirstCursorFrom(List<Edge<T>> edges) {
		return edges.isEmpty()? null :edges.get(0).getCursor();
	}
	
	public <T> ConnectionCursor getLastCursorFrom(List<Edge<T>> edges) {
		return edges.isEmpty()? null :edges.get(edges.size() - 1).getCursor();
	}
}
