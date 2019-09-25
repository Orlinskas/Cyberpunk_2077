package com.orlinskas.cyberpunk.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class WeatherDatabaseAdapter {
    private WeatherDatabase weatherDatabase;

    public WeatherDatabaseAdapter(Context context) {
        weatherDatabase = new WeatherDatabase(context);
    }

    public SQLiteDatabase getDatabase(){
        return weatherDatabase.getWritableDatabase();
    }
}