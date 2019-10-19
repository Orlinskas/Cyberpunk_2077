package com.orlinskas.cyberpunk.forecast;

import java.io.Serializable;
import java.util.ArrayList;

public class Forecast implements Serializable {
    private String dayDate;
    private ArrayList<Weather> dayWeathers;

    public Forecast(String dayDate) {
        this.dayDate = dayDate;
        this.dayWeathers = new ArrayList<>();
    }

    public String getDayDate() {
        return dayDate;
    }

    public ArrayList<Weather> getDayWeathers() {
        return dayWeathers;
    }

    void setDayWeathers(ArrayList<Weather> dayWeathers) {
        this.dayWeathers = dayWeathers;
    }
}
