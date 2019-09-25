package com.orlinskas.cyberpunk.forecast;

public class InstrumentPerformance {
    private int averagePressure;
    private double averageWindSpeed;
    private int averageHumidity;
    private double averageRainVolume;
    private double averageSnowVolume;

    InstrumentPerformance(int averagePressure, double averageWindSpeed, int averageHumidity, double averageRainVolume, double averageSnowVolume) {
        this.averagePressure = averagePressure;
        this.averageWindSpeed = averageWindSpeed;
        this.averageHumidity = averageHumidity;
        this.averageRainVolume = averageRainVolume;
        this.averageSnowVolume = averageSnowVolume;
    }

    public int getAveragePressure() {
        return averagePressure;
    }

    public double getAverageWindSpeed() {
        return averageWindSpeed;
    }

    public int getAverageHumidity() {
        return averageHumidity;
    }

    public double getAverageRainVolume() {
        return averageRainVolume;
    }

    public double getAverageSnowVolume() {
        return averageSnowVolume;
    }
}
