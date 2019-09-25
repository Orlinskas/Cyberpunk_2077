package com.orlinskas.cyberpunk.specification;

import android.location.Location;

import static com.orlinskas.cyberpunk.data.CityDatabase.COLUMN_COORD_LAT;
import static com.orlinskas.cyberpunk.data.CityDatabase.COLUMN_COORD_LON;
import static com.orlinskas.cyberpunk.data.CityDatabase.TABLE_CITY;

public class CitiesThisLocationSpecification implements SqlSpecification {
    private Location location;
    private final static double MAX_COORDINATES_DEVIATION = 1.0;

    public CitiesThisLocationSpecification(Location location) {
        this.location = location;
    }

    @Override
    public String toSqlQuery() {
        return String.format(
                "SELECT * FROM %1$s WHERE %2$s BETWEEN %3$s AND %4$s AND %5$s BETWEEN %6$s AND %7$s;",
                TABLE_CITY,
                COLUMN_COORD_LAT,
                location.getLatitude() - MAX_COORDINATES_DEVIATION,
                location.getLatitude() + MAX_COORDINATES_DEVIATION,
                COLUMN_COORD_LON,
                location.getLongitude() - MAX_COORDINATES_DEVIATION,
                location.getLongitude() + MAX_COORDINATES_DEVIATION
        );
    }
}
