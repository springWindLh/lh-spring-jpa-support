package com.lh.spring.jpa.support.service;

import com.lh.spring.jpa.support.model.BaseDomain;
import com.lh.spring.jpa.support.model.Query;
import com.lh.spring.jpa.support.model.QueryItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by lh on 2016/3/12.
 */
public interface IBaseService<T extends BaseDomain> {
    Optional<T> add(T entity);

    Optional<T> update(T entity);

    Optional<T> findOne(Long id);

    Page<T> findAll(Query query);

    List<T> findByFieldsAndValues(Object... fieldsAndValues);

    Page<T> findByFieldsAndValues(Query query, Object... fieldsAndValues);

    Page<T> findByParamsMap(Query query, Map<String, Object> paramsMap);

    Page<T> findByQueryItems(Query query, List<QueryItem> queryItems);

    Page<T> findAll(Pageable pageable);

    void delete(Class<T> clazz, Long id);

    void delete(T entity);

    void delete(Iterable<? extends T> entities);

    void deleteAll(Class<T> clazz);
}
