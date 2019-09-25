package com.orlinskas.cyberpunk.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.orlinskas.cyberpunk.Country;
import com.orlinskas.cyberpunk.data.CountryDatabaseAdapter;
import com.orlinskas.cyberpunk.specification.SqlSpecification;

import java.util.ArrayList;

import static com.orlinskas.cyberpunk.data.CountryDatabase.COLUMN_COUNTRY_CODE;
import static com.orlinskas.cyberpunk.data.CountryDatabase.COLUMN_COUNTRY_NAME;
import static com.orlinskas.cyberpunk.data.CountryDatabase.TABLE_COUNTRY;

public class CountryRepository implements Repository<Country> {
    private SQLiteDatabase database;
    private CountryDatabaseAdapter countryDatabaseAdapter;

    CountryRepository(Context context) {
        countryDatabaseAdapter = new CountryDatabaseAdapter(context);
    }

    @Override
    public void add(Country object) {
        database = countryDatabaseAdapter.getDatabase();
        try {
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_COUNTRY_CODE, object.getCode());
            cv.put(COLUMN_COUNTRY_NAME, object.getName());
            database.insert(TABLE_COUNTRY, null, cv);
        } finally {
            database.close();
        }
    }

    @Override
    public void update(Country object) {
    }

    @Override
    public void remote(Country object) {
        database = countryDatabaseAdapter.getDatabase();
        try {
            String sql = String.format(
                    "DELETE FROM %1$s WHERE %2$s = '%3$s';",
                    TABLE_COUNTRY,
                    COLUMN_COUNTRY_CODE,
                    object.getCode()
            );
            database.execSQL(sql);
        } finally {
            database.close();
        }
    }

    @Override
    public ArrayList<Country> query(SqlSpecification sqlSpecification) {
        final ArrayList<Country> countries = new ArrayList<>();
        database = countryDatabaseAdapter.getDatabase();
        database.beginTransaction();

        try {
            final Cursor cursor = database.rawQuery(sqlSpecification.toSqlQuery(), new String[]{});

            if (cursor.moveToFirst()) {
                do {
                    String code = cursor.getString(cursor.getColumnIndex(COLUMN_COUNTRY_CODE));
                    String name = cursor.getString(cursor.getColumnIndex(COLUMN_COUNTRY_NAME));

                    countries.add(new Country(code, name));
                }while (cursor.moveToNext());
            }
            cursor.close();
            database.setTransactionSuccessful();

        } finally {
            database.endTransaction();
            database.close();
        }
        return countries;
    }
}
