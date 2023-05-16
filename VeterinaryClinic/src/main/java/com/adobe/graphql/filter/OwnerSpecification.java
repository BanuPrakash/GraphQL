package com.adobe.graphql.filter;

import java.util.LinkedList;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.adobe.entity.Owner;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Data;

@Component
@Data
public class OwnerSpecification implements Specification<Owner> {
	private static final long serialVersionUID = 1L;
	private String firstName;
	private String lastName;
	private String address;
	private String city;
	private String telephone;

	private boolean isSet(String s) {
		return s != null && !s.isBlank();
	}

	@Override
	public Predicate toPredicate(Root<Owner> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		var predicates = new LinkedList<Predicate>();

		if (isSet(firstName)) {
			predicates.add(criteriaBuilder.like(root.get("firstName"), "%" + firstName + "%"));
		}

		if (isSet(lastName)) {
			predicates.add(criteriaBuilder.like(root.get("lastName"), "%" + lastName + "%"));
		}

		if (isSet(address)) {
			predicates.add(criteriaBuilder.like(root.get("address"), "%" + address + "%"));
		}

		if (isSet(city)) {
			predicates.add(criteriaBuilder.equal(root.get("city"), city));
		}

		if (isSet(telephone)) {
			predicates.add(criteriaBuilder.equal(root.get("telephone"), telephone));
		}

		if (predicates.isEmpty()) {
			return null;
		}
		return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
	}

}
