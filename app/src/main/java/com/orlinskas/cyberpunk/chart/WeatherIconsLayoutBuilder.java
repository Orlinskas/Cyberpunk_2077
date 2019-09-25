package com.orlinskas.cyberpunk.chart;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.orlinskas.cyberpunk.forecast.Forecast;
import com.orlinskas.cyberpunk.forecast.Weather;

import java.util.ArrayList;

public class WeatherIconsLayoutBuilder {
    private Forecast forecast;
    private Context context;

    public WeatherIconsLayoutBuilder(Forecast forecast, Context context) {
        this.forecast = forecast;
        this.context = context;
    }

    public LinearLayout buildLayout() {
        LinearLayout layout = new LinearLayout(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        layout.setLayoutParams(params);
        layout.setOrientation(LinearLayout.HORIZONTAL);

        ArrayList<Weather> weathers = forecast.getDayWeathers();
        WeatherIconsSelector iconsManager = new WeatherIconsSelector();
        ArrayList<Integer> icons = iconsManager.select(weathers);

        LinearLayout.LayoutParams paramsImage = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        paramsImage.weight = 1.0f;

        for (int i = 0; i < icons.size(); i++) {
            ImageView iconImage = new ImageView(context);
            iconImage.setLayoutParams(paramsImage);
            iconImage.setImageResource(icons.get(i));
            layout.addView(iconImage);
        }

        return layout;
    }
}
