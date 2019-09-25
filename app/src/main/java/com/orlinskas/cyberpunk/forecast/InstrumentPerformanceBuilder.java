package com.orlinskas.cyberpunk.forecast;

import java.util.ArrayList;

public class InstrumentPerformanceBuilder {
    private Forecast forecast;
    private final String PRESSURE = "pressure";
    private final String WIND_SPEED = "wind";
    private final String HUMIDITY = "humidity";
    private final String RAIN = "rain";
    private final String SNOW = "snow";

    public InstrumentPerformanceBuilder(Forecast forecast) {
        this.forecast = forecast;
    }

    public InstrumentPerformance build() {
        int pressure = (int) pickOutData(PRESSURE);
        double wind = pickOutData(WIND_SPEED);
        int humidity = (int) pickOutData(HUMIDITY);
        double rain = pickOutData(RAIN);
        double snow = pickOutData(SNOW);

        return new InstrumentPerformance(pressure, wind, humidity, rain, snow);
    }

    private double pickOutData(String param) {
        ArrayList<Double> values = new ArrayList<>();

        ArrayList<Weather> dayWeathers = forecast.getDayWeathers();

        for(Weather weather : dayWeathers) {
            switch (param) {
                case PRESSURE:
                    values.add((double) weather.getPressure());
                    break;
                case WIND_SPEED:
                    values.add(weather.getWindSpeed());
                    break;
                case HUMIDITY:
                    values.add((double)weather.getHumidity());
                    break;
                case RAIN:
                    values.add(weather.getRainVolume());
                    break;
                case SNOW:
                    values.add(weather.getSnowVolume());
                    break;
            }
        }

        if(param.equals(RAIN) | param.equals(SNOW)) {
            return AveragerValue.sum(values);
        }
        return AveragerValue.average(values);
    }
}
