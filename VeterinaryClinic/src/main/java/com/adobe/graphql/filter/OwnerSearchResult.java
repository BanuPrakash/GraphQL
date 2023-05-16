package com.adobe.graphql.filter;

import java.util.List;

import org.springframework.data.domain.Page;

import com.adobe.entity.Owner;

public class OwnerSearchResult {
    private final Page<Owner> result;

    public OwnerSearchResult(Page<Owner> result) {
        this.result = result;
    }

    public PageInfo getPageInfo() {
        return new PageInfo(result);
    }

    public List<Owner> getOwners() {
        return result.getContent();
    }
}
