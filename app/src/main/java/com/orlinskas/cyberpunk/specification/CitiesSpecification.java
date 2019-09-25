package com.orlinskas.cyberpunk.specification;

import static com.orlinskas.cyberpunk.data.CityDatabase.COLUMN_CITY_NAME;
import static com.orlinskas.cyberpunk.data.CityDatabase.TABLE_CITY;

public class CitiesSpecification implements SqlSpecification {
    @Override
    public String toSqlQuery() {
        return String.format(
                "SELECT * FROM %1$s GROUP BY %2$s;",
                TABLE_CITY,
                COLUMN_CITY_NAME
        );
    }
}
