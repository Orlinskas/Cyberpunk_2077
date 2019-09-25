package com.orlinskas.cyberpunk.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.orlinskas.cyberpunk.City;
import com.orlinskas.cyberpunk.data.CityDatabaseAdapter;
import com.orlinskas.cyberpunk.specification.SqlSpecification;

import java.util.ArrayList;

import static com.orlinskas.cyberpunk.data.CityDatabase.COLUMN_CITY_ID;
import static com.orlinskas.cyberpunk.data.CityDatabase.COLUMN_CITY_NAME;
import static com.orlinskas.cyberpunk.data.CityDatabase.COLUMN_COORD_LAT;
import static com.orlinskas.cyberpunk.data.CityDatabase.COLUMN_COORD_LON;
import static com.orlinskas.cyberpunk.data.CityDatabase.COLUMN_COUNTRY_CODE;
import static com.orlinskas.cyberpunk.data.CityDatabase.TABLE_CITY;

public class CityRepository implements Repository<City> {
    private SQLiteDatabase database;
    private CityDatabaseAdapter cityDatabaseAdapter;

    public CityRepository(Context context) {
        cityDatabaseAdapter = new CityDatabaseAdapter(context);
    }

    @Override
    public void add(City object) {
        database = cityDatabaseAdapter.getDatabase();
        try {
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_CITY_ID, object.getId());
            cv.put(COLUMN_CITY_NAME, object.getName());
            cv.put(COLUMN_COUNTRY_CODE, object.getCountryCode());
            cv.put(COLUMN_COORD_LON, object.getCoordLon());
            cv.put(COLUMN_COORD_LAT, object.getCoordLat());
            database.insert(TABLE_CITY, null, cv);
        }finally {
            database.close();
        }
    }

    @Override
    public void update(City object) {
    }

    @Override
    public void remote(City object) {
        database = cityDatabaseAdapter.getDatabase();
        try {
            String sql = String.format(
                    "DELETE FROM %1$s WHERE %2$s = '%3$s';",
                    TABLE_CITY,
                    COLUMN_CITY_ID,
                    object.getId()
            );
            database.execSQL(sql);
        } finally {
            database.close();
        }
    }

    @Override
    public ArrayList<City> query(SqlSpecification sqlSpecification) {
        final ArrayList<City> cities = new ArrayList<>();
        database = cityDatabaseAdapter.getDatabase();
        database.beginTransaction();
        try {
            try {
                final Cursor cursor = database.rawQuery(sqlSpecification.toSqlQuery(), new String[]{});

                if (cursor.moveToFirst()) {
                    do {
                        int cityID = cursor.getInt(cursor.getColumnIndex(COLUMN_CITY_ID));
                        String cityName = cursor.getString(cursor.getColumnIndex(COLUMN_CITY_NAME));
                        String countryCode = cursor.getString(cursor.getColumnIndex(COLUMN_COUNTRY_CODE));
                        double coordLon = cursor.getDouble(cursor.getColumnIndex(COLUMN_COORD_LON));
                        double coordLat = cursor.getDouble(cursor.getColumnIndex(COLUMN_COORD_LAT));

                        cities.add(new City(cityID, cityName, countryCode, coordLon, coordLat));
                    }
                    while (cursor.moveToNext());
                }
                cursor.close();
                database.setTransactionSuccessful();
            } finally {
                database.endTransaction();
                database.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cities;
    }
}
