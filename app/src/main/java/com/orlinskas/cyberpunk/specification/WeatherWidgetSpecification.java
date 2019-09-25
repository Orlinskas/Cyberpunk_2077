package com.orlinskas.cyberpunk.specification;

import com.orlinskas.cyberpunk.widget.Widget;

import static com.orlinskas.cyberpunk.data.WeatherDatabase.COLUMN_CITY_ID;
import static com.orlinskas.cyberpunk.data.WeatherDatabase.TABLE_WEATHER;

public class WeatherWidgetSpecification implements SqlSpecification {
    private Widget widget;

    public WeatherWidgetSpecification(Widget widget) {
        this.widget = widget;
    }

    @Override
    public String toSqlQuery() {
        return String.format(
                "SELECT * FROM %1$s WHERE %2$s == %3$s;",
                TABLE_WEATHER,
                COLUMN_CITY_ID,
                widget.getCity().getId()
        );
    }
}
