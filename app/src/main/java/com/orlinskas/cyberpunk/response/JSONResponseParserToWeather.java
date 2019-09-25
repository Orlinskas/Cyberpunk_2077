package com.orlinskas.cyberpunk.response;

import com.orlinskas.cyberpunk.date.DateCalculator;
import com.orlinskas.cyberpunk.date.DateFormat;
import com.orlinskas.cyberpunk.date.DateHelper;
import com.orlinskas.cyberpunk.forecast.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JSONResponseParserToWeather {
    private int cityID;
    private String cityName;
    private String countryCode;
    private String timeOfDataForecast;
    private String responseDate;
    private int currentTemperature;
    private int pressure;
    private int timezone;
    private int weatherID;
    private String weatherGroup;
    private String weatherGroupDescription;
    private String weatherIconID;
    private int humidity;
    private double windSpeed;
    private double rainVolume;
    private double snowVolume;

    public ArrayList<Weather> parse(String json) {
        ArrayList<Weather> weathers = new ArrayList<>();
        try {
            JSONObject fullJson = new JSONObject(json);
            writeCityInfo(fullJson.getJSONObject("city"));
            writeCurrentDate();

            JSONArray forecastList = fullJson.getJSONArray("list");
            for (int i = 0; i < forecastList.length(); i++) {
                JSONObject dayForecast = forecastList.getJSONObject(i);

                checkPrecipitation(dayForecast);
                timeOfDataForecast = calculateWithTimeZone(dayForecast.getString("dt_txt"));
                writeMainInfo(dayForecast.getJSONObject("main"));
                writeWeatherInfo(dayForecast.getJSONArray("weather").getJSONObject(0));
                writeWindInfo(dayForecast.getJSONObject("wind"));

                weathers.add(createWeatherObject());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return weathers;
    }

    private void writeCityInfo(JSONObject object) {
        try {
            cityID = object.getInt("id");
            cityName = object.getString("name");
            countryCode = object.getString("country");
            int timezoneInSeconds = object.getInt("timezone");
            timezone = timezoneInSeconds / 3600;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void writeCurrentDate() {
        responseDate = DateHelper.getCurrent(DateFormat.YYYY_MM_DD_HH_MM);
    }

    private void checkPrecipitation(JSONObject dayForecast) {
        if (dayForecast.has("rain")){
            try {
                writeRain(dayForecast.getJSONObject("rain"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else {
            rainVolume = 0;
        }

        if (dayForecast.has("snow")){
            try {
                writeSnow(dayForecast.getJSONObject("snow"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else {
            snowVolume = 0;
        }
    }

    private String calculateWithTimeZone(String dateLine) {
        DateCalculator calculator = new DateCalculator();
        return calculator.plusHours(dateLine, timezone, DateFormat.YYYY_MM_DD_HH_MM);
    }

    private void writeMainInfo(JSONObject object) {
        try {
            currentTemperature = (int) object.getDouble("temp");
            pressure = (int) object.getDouble("pressure");
            humidity = object.getInt("humidity");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void writeWeatherInfo(JSONObject object) {
        try {
            weatherID = object.getInt("id");
            weatherGroup = object.getString("main");
            weatherGroupDescription = object.getString("description");
            weatherIconID = object.getString("icon");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void writeWindInfo(JSONObject object) {
        try {
            windSpeed = object.getDouble("speed");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void writeRain(JSONObject object) throws JSONException {
        rainVolume = object.getDouble("3h");
    }

    private void writeSnow(JSONObject object) throws JSONException {
        snowVolume = object.getDouble("3h");
    }

    private Weather createWeatherObject() {
        return new Weather(cityID, cityName, countryCode, timeOfDataForecast, responseDate,
                currentTemperature, pressure, timezone, weatherID, weatherGroup,
                weatherGroupDescription, weatherIconID, humidity, windSpeed, rainVolume,
                snowVolume);
    }
}
