package com.orlinskas.cyberpunk.request;

import com.orlinskas.cyberpunk.City;
import com.orlinskas.cyberpunk.date.DateFormat;
import com.orlinskas.cyberpunk.date.DateHelper;

public class RequestBuilder {
    public static final String OPEN_WEATHER = "OpenWeather";
    private final static String OPENWEATHERMAP_COM = "http://api.openweathermap.org/";
    private final static String OPENWEATHERMAP_FORECAST_5day = "data/2.5/forecast?";
    private final static String OPENWEATHERMAP_API_KEY = "a39b0e16bbd652220c6c82560e6814a6";
    private final static String OPENWEATHERMAP_UNITS = "metric";

    private String date;
    private String source;
    private String forecastType;
    private String unitsType;
    private String apiKey;

    public Request build(City city) {
        date = DateHelper.getCurrent(DateFormat.YYYY_MM_DD);
        source = OPENWEATHERMAP_COM;
        forecastType = OPENWEATHERMAP_FORECAST_5day;
        unitsType = OPENWEATHERMAP_UNITS;
        apiKey = OPENWEATHERMAP_API_KEY;
        return new Request(date, city, source, forecastType, unitsType, apiKey);
    }
}