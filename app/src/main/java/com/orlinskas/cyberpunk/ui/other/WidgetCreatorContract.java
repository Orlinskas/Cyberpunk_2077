package com.orlinskas.cyberpunk.ui.other;

import android.content.Context;
import android.widget.LinearLayout;

import com.github.mikephil.charting.charts.LineChart;
import com.orlinskas.cyberpunk.City;
import com.orlinskas.cyberpunk.Country;
import com.orlinskas.cyberpunk.forecast.InstrumentPerformance;
import com.orlinskas.cyberpunk.widget.Widget;

public interface WidgetCreatorContract {

    interface View {
        void setCountry(Country country);
        void setCity(City city);
        void setStatusIndicatorGPS(int status);
        void setStatusIndicatorINTERNET(int status);
        void startAnimationButton(int button);
        void startProgressBar();
        void stopProgressBar();
        void doToast(String message);
        void doSnackBar(String message);
        int STATUS_ON = 1;
        int STATUS_OFF = 0;
        int BUTTON_OPEN_LIST_ACTIVITY = 123;
        int BUTTON_START_SEARCH_LOCATION = 321;
        int BUTTON_CREATE_WIDGET = 213;
    }

    interface Presenter {
        void startWork();
        void createWidget(City city);
        void showResult(Widget widget);
        void startSearchLocation();
        void destroy();
    }

    interface Model {
        void createWidget(City city);

    }
}
