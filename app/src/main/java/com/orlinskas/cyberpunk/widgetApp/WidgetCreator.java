package com.orlinskas.cyberpunk.widgetApp;

import com.orlinskas.cyberpunk.City;
import com.orlinskas.cyberpunk.request.Request;
import com.orlinskas.cyberpunk.request.RequestBuilder;

public class WidgetCreator {
    public Widget create(City city) {
        int id = WidgetIDGenerator.getID();

        RequestBuilder builder = new RequestBuilder();
        Request request = builder.build(city);

        return new Widget(id, city, request);
    }
}