package com.adobe.graphql;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.adobe.dao.OwnerRepository;
import com.adobe.graphql.filter.OwnerSearchResult;
import com.adobe.graphql.filter.OwnerSpecification;

@Controller
public class OwnerController {
	@Autowired
	OwnerRepository ownerRepository;

	@QueryMapping
	public OwnerSearchResult owners(@Argument Optional<Integer> page, @Argument Optional<Integer> size,
			@Argument("filter") Optional<OwnerSpecification> filter) {
		int pageNo = page.orElse(0);
		int sizeNo = Math.min(size.orElse(20), 25);

		final PageRequest pageRequest = PageRequest.of(pageNo, sizeNo);
		return new OwnerSearchResult(ownerRepository.findAll(filter.orElse(null), pageRequest));
	}
}
