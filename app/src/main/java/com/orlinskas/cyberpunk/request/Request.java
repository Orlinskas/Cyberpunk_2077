package com.orlinskas.cyberpunk.request;

import com.orlinskas.cyberpunk.City;

import java.io.Serializable;

public class Request implements Serializable {
    private String date;
    private City city;
    private String source;
    private String forecastType;
    private String unitsType;
    private String apiKey;

    public Request(String date, City city, String source, String forecastType, String unitsType, String apiKey) {
        this.date = date;
        this.city = city;
        this.source = source;
        this.forecastType = forecastType;
        this.unitsType = unitsType;
        this.apiKey = apiKey;
    }

    public String getDate() {
        return date;
    }

    public City getCity() {
        return city;
    }

    String getSource() {
        return source;
    }

    String getForecastType() {
        return forecastType;
    }

    String getUnitsType() {
        return unitsType;
    }

    String getApiKey() {
        return apiKey;
    }
}
