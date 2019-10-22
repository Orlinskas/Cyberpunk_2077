package com.orlinskas.cyberpunk.specification;

import com.orlinskas.cyberpunk.widgetApp.Widget;

import static com.orlinskas.cyberpunk.data.WeatherDatabase.COLUMN_CITY_ID;
import static com.orlinskas.cyberpunk.data.WeatherDatabase.COLUMN_TIME_OF_DATA_FORECAST;
import static com.orlinskas.cyberpunk.data.WeatherDatabase.TABLE_WEATHER;

public class WeatherDaySpecification implements SqlSpecification {
    private Widget widget;
    private String needDate;

    public WeatherDaySpecification(Widget widget, String needDate) {
        this.widget = widget;
        this.needDate = needDate;
    }

    @Override
    public String toSqlQuery() {
        return String.format(
                "SELECT * FROM %1$s WHERE %2$s == %3$s AND %4$s LIKE '%5$s';",
                TABLE_WEATHER,
                COLUMN_CITY_ID,
                widget.getCity().getId(),
                COLUMN_TIME_OF_DATA_FORECAST,
                needDate + "______"
        );
    }
}
