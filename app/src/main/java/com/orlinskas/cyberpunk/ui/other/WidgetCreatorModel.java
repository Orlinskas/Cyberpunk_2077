package com.orlinskas.cyberpunk.ui.other;

import android.content.Context;

import com.orlinskas.cyberpunk.City;
import com.orlinskas.cyberpunk.Country;
import com.orlinskas.cyberpunk.request.Request;
import com.orlinskas.cyberpunk.widget.Widget;
import com.orlinskas.cyberpunk.widget.WidgetCopyChecker;
import com.orlinskas.cyberpunk.widget.WidgetCreator;
import com.orlinskas.cyberpunk.widget.WidgetRepository;

public class WidgetCreatorModel implements WidgetCreatorContract.Model {
    private Context context;
    private WidgetCreatorContract.Presenter presenter;
    private Widget emptyWidget;

    WidgetCreatorModel(Context context, WidgetCreatorContract.Presenter presenter) {
        this.context = context;
        this.presenter = presenter;
        City emptyCity = new City(404, "Not found", "Need to create", 1.0, 1.0);
        emptyWidget = new Widget(0, emptyCity, new Request("n/a", emptyCity, "n/a", "n/a" , "n/a", "n/a"));
    }

    @Override
    public void createWidget(Country country, City city) {
        try {
            WidgetCreator widgetCreator = new WidgetCreator();
            WidgetRepository widgetRepository = new WidgetRepository(context);
            Widget widget = widgetCreator.create(city);
            WidgetCopyChecker checker = new WidgetCopyChecker(context, widget);
            if(!checker.isHasCopy() && isCorrectnessWidgetData(country, city)) {
                widgetRepository.add(widget);
                presenter.onResult(widget);
            }
            else {
                presenter.onResult(emptyWidget);
            }
        } catch (Exception e) {
            e.printStackTrace();
            presenter.onResult(emptyWidget);
        }
    }

    private boolean isCorrectnessWidgetData(Country country, City city) {
        if(city != null && country != null) {
            return city.getCountryCode().equals(country.getCode());
        }
        else {
            return false;
        }
    }
}
