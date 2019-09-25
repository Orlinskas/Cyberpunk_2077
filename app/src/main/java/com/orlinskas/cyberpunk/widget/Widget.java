package com.orlinskas.cyberpunk.widget;

import android.support.annotation.NonNull;

import com.orlinskas.cyberpunk.City;
import com.orlinskas.cyberpunk.forecast.Forecast;
import com.orlinskas.cyberpunk.request.Request;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class Widget implements Serializable {
    private int id;
    private String source = "OpenWeather";
    private City city;
    private Request request;
    private ArrayList<Forecast> daysForecast;

    public Widget(int id, City city, Request request) {
        this.id = id;
        this.city = city;
        this.request = request;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Widget)) return false;
        Widget widget = (Widget) o;
        return getCity().equals(widget.getCity());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCity());
    }

    @NonNull
    @Override
    public String toString() {
        return getCity().getName();
    }

    public void setCity(City city) {
        this.city = city;
    }

    public City getCity() {
        return city;
    }

    public int getId() {
        return id;
    }

    public Request getRequest() {
        return request;
    }

    public ArrayList<Forecast> getDaysForecast() {
        return daysForecast;
    }

    public void setDaysForecast(ArrayList<Forecast> daysForecast) {
        this.daysForecast = daysForecast;
    }
}
