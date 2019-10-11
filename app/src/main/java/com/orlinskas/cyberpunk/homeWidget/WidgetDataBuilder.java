package com.orlinskas.cyberpunk.homeWidget;

import com.orlinskas.cyberpunk.R;
import com.orlinskas.cyberpunk.chart.WeatherIconsSelector;
import com.orlinskas.cyberpunk.date.DateCalculator;
import com.orlinskas.cyberpunk.date.DateFormat;
import com.orlinskas.cyberpunk.date.DateHelper;
import com.orlinskas.cyberpunk.forecast.Forecast;
import com.orlinskas.cyberpunk.forecast.Weather;

import java.util.Date;

public class WidgetDataBuilder {

    public Weather findCurrentWeather(Forecast forecast) {
        String nowDateString = DateHelper.getCurrent(DateFormat.YYYY_MM_DD_HH_MM);
        Date nowDate = DateHelper.parse(nowDateString, DateFormat.YYYY_MM_DD_HH_MM);

        int lastMinDifference = 200;
        Weather needWeather = forecast.getDayWeathers().get(0);

        for(Weather weather : forecast.getDayWeathers()) {
            String weatherDateString = weather.getTimeOfDataForecast();
            Date weatherDate = DateHelper.parse(weatherDateString, DateFormat.YYYY_MM_DD_HH_MM);

            DateCalculator calculator = new DateCalculator();
            int currentAbsDifference = Math.abs(calculator.calculateDifferencesInMinutes(nowDate, weatherDate));
            if(currentAbsDifference < lastMinDifference) {
                lastMinDifference = currentAbsDifference;
                needWeather = weather;
            }
        }

        return needWeather;
    }

    public String setTimezone(Forecast forecast) {
        int timezone = forecast.getDayWeathers().get(0).getTimezone();

        if(timezone > 0) {
            return "UTC +" + timezone;
        }
        else {
            return "UTC " + timezone;
        }
    }

    public String setTemperature(Weather currentWeather) {
        String temperature = "—";
        try {
            int temp = currentWeather.getCurrentTemperature();

            if(temp > 0) {
                temperature = "+" + temp + "°C";
            }
            else {
                temperature = temp + "°C";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return temperature;
    }

    public int setIcon(Weather weather) {
        int ID = R.drawable.ic_na_icon;
        WeatherIconsSelector selector = new WeatherIconsSelector();
        try {
           ID = selector.findIcon(weather);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ID;
    }
}
