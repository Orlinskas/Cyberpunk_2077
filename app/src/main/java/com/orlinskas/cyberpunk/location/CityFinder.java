package com.orlinskas.cyberpunk.location;

import android.content.Context;
import android.location.Location;

import com.orlinskas.cyberpunk.City;
import com.orlinskas.cyberpunk.repository.CityRepository;
import com.orlinskas.cyberpunk.specification.CitiesThisLocationSpecification;

import java.util.ArrayList;

public class CityFinder {
    private Context context;
    private Location location;

    public CityFinder(Context context, Location location) {
        this.context = context;
        this.location = location;
    }

    public City find() {
        CityRepository repository = new CityRepository(context);
        ArrayList<City> cities = repository.query(new CitiesThisLocationSpecification(location));

        City nearCity = null;
        double distanceToNearCity = 99999.0;

        for (City city : cities) {

            double currentDistance = calculate(location, city);

            if (distanceToNearCity > currentDistance) {
                nearCity = city;
                distanceToNearCity = currentDistance;
            }
        }
        return nearCity;
    }

    private double calculate(Location location, City city) {
        double PK = (180 / Math.PI);

        double a1 = location.getLatitude() / PK;
        double a2 = location.getLongitude() / PK;

        double b1 = city.getCoordLat() / PK;
        double b2 = city.getCoordLon() / PK;

        double t1 = Math.cos(a1) * Math.cos(a2) * Math.cos(b1) * Math.cos(b2);
        double t2 = Math.cos(a1) * Math.sin(a2) * Math.cos(b1) * Math.sin(b2);
        double t3 = Math.sin(a1) * Math.sin(b1);

        double tt = Math.acos(t1 + t2 + t3);

        return 6366000 * tt;
    }
}
