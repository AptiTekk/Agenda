package com.aptitekk.agenda.core.entity;

import com.aptitekk.agenda.core.properties.PropertyKey;
import com.aptitekk.agenda.core.utilities.EqualsHelper;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import java.io.Serializable;

/**
 * Entity implementation class for Entity: Property
 */
@Entity
@NamedQuery(name = "AppProperty.findAll", query = "SELECT e FROM Property e")
public class Property implements Serializable {

    @Id
    @GeneratedValue
    private int id;

    private PropertyKey propertyKey;
    private String propertyValue;

    private static final long serialVersionUID = 1L;

    public Property() {
    }

    public Property(PropertyKey propertyKey, String propertyValue) {
        setPropertyKey(propertyKey);
        setPropertyValue(propertyValue);
    }

    public PropertyKey getPropertyKey() {
        return this.propertyKey;
    }

    public void setPropertyKey(PropertyKey key) {
        this.propertyKey = key;
    }

    public String getPropertyValue() {
        return this.propertyValue;
    }

    public void setPropertyValue(String value) {
        this.propertyValue = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null) return false;

        if (!(o instanceof Property)) return false;

        Property other = (Property) o;

        return EqualsHelper.areEquals(getPropertyKey(), other.getPropertyKey())
                && EqualsHelper.areEquals(getPropertyValue(), other.getPropertyValue());
    }

    @Override
    public int hashCode() {
        return EqualsHelper.calculateHashCode(getPropertyKey(), getPropertyValue());
    }
}
