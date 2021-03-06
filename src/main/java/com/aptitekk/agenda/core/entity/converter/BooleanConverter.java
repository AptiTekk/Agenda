package com.aptitekk.agenda.core.entity.converter;

import javax.persistence.AttributeConverter;

/**
 * Created by kevint on 6/26/2016.
 * <p>
 * Makes stored booleans readable.
 */
public class BooleanConverter implements AttributeConverter<Boolean, String> {

    private static final String TRUE = "True";
    private static final String FALSE = "False";

    @Override
    public String convertToDatabaseColumn(Boolean attribute) {
        if (attribute != null)
            if (attribute) {
                return TRUE;
            } else {
                return FALSE;
            }
        return FALSE;
    }

    @Override
    public Boolean convertToEntityAttribute(String dbData) {
        if (dbData != null) {
            return dbData.equals(TRUE);
        }
        return false;
    }

}