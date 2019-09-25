package com.orlinskas.cyberpunk.background;

import android.content.Context;

import com.orlinskas.cyberpunk.date.DateCalculator;
import com.orlinskas.cyberpunk.date.DateFormat;
import com.orlinskas.cyberpunk.date.DateHelper;
import com.orlinskas.cyberpunk.preferences.Preferences;
import com.orlinskas.cyberpunk.widget.Widget;
import com.orlinskas.cyberpunk.widget.WidgetRepository;

import java.util.Date;

import static com.orlinskas.cyberpunk.background.Settings.NEED_HOURS_TO_UPDATE;
import static com.orlinskas.cyberpunk.preferences.Preferences.WIDGET_LAST_UPDATE;

public class WidgetUpdateChecker {
    private int widgetID;
    private Context context;

    public WidgetUpdateChecker(int widgetID, Context context) {
        this.context = context;
        this.widgetID = widgetID;
    }

    public boolean check() {
        Widget widget = findWidgetInRepo(widgetID);

        String lastUpdate;

        if(widget != null) {
            lastUpdate = getLastUpdate(widget);
        }
        else {
            return false;
        }

        Date lastUpdateDate = DateHelper.parse(lastUpdate, DateFormat.YYYY_MM_DD_HH_MM);
        Date currentDate = DateHelper.getCurrentDate(DateFormat.YYYY_MM_DD_HH_MM);

        DateCalculator calculator = new DateCalculator();
        int hours = calculator.calculateDifferencesInHours(lastUpdateDate, currentDate);

        if(widget.getDaysForecast() == null) { //значит данные пустые, из за ошибки, но дата обновления записана
            return true;                       //нужно запустить обновление и получить данные несмотря на дату
        }
        else {
            return hours >= NEED_HOURS_TO_UPDATE; //обновляем если наступило время
        }
    }

    private Widget findWidgetInRepo(int widgetID) {
        Widget widget = null;
        WidgetRepository repository = new WidgetRepository(context);
        try {
            widget = repository.find(widgetID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return widget;
    }

    private String getLastUpdate(Widget widget) {
        String id = String.valueOf(widget.getId());
        Preferences preferences = Preferences.getInstance(context, Preferences.SETTINGS);

        return preferences.getData(WIDGET_LAST_UPDATE + id,"1996-01-22 15:00");
    }
}
