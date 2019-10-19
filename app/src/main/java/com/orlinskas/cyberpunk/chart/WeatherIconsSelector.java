package com.orlinskas.cyberpunk.chart;

import com.orlinskas.cyberpunk.R;
import com.orlinskas.cyberpunk.forecast.Weather;

import java.util.ArrayList;

import static com.orlinskas.cyberpunk.chart.Icon.*;

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
            case 201:
            case 202:
            case 210:
            case 211:
            case 212:
            case 221:
            case 230:
            case 231:
            case 232:
                return IC_001_ID;
            case 300:
            case 301:
            case 302:
            case 310:
            case 500:
                return IC_002_ID;
            case 311:
            case 312:
            case 313:
            case 314:
            case 321:
            case 501:
            case 502:
            case 520:
                return IC_003_ID;
            case 503:
            case 504:
            case 521:
            case 522:
            case 531:
                return IC_004_ID;
            case 511:
                return IC_005_ID;
            case 600:
                return IC_006_ID;
            case 601:
                return IC_007_ID;
            case 602:
            case 620:
            case 621:
            case 622:
                return IC_008_ID;
            case 611:
            case 612:
            case 613:
                return IC_009_ID;
            case 615:
                return IC_010_ID;
            case 616:
                return IC_011_ID;
            case 701:
            case 711:
            case 721:
            case 731:
            case 741:
            case 751:
            case 761:
                return IC_012_ID;
            case 762:
            case 771:
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
