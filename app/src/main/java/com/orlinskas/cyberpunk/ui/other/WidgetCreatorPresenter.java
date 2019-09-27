package com.orlinskas.cyberpunk.ui.other;

import android.content.Context;

import com.orlinskas.cyberpunk.City;
import com.orlinskas.cyberpunk.Country;
import com.orlinskas.cyberpunk.post.CountryNameWriter;
import com.orlinskas.cyberpunk.widget.Widget;

public class WidgetCreatorPresenter implements WidgetCreatorContract.Presenter {
    private Context context;
    private WidgetCreatorContract.Model model;
    private WidgetCreatorContract.View view;

    WidgetCreatorPresenter(Context context, WidgetCreatorContract.View view) {
        this.view = view;
        this.context = context;
    }

    @Override
    public void startWork() {
        model = new WidgetCreatorModel(context, this);
    }

    @Override
    public void createWidget(City city) {
        model.createWidget(city);
    }

    @Override
    public void startSearchLocation() {

    }

    @Override
    public void showResult(Widget widget) {
        if(widget != null && widget.getId() != 0) {
            view.setCity(widget.getCity());
            view.setCountry(findCountry(widget));
        }
        else {
            view.doSnackBar("Some data is incorrect");
        }
    }

    private Country findCountry(Widget widget) {
        CountryNameWriter nameWriter = new CountryNameWriter();
        City city = widget.getCity();
        return new Country(city.getCountryCode(), nameWriter.findNameWith(city.getCountryCode()));
    }

    @Override
    public void destroy() {
        context = null;
    }
}
