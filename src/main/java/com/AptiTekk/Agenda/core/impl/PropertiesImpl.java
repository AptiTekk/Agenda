package com.AptiTekk.Agenda.core.impl;

import javax.ejb.Stateless;

import com.AptiTekk.Agenda.core.Properties;
import com.AptiTekk.Agenda.core.entity.AppProperty;
import com.AptiTekk.Agenda.core.entity.QAppProperty;
import com.mysema.query.jpa.impl.JPAQuery;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class PropertiesImpl implements Properties {
    
    @PersistenceContext(unitName = "Agenda")
    protected EntityManager entityManager;

    protected Class<AppProperty> entityType = AppProperty.class;

    QAppProperty table = QAppProperty.appProperty;

    public PropertiesImpl() {
    }

    @Override
    public String get(String key) {
        return new JPAQuery(entityManager).from(table).where(table.propertyKey.eq(key))
                .uniqueResult(table).getValue();
    }

    @Override
    public AppProperty getProperty(String key) {
        return new JPAQuery(entityManager).from(table).where(table.propertyKey.eq(key))
                .uniqueResult(table);
    }

    @Override
    public void put(String key, String value) {
        if (get(key) == null) {
            AppProperty property = new AppProperty(key, value);
            insert(property);
        } else {
            AppProperty property = getProperty(key);
            property.setValue(value);
            merge(property);
        }
    }

    @Override
    public List<AppProperty> getAll() {
        Query query = this.entityManager.createQuery("SELECT e FROM " + this.entityType.getSimpleName() + " e");
        return query.getResultList();
    }

    @Override
    public AppProperty merge(AppProperty property) {
        return entityManager.merge(property);
    }

    @Override
    public void insert(AppProperty property) {
        entityManager.persist(property);
    }

}
