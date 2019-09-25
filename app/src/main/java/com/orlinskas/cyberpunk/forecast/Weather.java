package com.orlinskas.cyberpunk.forecast;

import java.io.Serializable;
import java.util.Objects;

public class Weather implements Serializable {
    private int cityID;
    private String cityName;
    private String countryCode;
    private String timeOfDataForecast;
    private String responseDate;
    private int currentTemperature;
    private int pressure;
    private int timezone;
    private int weatherID;
    private String weatherGroup;
    private String weatherGroupDescription;
    private String weatherIconID;
    private int humidity;
    private double windSpeed;
    private double rainVolume;
    private double snowVolume;

    public Weather(int cityID, String cityName, String countryCode, String timeOfDataForecast,
                   String responseDate, int currentTemperature, int pressure, int timezone,
                   int weatherID, String weatherGroup, String weatherGroupDescription, String weatherIconID,
                   int humidity, double windSpeed, double rainVolume, double snowVolume) {
        this.cityID = cityID;
        this.cityName = cityName;
        this.countryCode = countryCode;
        this.timeOfDataForecast = timeOfDataForecast;
        this.responseDate = responseDate;
        this.currentTemperature = currentTemperature;
        this.pressure = pressure;
        this.timezone = timezone;
        this.weatherID = weatherID;
        this.weatherGroup = weatherGroup;
        this.weatherGroupDescription = weatherGroupDescription;
        this.weatherIconID = weatherIconID;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.rainVolume = rainVolume;
        this.snowVolume = snowVolume;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Weather)) return false;
        Weather that = (Weather) o;
        return getCityID() == that.getCityID() &&
                getCurrentTemperature() == that.getCurrentTemperature() &&
                getPressure() == that.getPressure() &&
                getTimezone() == that.getTimezone() &&
                getWeatherID() == that.getWeatherID() &&
                getHumidity() == that.getHumidity() &&
                Double.compare(that.getWindSpeed(), getWindSpeed()) == 0 &&
                getRainVolume() == that.getRainVolume() &&
                getSnowVolume() == that.getSnowVolume() &&
                Objects.equals(getCityName(), that.getCityName()) &&
                getCountryCode().equals(that.getCountryCode()) &&
                getTimeOfDataForecast().equals(that.getTimeOfDataForecast()) &&
                getResponseDate().equals(that.getResponseDate()) &&
                Objects.equals(getWeatherGroup(), that.getWeatherGroup()) &&
                Objects.equals(getWeatherGroupDescription(), that.getWeatherGroupDescription()) &&
                Objects.equals(getWeatherIconID(), that.getWeatherIconID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCityID(), getCityName(), getCountryCode(), getTimeOfDataForecast(),
                getResponseDate(), getCurrentTemperature(), getPressure(), getTimezone(), getWeatherID(),
                getWeatherGroup(), getWeatherGroupDescription(), getWeatherIconID(), getHumidity(),
                getWindSpeed(), getRainVolume(), getSnowVolume());
    }

    public int getCityID() {
        return cityID;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getTimeOfDataForecast() {
        return timeOfDataForecast;
    }

    public String getResponseDate() {
        return responseDate;
    }

    public int getCurrentTemperature() {
        return currentTemperature;
    }

    public int getPressure() {
        return pressure;
    }

    public int getTimezone() {
        return timezone;
    }

    public int getWeatherID() {
        return weatherID;
    }

    public String getWeatherGroup() {
        return weatherGroup;
    }

    public String getWeatherGroupDescription() {
        return weatherGroupDescription;
    }

    public String getWeatherIconID() {
        return weatherIconID;
    }

    public int getHumidity() {
        return humidity;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public double getRainVolume() {
        return rainVolume;
    }

    public double getSnowVolume() {
        return snowVolume;
    }

}
