package com.orlinskas.cyberpunk.specification;

import static com.orlinskas.cyberpunk.data.WeatherDatabase.TABLE_WEATHER;

public class WeatherSpecification implements SqlSpecification {
    @Override
    public String toSqlQuery() {
        return String.format(
                "SELECT DISTINCT * FROM %1$s;",
                TABLE_WEATHER
        );
    }
}
