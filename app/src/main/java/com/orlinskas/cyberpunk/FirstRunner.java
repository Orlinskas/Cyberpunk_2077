package com.orlinskas.cyberpunk;

import android.content.Context;

import com.orlinskas.cyberpunk.data.CityDatabaseAdapter;
import com.orlinskas.cyberpunk.data.CountryDatabaseAdapter;

public class FirstRunner {
    private Context context;

    public FirstRunner(Context context) {
        this.context = context;
    }

    public void doFirstRun() {
        createCityDatabase();
        createCountryDatabase();
    }

    private void createCountryDatabase() {
        CountryDatabaseAdapter countryDatabaseAdapter = new CountryDatabaseAdapter(context);
        countryDatabaseAdapter.createDatabase();
        countryDatabaseAdapter.getDatabase().close();
    }

    private void createCityDatabase() {
        CityDatabaseAdapter cityDatabaseAdapter = new CityDatabaseAdapter(context);
        cityDatabaseAdapter.createDatabase();
        cityDatabaseAdapter.getDatabase().close();
    }
}
