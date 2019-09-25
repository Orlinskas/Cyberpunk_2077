package com.orlinskas.cyberpunk.repository;

import android.content.Context;

import com.orlinskas.cyberpunk.City;
import com.orlinskas.cyberpunk.Country;
import com.orlinskas.cyberpunk.specification.CitiesThisCountrySpecification;

import java.util.ArrayList;

public class CityListPresenter {
    private Context context;

    public CityListPresenter(Context context) {
        this.context = context;
    }

    public ArrayList<City> present(Country country) {
        CityRepository repository = new CityRepository(context);
        return repository.query(new CitiesThisCountrySpecification(country));
    }
}
