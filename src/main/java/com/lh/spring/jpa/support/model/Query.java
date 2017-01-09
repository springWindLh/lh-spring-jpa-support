package com.lh.spring.jpa.support.model;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * Created by lh on 2016/4/19.
 */
public class Query {
    private int page = 0;
    private int size = 10;
    private String sortField = "id";
    private Sort.Direction direction = Sort.Direction.DESC;

    public Query() {
    }

    public Query(int page, int size, String sortField, Sort.Direction direction) {
        if (page < 0 || size < 0) throw new IllegalArgumentException("index or size must be natural number");
        this.page = page;
        this.size = size;
        this.sortField = sortField;
        this.direction = direction;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public Sort.Direction getDirection() {
        return direction;
    }

    public void setDirection(Sort.Direction direction) {
        this.direction = direction;
    }

    public Pageable getPageable() {
        PageRequest request = new PageRequest(page, size, direction, sortField);
        return request;
    }
}
