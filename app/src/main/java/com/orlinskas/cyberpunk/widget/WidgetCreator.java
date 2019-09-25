package com.orlinskas.cyberpunk.widget;

import com.orlinskas.cyberpunk.City;
import com.orlinskas.cyberpunk.math.Random;
import com.orlinskas.cyberpunk.request.Request;
import com.orlinskas.cyberpunk.request.RequestBuilder;

public class WidgetCreator {
    public Widget create(City city) {
        int id = Random.getID();

        RequestBuilder builder = new RequestBuilder();
        Request request = builder.build(city);

        return new Widget(id, city, request);
    }
}
