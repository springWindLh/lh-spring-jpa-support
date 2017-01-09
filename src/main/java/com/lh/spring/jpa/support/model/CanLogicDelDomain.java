package com.lh.spring.jpa.support.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * Created by lh on 2016/4/7.
 */
@MappedSuperclass
public abstract class CanLogicDelDomain extends BaseDomain {
    @Column
    protected Boolean deleted = false;

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
