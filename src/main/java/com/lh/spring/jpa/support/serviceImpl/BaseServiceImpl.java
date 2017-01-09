package com.lh.spring.jpa.support.serviceImpl;

import com.lh.spring.jpa.support.dao.IBaseDao;
import com.lh.spring.jpa.support.model.BaseDomain;
import com.lh.spring.jpa.support.model.CanLogicDelDomain;
import com.lh.spring.jpa.support.model.Query;
import com.lh.spring.jpa.support.model.QueryItem;
import com.lh.spring.jpa.support.service.IBaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by lh on 2016/3/12.
 */
@NoRepositoryBean
public abstract class BaseServiceImpl<T extends BaseDomain> implements IBaseService<T> {

    public abstract IBaseDao<T> getDao();

    public Optional<T> add(T entity) {
        return Optional.ofNullable(this.getDao().save(entity));
    }

    public Optional<T> update(T entity) {
        return Optional.ofNullable(this.getDao().save(entity));
    }

    @Override
    public Page<T> findAll(Query query) {
        return this.getDao().findAll(query);
    }

    @Override
    public List<T> findByFieldsAndValues(Object... fieldsAndValues) {
        return this.getDao().findByFieldsAndValues(fieldsAndValues);
    }

    @Override
    public Page<T> findByFieldsAndValues(Query query, Object... fieldsAndValues) {
        return this.getDao().findByFieldsAndValues(query, fieldsAndValues);
    }

    @Override
    public Page<T> findByParamsMap(Query query, Map<String, Object> paramsMap) {
        return this.getDao().findByMap(query, paramsMap);
    }

    @Override
    public Page<T> findByQueryItems(Query query, List<QueryItem> queryItems) {
        return this.getDao().findByQueryItems(query, queryItems);
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        return getDao().findAll(pageable);
    }

    @Override
    public Optional<T> findOne(Long id) {
        return Optional.ofNullable(getDao().findOne(id));
    }

    private boolean isCanLogicDel(T entity) {
        return (entity instanceof CanLogicDelDomain);
    }

    private boolean isCanLogicDel(Class<T> clazz) {
        return CanLogicDelDomain.class.isAssignableFrom(clazz);
    }

    private void logicDel(Class<T> clazz, Long id) {
        if (isCanLogicDel(clazz)) {
            Optional<T> optional = this.findOne(id);
            if (!optional.isPresent()) {
                throw new IllegalArgumentException("can not find a domain with the id");
            }
            try {
                T entity = optional.get();
                Method method = entity.getClass().getMethod("setDeleted", Boolean.class);
                method.invoke(entity, true);
                this.update(entity);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            getDao().delete(id);
        }
    }

    private void logicDel(T entity) {
        if (isCanLogicDel(entity)) {
            try {
                Method method = entity.getClass().getMethod("setDeleted", Boolean.class);
                method.invoke(entity, true);
                this.update(entity);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            getDao().delete(entity);
        }
    }

    private void logicDel(Iterable<? extends T> entities) {
        for (T entity : entities) {
            logicDel(entity);
        }
    }

    @Override
    public void delete(Class<T> clazz, Long id) {
        logicDel(clazz, id);
    }

    @Override
    public void delete(T entity) {
        this.logicDel(entity);
    }

    @Override
    public void delete(Iterable<? extends T> entities) {
        this.logicDel(entities);
    }

    @Override
    public void deleteAll(Class<T> clazz) {
        if (!isCanLogicDel(clazz)) {
            getDao().deleteAll();
        }
    }
}
