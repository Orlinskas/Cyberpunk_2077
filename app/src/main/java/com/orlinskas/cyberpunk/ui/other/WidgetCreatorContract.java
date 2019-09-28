package com.orlinskas.cyberpunk.ui.other;

import com.orlinskas.cyberpunk.City;
import com.orlinskas.cyberpunk.Country;
import com.orlinskas.cyberpunk.widget.Widget;

public interface WidgetCreatorContract {
    int STATUS_ON = 1241;
    int STATUS_OFF = 1313;
    int BUTTON_OPEN_LIST_ACTIVITY = 123;
    int BUTTON_START_SEARCH_LOCATION = 321;
    int BUTTON_CREATE_WIDGET = 213;
    int EMPTY_WIDGET = 13;

    interface View {
        void setCountry(Country country);
        void setCity(City city);
        void setStatusIndicatorGPS(int STATUS);
        void setStatusIndicatorINTERNET(int STATUS);
        void setButtonStatus(int BUTTON, int STATUS);
        void setProgressImage(int KEY);
        void openActivityForResult(Class activity);
        void openWidgetActivity(Class activity);
        void toAskGPSPermission();
        void toAskEnableGPS();
        void doToast(String message);
        void doSnackBar(String message);
    }

    interface Presenter {
        void startWork();
        void onClickChooseLocation();
        void onClickSearchLocation();
        void onClickCreateWidget(Country country, City city);
        void onResult(Widget widget);
        void startSearchLocation();
        void startProgressBar();
        void stopProgressBar();
        void destroy();
    }

    interface Model {
        void createWidget(Country country, City city);
    }
}
