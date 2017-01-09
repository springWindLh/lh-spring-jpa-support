package com.lh.spring.jpa.support.dao;

import com.lh.spring.jpa.support.model.BaseDomain;
import com.lh.spring.jpa.support.model.Query;
import com.lh.spring.jpa.support.model.QueryItem;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Map;

/**
 * Created by lh on 2016/3/7.
 */
public interface IBaseDao<T extends BaseDomain> extends PagingAndSortingRepository<T, Long> {

    Page<T> findAll(Query query);

    List<T> findByFieldsAndValues(Object... fieldsAndValues);

    Page<T> findByFieldsAndValues(Query query, Object... fieldsAndValues);

    Page<T> findByMap(Query query, Map<String, Object> map);

    Page<T> findByQueryItems(Query query, List<QueryItem> queryItems);

}
