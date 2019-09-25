package com.orlinskas.cyberpunk.forecast;

import android.content.Context;

import com.orlinskas.cyberpunk.request.Request;
import com.orlinskas.cyberpunk.request.RequestURLGenerator;
import com.orlinskas.cyberpunk.request.RequestURLSender;
import com.orlinskas.cyberpunk.response.JSONResponseParserToWeather;
import com.orlinskas.cyberpunk.response.WeatherRepositoryWriter;

import java.net.URL;
import java.util.ArrayList;

public class WeatherReceiver {
    private Context context;
    private Request request;

    public WeatherReceiver(Context context, Request request) {
        this.context = context;
        this.request = request;
    }

    public void receive() throws Exception{
        RequestURLGenerator urlGenerator = new RequestURLGenerator();
        URL requestURL = urlGenerator.generate(request);
        RequestURLSender urlSender = new RequestURLSender();
        String response = urlSender.send(requestURL);
        JSONResponseParserToWeather parser = new JSONResponseParserToWeather();
        ArrayList<Weather> weathers = parser.parse(response);
        WeatherRepositoryWriter repositoryWriter = new WeatherRepositoryWriter(context);
        repositoryWriter.write(weathers);
    }
}
