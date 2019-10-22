package com.orlinskas.cyberpunk.forecast;

import android.content.Context;

import com.orlinskas.cyberpunk.repository.WeatherRepository;
import com.orlinskas.cyberpunk.specification.WeatherWidgetSpecification;
import com.orlinskas.cyberpunk.widgetApp.Widget;

import java.util.ArrayList;
import java.util.LinkedHashSet;

class LatestDatesListBuilder {
    private Widget widget;
    private Context context;

    LatestDatesListBuilder(Widget widget, Context context) {
        this.widget = widget;
        this.context = context;
    }

    ArrayList<String> build() {
        WeatherRepository repository = new WeatherRepository(context);
        ArrayList<Weather> allWeathersForCity = repository.query(new WeatherWidgetSpecification(widget));

        return new ArrayList<>(find(allWeathersForCity));
    }

    private LinkedHashSet<String> find(ArrayList<Weather> allWeathersForCity) {
        LinkedHashSet<String> unique = new LinkedHashSet<>();

        for (Weather weather : allWeathersForCity) {
            unique.add(weather.getTimeOfDataForecast().substring(0, 10));
        }

        return unique;
    }
}
