package org.tungsten.client.config;

public class ConfigEntry<T> {
    private T value;
    private T default_value;

    public ConfigEntry(T value, T default_value) {
        this.value = value;
        this.default_value = default_value;
    }

    public static <T> ConfigEntry<T> create(T value, T default_value){
        return new ConfigEntry<T>(value,default_value);
    }

    public static <T> ConfigEntry<T> create(T value){
        return new ConfigEntry<T>(value,value);
    }

    public T getValue() {
        return value;
    }

    public T getDefault() {
        return default_value;
    }

    public void setValue(T new_value){
        this.value = new_value;
    }

    public void setDefault(T new_default){
        this.default_value = new_default;
    }

    public void reset(){
        setValue(getDefault());
    }
}
