package org.tungsten.client.feature.module.config;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;


public abstract class GenericSetting<V> {

    public final String name, description;
    final V defaultValue;
    V value;

    public GenericSetting(V defaultValue, String name, String description) {
        this.name = name;
        this.description = description;
        this.defaultValue = this.value = defaultValue;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public void reset() {
        this.setValue(this.getDefaultValue());
    }


    public String getDescription() {
        return description;
    }


    public String getName() {
        return name;
    }

    public V getDefaultValue() {
        return defaultValue;
    }

    public String getDescriptor(){
        return "<p class=\"element-descriptor\">"+this.name+"</p>";
    }

    public abstract String toHTML();
}