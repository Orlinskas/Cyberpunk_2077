package com.orlinskas.cyberpunk.request;

import com.orlinskas.cyberpunk.City;
import com.orlinskas.cyberpunk.date.DateFormat;
import com.orlinskas.cyberpunk.date.DateHelper;

import static com.orlinskas.cyberpunk.Settings.OPENWEATHERMAP_API_KEY;
import static com.orlinskas.cyberpunk.Settings.OPENWEATHERMAP_UNITS;

public class RequestBuilder {
    private final static String OPENWEATHERMAP_COM = "http://api.openweathermap.org/";
    private final static String OPENWEATHERMAP_FORECAST_5day = "data/2.5/forecast?";

    public Request build(City city) {
        String date = DateHelper.getCurrent(DateFormat.YYYY_MM_DD);

        return new Request(date, city, OPENWEATHERMAP_COM, OPENWEATHERMAP_FORECAST_5day,
                OPENWEATHERMAP_UNITS, OPENWEATHERMAP_API_KEY);
    }
}