package com.orlinskas.cyberpunk.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WeatherDatabase extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "Forecasts.db";
    public static String TABLE_WEATHER = "WeatherTable";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_CITY_ID = "cityID";
    public static final String COLUMN_CITY_NAME = "cityName";
    public static final String COLUMN_COUNTRY_CODE = "countryCode";
    public static final String COLUMN_TIME_OF_DATA_FORECAST = "timeOfDataForecast";
    public static final String COLUMN_FORECAST_DATE = "forecastDate";
    public static final String COLUMN_CURRENT_TEMPERATURE = "currentTemperature";
    public static final String COLUMN_PRESSURE = "pressure";
    public static final String COLUMN_HUMIDITY_PERCENT = "humidityPercent";
    public static final String COLUMN_WEATHER_ID = "weatherID";
    public static final String COLUMN_WEATHER_GROUP = "weatherGroup";
    public static final String COLUMN_WEATHER_GROUP_DESCRIPTION = "weatherGroupDescription";
    public static final String COLUMN_WEATHER_ICON_ID = "weatherIconID";
    public static final String COLUMN_CLOUDINESS_PERCENT = "cloudinessPercent";
    public static final String COLUMN_WIND_SPEED = "windSpeed";
    public static final String COLUMN_RAIN_VOLUME = "rainVolume";
    public static final String COLUMN_SNOW_VOLUME = "snowVolume";

    WeatherDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_TABLE_WEATHER = "CREATE TABLE " + TABLE_WEATHER + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_CITY_ID + " INTEGER NOT NULL , "
                + COLUMN_CITY_NAME + " TEXT , "
                + COLUMN_COUNTRY_CODE + " TEXT , "
                + COLUMN_TIME_OF_DATA_FORECAST + " TEXT NOT NULL , "
                + COLUMN_FORECAST_DATE + " TEXT NOT NULL , "
                + COLUMN_CURRENT_TEMPERATURE + " INTEGER NOT NULL , "
                + COLUMN_PRESSURE + " INTEGER NOT NULL DEFAULT 0 , "
                + COLUMN_HUMIDITY_PERCENT + " INTEGER NOT NULL DEFAULT 0 , "
                + COLUMN_WEATHER_ID + " INTEGER , "
                + COLUMN_WEATHER_GROUP + " TEXT , "
                + COLUMN_WEATHER_GROUP_DESCRIPTION + " TEXT , "
                + COLUMN_WEATHER_ICON_ID + " TEXT NOT NULL , "
                + COLUMN_CLOUDINESS_PERCENT + " INTEGER NOT NULL DEFAULT 0 , "
                + COLUMN_WIND_SPEED + " REAL NOT NULL DEFAULT 0 , "
                + COLUMN_RAIN_VOLUME + " INTEGER NOT NULL DEFAULT 0 , "
                + COLUMN_SNOW_VOLUME + " INTEGER NOT NULL DEFAULT 0 );";

        db.execSQL(SQL_CREATE_TABLE_WEATHER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WEATHER);
        onCreate(db);
    }
}
