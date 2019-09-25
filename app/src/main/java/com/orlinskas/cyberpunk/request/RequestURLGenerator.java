package com.orlinskas.cyberpunk.request;

import android.net.Uri;

import java.net.URL;

public class RequestURLGenerator {
    private final String ID = "id";
    private final String APPID = "APPID";
    private final String UNITS = "units";

    public URL generate(Request request) {
        Uri builder = Uri.parse(request.getSource() + request.getForecastType())
                .buildUpon()
                .appendQueryParameter(ID, Integer.toString(request.getCity().getId()))
                .appendQueryParameter(APPID, request.getApiKey())
                .appendQueryParameter(UNITS, request.getUnitsType())
                .build();

        URL requestOpenWeather = null;
        try {
            requestOpenWeather = new URL(builder.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestOpenWeather;
    }
}
