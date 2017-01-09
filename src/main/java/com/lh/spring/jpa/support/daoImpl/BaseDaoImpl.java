package com.lh.spring.jpa.support.daoImpl;

import com.lh.spring.jpa.support.dao.IBaseDao;
import com.lh.spring.jpa.support.model.BaseDomain;
import com.lh.spring.jpa.support.model.Query;
import com.lh.spring.jpa.support.model.QueryItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by lh on 2016/3/7.
 */
@NoRepositoryBean
public abstract class BaseDaoImpl<T extends BaseDomain> extends SimpleJpaRepository<T, Long> implements IBaseDao<T> {

    private static final String PARAMS_NUMBERS_EVEN_ERROR = "参数必须为偶数个";

    public BaseDaoImpl(Class<T> domainClass, EntityManager em) {
        super(domainClass, em);
    }

    @Override
    public Page<T> findAll(Query query) {
        Sort sort = new Sort(query.getDirection(), query.getSortField());
        PageRequest pageRequest = new PageRequest(query.getPage(), query.getSize(), sort);
        return findAll(pageRequest);
    }

    @Override
    public List<T> findByFieldsAndValues(Object... fieldsAndValues) {
        return findAll(getWhereSpecification(fieldsAndValues));
    }

    @Override
    public Page<T> findByFieldsAndValues(Query query, Object... fieldsAndValues) {
        Sort sort = new Sort(query.getDirection(), query.getSortField());
        PageRequest pageRequest = new PageRequest(query.getPage(), query.getSize(), sort);
        return findAll(getWhereSpecification(fieldsAndValues), pageRequest);
    }

    @Override
    public Page<T> findByMap(Query query, Map<String, Object> map) {
        Sort sort = new Sort(query.getDirection(), query.getSortField());
        PageRequest pageRequest = new PageRequest(query.getPage(), query.getSize(), sort);
        return findAll(getWhereSpecification(map), pageRequest);
    }

    @Override
    public Page<T> findByQueryItems(Query query, List<QueryItem> queryItems) {
        Sort sort = new Sort(query.getDirection(), query.getSortField());
        PageRequest pageRequest = new PageRequest(query.getPage(), query.getSize(), sort);
        return findAll(getWhereSpecification(queryItems), pageRequest);
    }

    private Specification getWhereSpecification(final Object... fieldsAndValues) {
        this.CheckFieldsAndValues();
        Specification specification = new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                List<Object> predicates = new ArrayList<>();
                for (int i = 0; i < fieldsAndValues.length; i += 2) {
                    Predicate predicate = cb.equal(root.get(fieldsAndValues[i].toString()), fieldsAndValues[i + 1]);
                    predicates.add(predicate);
                }
                query.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
                return query.getRestriction();
            }
        };
        return specification;
    }

    private Specification getWhereSpecification(final Map<String, Object> paramsMap) {
        Specification specification = new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                List<Object> predicates = new ArrayList<>();
                for (Map.Entry entry : paramsMap.entrySet()) {
                    Predicate predicate = cb.equal(root.get(entry.getKey().toString()), entry.getValue());
                    predicates.add(predicate);
                }
                query.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
                return query.getRestriction();
            }
        };
        return specification;
    }

    private Specification getWhereSpecification(final List<QueryItem> queryItems) {
        Specification specification = new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                List<Object> predicates = new ArrayList<>();
                for (QueryItem item : queryItems) {
                    Predicate predicate = null;
                    if (item.getOperatorType() != null) {
                        switch (item.getOperatorType()) {
                            case LIKE:
                                predicate = cb.like(root.get(item.getField()), "%" + item.getValue() + "%");
                                break;
                            case GREATER_THAN:
                                predicate = cb.gt(root.get(item.getField()), (Number) item.getValue());
                                break;
                            case LESS_THAN:
                                predicate = cb.lt(root.get(item.getField()), (Number) item.getValue());
                                break;
                            case GREATER_THAN_OR_EQUAL:
                                predicate = cb.ge(root.get(item.getField()), (Number) item.getValue());
                                break;
                            case LESS_THAN_OR_EQUAL:
                                predicate = cb.le(root.get(item.getField()), (Number) item.getValue());
                                break;
                            default:
                                predicate = cb.equal(root.get(item.getField()), item.getValue());
                                break;
                        }
                    } else {
                        predicate = cb.equal(root.get(item.getField()), item.getValue());
                    }
                    predicates.add(predicate);
                }
                query.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
                return query.getRestriction();
            }
        };
        return specification;
    }

    private boolean CheckFieldsAndValues(Object... fieldsAndValues) {
        if (fieldsAndValues.length % 2 != 0) {
            throw new IllegalArgumentException(PARAMS_NUMBERS_EVEN_ERROR);
        } else {
            return true;
        }
    }

}
