package com.orlinskas.cyberpunk.repository;

import android.content.Context;

import com.orlinskas.cyberpunk.Country;
import com.orlinskas.cyberpunk.specification.CountrySpecification;

import java.util.ArrayList;

public class CountryListPresenter {
    private Context context;

    public CountryListPresenter(Context context) {
        this.context = context;
    }

    public ArrayList<Country> present() {
        ArrayList<Country> countries;
        CountryRepository repository = new CountryRepository(context);
        countries = repository.query(new CountrySpecification());
        return cleanList(countries);
    }

    private ArrayList<Country> cleanList(ArrayList<Country> countries) {
        ArrayList<Country> cleanCountryList = new ArrayList<>();

        for (Country country : countries) {
            if(checkValidity(country.getName())) {
                cleanCountryList.add(country);
            }
        }
        return cleanCountryList;
    }

    private boolean checkValidity(String name) {
        if(name == null){
            return false;
        }
        return !name.equals("-");
    }
}
