package com.orlinskas.cyberpunk.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class CountryDatabaseAdapter {
    private CountryDatabase countryDatabase;
    private static String DATABASE_PATH;

    public CountryDatabaseAdapter(Context context) {
        countryDatabase = new CountryDatabase(context);
        DATABASE_PATH = context.getFilesDir().getPath() + CountryDatabase.DATABASE_NAME;
    }

    public void createDatabase() {
        countryDatabase.create_db();
    }

    public SQLiteDatabase getDatabase(){
        return SQLiteDatabase.openDatabase(DATABASE_PATH, null, SQLiteDatabase.OPEN_READWRITE);
    }
}
