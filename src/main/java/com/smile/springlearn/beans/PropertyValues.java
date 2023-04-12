package com.smile.springlearn.beans;

import java.util.ArrayList;
import java.util.List;

public class PropertyValues {

    private final List<PropertyValue> propertyValuesList = new ArrayList<>();

    public void addPropertyValue(PropertyValue pv) {
        propertyValuesList.add(pv);
    }

    public PropertyValue[] getPropertyValue() {
        return this.propertyValuesList.toArray(new PropertyValue[0]);
    }

    public PropertyValue getPropertyValue(String propertyName) {
        for (int i = 0; i < propertyValuesList.size(); i++) {
            PropertyValue pv = this.propertyValuesList.get(i);
            if (pv.getName().equals(propertyName)) {
                return pv;
            }
        }
        return null;
    }

}
