package com.orlinskas.cyberpunk.chart;

import com.orlinskas.cyberpunk.R;
import com.orlinskas.cyberpunk.forecast.Weather;

import java.util.ArrayList;

import static com.orlinskas.cyberpunk.chart.Icon.*;
import static com.orlinskas.cyberpunk.chart.Icon.IC_001_ID;
import static com.orlinskas.cyberpunk.chart.Icon.IC_002_ID;
import static com.orlinskas.cyberpunk.chart.Icon.IC_003_ID;
import static com.orlinskas.cyberpunk.chart.Icon.IC_004_ID;
import static com.orlinskas.cyberpunk.chart.Icon.IC_005_ID;
import static com.orlinskas.cyberpunk.chart.Icon.IC_006_ID;
import static com.orlinskas.cyberpunk.chart.Icon.IC_007_ID;
import static com.orlinskas.cyberpunk.chart.Icon.IC_008_ID;
import static com.orlinskas.cyberpunk.chart.Icon.IC_009_ID;
import static com.orlinskas.cyberpunk.chart.Icon.IC_010_ID;
import static com.orlinskas.cyberpunk.chart.Icon.IC_011_ID;
import static com.orlinskas.cyberpunk.chart.Icon.IC_012_ID;
import static com.orlinskas.cyberpunk.chart.Icon.IC_013_ID;
import static com.orlinskas.cyberpunk.chart.Icon.IC_014_ID;
import static com.orlinskas.cyberpunk.chart.Icon.IC_015_ID;
import static com.orlinskas.cyberpunk.chart.Icon.IC_016_ID;
import static com.orlinskas.cyberpunk.chart.Icon.IC_017_ID;
import static com.orlinskas.cyberpunk.chart.Icon.IC_018_ID;
import static com.orlinskas.cyberpunk.chart.Icon.IC_019_ID;
import static com.orlinskas.cyberpunk.chart.Icon.IC_020_ID;
import static com.orlinskas.cyberpunk.chart.Icon.IC_021_ID;
import static com.orlinskas.cyberpunk.chart.Icon.IC_022_ID;

public class WeatherIconsSelector {
    public ArrayList<Integer> select(ArrayList<Weather> weathers) {
        ArrayList<Integer> iconsID = new ArrayList<>();

        for(Weather weather : weathers) {
            if(weather != null) {
                iconsID.add(findIcon(weather));
            }
        }

        return iconsID;
    }

    public int findIcon(Weather weather) {
        switch (weather.getWeatherID()) {
            case 200:
                return IC_001_ID;
            case 201:
                return IC_001_ID;
            case 202:
                return IC_001_ID;
            case 210:
                return IC_001_ID;
            case 211:
                return IC_001_ID;
            case 212:
                return IC_001_ID;
            case 221:
                return IC_001_ID;
            case 230:
                return IC_001_ID;
            case 231:
                return IC_001_ID;
            case 232:
                return IC_001_ID;
            case 300:
                return IC_002_ID;
            case 301:
                return IC_002_ID;
            case 302:
                return IC_002_ID;
            case 310:
                return IC_002_ID;
            case 311:
                return IC_003_ID;
            case 312:
                return IC_003_ID;
            case 313:
                return IC_003_ID;
            case 314:
                return IC_003_ID;
            case 321:
                return IC_003_ID;
            case 500:
                return IC_002_ID;
            case 501:
                return IC_003_ID;
            case 502:
                return IC_003_ID;
            case 503:
                return IC_004_ID;
            case 504:
                return IC_004_ID;
            case 511:
                return IC_005_ID;
            case 520:
                return IC_003_ID;
            case 521:
                return IC_004_ID;
            case 522:
                return IC_004_ID;
            case 531:
                return IC_004_ID;
            case 600:
                return IC_006_ID;
            case 601:
                return IC_007_ID;
            case 602:
                return IC_008_ID;
            case 611:
                return IC_009_ID;
            case 612:
                return IC_009_ID;
            case 613:
                return IC_009_ID;
            case 615:
                return IC_010_ID;
            case 616:
                return IC_011_ID;
            case 620:
                return IC_008_ID;
            case 621:
                return IC_008_ID;
            case 622:
                return IC_008_ID;
            case 701:
                return IC_012_ID;
            case 711:
                return IC_012_ID;
            case 721:
                return IC_012_ID;
            case 731:
                return IC_012_ID;
            case 741:
                return IC_012_ID;
            case 751:
                return IC_012_ID;
            case 761:
                return IC_012_ID;
            case 762:
                return IC_013_ID;
            case 771:
                return IC_013_ID;
            case 781:
                return IC_013_ID;
            case 800:
                if(weather.getWeatherIconID().contains("n")){
                    return IC_015_ID;
                }
                else {
                    return IC_014_ID;
                }
            case 801:
                if(weather.getWeatherIconID().contains("n")){
                    return IC_017_ID;
                }
                else {
                    return IC_016_ID;
                }
            case 802:
                if(weather.getWeatherIconID().contains("n")){
                    return IC_019_ID;
                }
                else {
                    return IC_018_ID;
                }
            case 803:
                if(weather.getWeatherIconID().contains("n")){
                    return IC_021_ID;
                }
                else {
                    return IC_020_ID;
                }
            case 804:
                return IC_022_ID;
                default:return R.drawable.im_circle_off;
        }
    }
}
