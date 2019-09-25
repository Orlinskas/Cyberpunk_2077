package com.orlinskas.cyberpunk;

import java.io.Serializable;
import java.util.Objects;

public class Country implements Serializable {
    private String code;
    private String name;

    public Country(String code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Country)) return false;
        Country country = (Country) o;
        return getCode().equals(country.getCode()) &&
                Objects.equals(getName(), country.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCode(), getName());
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

}
