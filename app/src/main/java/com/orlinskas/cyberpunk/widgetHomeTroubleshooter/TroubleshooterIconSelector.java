package com.orlinskas.cyberpunk.widgetHomeTroubleshooter;

import com.orlinskas.cyberpunk.forecast.Forecast;
import com.orlinskas.cyberpunk.forecast.Weather;

import static com.orlinskas.cyberpunk.Settings.DANGEROUS_PRECIPITATION_VALUE;
import static com.orlinskas.cyberpunk.Settings.DANGEROUS_PRESSURE_VIBRATION_VALUE;
import static com.orlinskas.cyberpunk.Settings.DANGEROUS_WIND_VALUE;

class TroubleshooterIconSelector {
    private Forecast forecast;

    TroubleshooterIconSelector(Forecast forecast) {
        this.forecast = forecast;
    }

    boolean shootPrecipitation() {
        for(Weather weather : forecast.getDayWeathers()) {
            double rain = weather.getRainVolume();
            double snow = weather.getSnowVolume();

            if(rain > DANGEROUS_PRECIPITATION_VALUE || snow > DANGEROUS_PRECIPITATION_VALUE) {
                return true;
            }
        }
        return false;
    }

    boolean shootWind() {
        for(Weather weather : forecast.getDayWeathers()) {
            double wind = weather.getWindSpeed();

            if(wind > DANGEROUS_WIND_VALUE) {
                return true;
            }
        }
        return false;
    }

    boolean shootWet() {
        boolean isMinusTemp = false;
        boolean isPlusTemp = false;

        for(Weather weather : forecast.getDayWeathers()) {
            int currentTemperature = weather.getCurrentTemperature();

            if(currentTemperature <= 0) {
                isMinusTemp = true;
            }
            else {
                isPlusTemp = true;
            }
        }
        return isMinusTemp && isPlusTemp;
    }

    boolean shootHeadPain() {
        int pressureSum = 0;

        for(Weather weather : forecast.getDayWeathers()) {
            pressureSum = pressureSum + weather.getPressure();
        }

        int pressureAverage = pressureSum / forecast.getDayWeathers().size();

        for(Weather weather : forecast.getDayWeathers()) {
            int pressureDifference = pressureAverage - weather.getPressure();

            if(Math.abs(pressureDifference) > DANGEROUS_PRESSURE_VIBRATION_VALUE) {
                return true;
            }
        }
        return false;
    }
}
