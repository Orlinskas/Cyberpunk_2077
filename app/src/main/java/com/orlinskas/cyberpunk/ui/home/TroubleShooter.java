package com.orlinskas.cyberpunk.ui.home;

import com.orlinskas.cyberpunk.forecast.Forecast;
import com.orlinskas.cyberpunk.forecast.Weather;

public class TroubleShooter {
    private Forecast forecast;

    public TroubleShooter(Forecast forecast) {
        this.forecast = forecast;
    }

    public boolean shootPrecipitation() {
        for(Weather weather : forecast.getDayWeathers()) {
            double rain = weather.getRainVolume();
            double snow = weather.getSnowVolume();

            if(rain > 0 | snow > 0) {
                return true;
            }
        }
        return false;
    }

    public boolean shootWind() {
        for(Weather weather : forecast.getDayWeathers()) {
            double wind = weather.getWindSpeed();

            if(wind > 4.5) {
                return true;
            }
        }
        return false;
    }

    public boolean shootWet() {
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
        return isMinusTemp & isPlusTemp;
    }

    public boolean shootHeadPain() {
        int pressureSum = 0;

        for(Weather weather : forecast.getDayWeathers()) {
            pressureSum = pressureSum + weather.getPressure();
        }

        int pressureAverage = pressureSum / forecast.getDayWeathers().size();

        for(Weather weather : forecast.getDayWeathers()) {
            int pressureDifference = pressureAverage - weather.getPressure();

            if(Math.abs(pressureDifference) > 3) {
                return true;
            }
        }
        return false;
    }
}
