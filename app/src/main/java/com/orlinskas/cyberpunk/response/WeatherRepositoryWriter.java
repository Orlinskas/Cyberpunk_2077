package com.orlinskas.cyberpunk.response;

import android.content.Context;

import com.orlinskas.cyberpunk.forecast.Weather;
import com.orlinskas.cyberpunk.repository.WeatherRepository;

import java.util.ArrayList;

public class WeatherRepositoryWriter {
    private Context context;

    public WeatherRepositoryWriter(Context context) {
        this.context = context;
    }

    public void write(ArrayList<Weather> weathers) {
        WeatherRepository weatherRepository = new WeatherRepository(context);

        for(Weather weather : weathers) {
            weatherRepository.add(weather);
        }
    }
}
