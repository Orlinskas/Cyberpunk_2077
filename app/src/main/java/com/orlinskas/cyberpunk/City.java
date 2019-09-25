package com.orlinskas.cyberpunk;

import java.io.Serializable;
import java.util.Objects;

public class City implements Serializable {
    private int id;
    private String name;
    private String countryCode;
    private double coordLon;
    private double coordLat;

    public City(int id, String name, String countryCode, double coordLon, double coordLat) {
        this.id = id;
        this.name = name;
        this.countryCode = countryCode;
        this.coordLon = coordLon;
        this.coordLat = coordLat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof City)) return false;
        City city = (City) o;
        return getId() == city.getId() &&
                Double.compare(city.getCoordLon(), getCoordLon()) == 0 &&
                Double.compare(city.getCoordLat(), getCoordLat()) == 0 &&
                Objects.equals(getName(), city.getName()) &&
                Objects.equals(getCountryCode(), city.getCountryCode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getCountryCode(), getCoordLon(), getCoordLat());
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public double getCoordLon() {
        return coordLon;
    }

    public double getCoordLat() {
        return coordLat;
    }

}
