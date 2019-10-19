package com.orlinskas.cyberpunk.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.orlinskas.cyberpunk.data.WeatherDatabaseAdapter;
import com.orlinskas.cyberpunk.forecast.Weather;
import com.orlinskas.cyberpunk.specification.SqlSpecification;

import java.util.ArrayList;

import static com.orlinskas.cyberpunk.data.WeatherDatabase.*;

public class WeatherRepository implements Repository<Weather> {
    private SQLiteDatabase database;
    private WeatherDatabaseAdapter weatherDatabaseAdapter;

    public WeatherRepository(Context context) {
        weatherDatabaseAdapter = new WeatherDatabaseAdapter(context);
    }

    @Override
    public void add(Weather object) {
        database = weatherDatabaseAdapter.getDatabase();

        try {
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_CITY_ID, object.getCityID());
            cv.put(COLUMN_CITY_NAME, object.getCityName());
            cv.put(COLUMN_COUNTRY_CODE, object.getCountryCode());
            cv.put(COLUMN_TIME_OF_DATA_FORECAST, object.getTimeOfDataForecast());
            cv.put(COLUMN_FORECAST_DATE, object.getResponseDate());
            cv.put(COLUMN_CURRENT_TEMPERATURE, object.getCurrentTemperature());
            cv.put(COLUMN_PRESSURE, object.getPressure());
            cv.put(COLUMN_HUMIDITY_PERCENT, object.getTimezone());
            cv.put(COLUMN_WEATHER_ID, object.getWeatherID());
            cv.put(COLUMN_WEATHER_GROUP, object.getWeatherGroup());
            cv.put(COLUMN_WEATHER_GROUP_DESCRIPTION, object.getWeatherGroupDescription());
            cv.put(COLUMN_WEATHER_ICON_ID, object.getWeatherIconID());
            cv.put(COLUMN_CLOUDINESS_PERCENT, object.getHumidity());
            cv.put(COLUMN_WIND_SPEED, object.getWindSpeed());
            cv.put(COLUMN_RAIN_VOLUME, object.getRainVolume());
            cv.put(COLUMN_SNOW_VOLUME, object.getSnowVolume());

            database.insert(TABLE_WEATHER, null, cv);
        }
        finally {
            database.close();
        }
    }

    @Override
    public void update(Weather object) {
    }

    @Override
    public void remote(Weather object) {
    }

    @Override
    public ArrayList<Weather> query(SqlSpecification sqlSpecification) {
        final ArrayList<Weather> weathers = new ArrayList<>();
        database = weatherDatabaseAdapter.getDatabase();
        database.beginTransaction();

        try {
            final Cursor cursor = database.rawQuery(sqlSpecification.toSqlQuery(), new String[]{});

            if (cursor.moveToFirst()) {
                do {
                    int cityID = cursor.getInt(cursor.getColumnIndex(COLUMN_CITY_ID));
                    String cityName = cursor.getString(cursor.getColumnIndex(COLUMN_CITY_NAME));
                    String countryCode = cursor.getString(cursor.getColumnIndex(COLUMN_COUNTRY_CODE));
                    String timeOfDataForecast = cursor.getString(cursor.getColumnIndex(COLUMN_TIME_OF_DATA_FORECAST));
                    String forecastDate = cursor.getString(cursor.getColumnIndex(COLUMN_FORECAST_DATE));
                    int currentTemperature = cursor.getInt(cursor.getColumnIndex(COLUMN_CURRENT_TEMPERATURE));
                    int pressure = cursor.getInt(cursor.getColumnIndex(COLUMN_PRESSURE));
                    int humidityPercent = cursor.getInt(cursor.getColumnIndex(COLUMN_HUMIDITY_PERCENT));
                    int weatherID = cursor.getInt(cursor.getColumnIndex(COLUMN_WEATHER_ID));
                    String weatherGroup = cursor.getString(cursor.getColumnIndex(COLUMN_WEATHER_GROUP));
                    String weatherGroupDescription = cursor.getString(cursor.getColumnIndex(COLUMN_WEATHER_GROUP_DESCRIPTION));
                    String weatherIconID = cursor.getString(cursor.getColumnIndex(COLUMN_WEATHER_ICON_ID));
                    int cloudinessPercent = cursor.getInt(cursor.getColumnIndex(COLUMN_CLOUDINESS_PERCENT));
                    double windSpeed = cursor.getDouble(cursor.getColumnIndex(COLUMN_WIND_SPEED));
                    double rainVolume = cursor.getDouble(cursor.getColumnIndex(COLUMN_RAIN_VOLUME));
                    double snowVolume = cursor.getDouble(cursor.getColumnIndex(COLUMN_SNOW_VOLUME));

                    weathers.add(new Weather(cityID, cityName, countryCode, timeOfDataForecast, forecastDate,
                            currentTemperature, pressure, humidityPercent, weatherID, weatherGroup, weatherGroupDescription,
                            weatherIconID, cloudinessPercent, windSpeed, rainVolume, snowVolume));
                }
                while (cursor.moveToNext());
            }
            cursor.close();
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
            database.close();
        }
        return weathers;
    }

}
