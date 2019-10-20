package com.orlinskas.cyberpunk.updateWidget;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.orlinskas.cyberpunk.ToastBuilder;
import com.orlinskas.cyberpunk.date.DateFormat;
import com.orlinskas.cyberpunk.date.DateHelper;
import com.orlinskas.cyberpunk.forecast.ForecastListBuilder;
import com.orlinskas.cyberpunk.forecast.WeatherReceiver;
import com.orlinskas.cyberpunk.preferences.Preferences;
import com.orlinskas.cyberpunk.ui.home.WidgetTroubleshooter;
import com.orlinskas.cyberpunk.widget.Widget;
import com.orlinskas.cyberpunk.widget.WidgetRepository;

import static com.orlinskas.cyberpunk.updateWidget.Settings.MY_WIDGET_ID;
import static com.orlinskas.cyberpunk.preferences.Preferences.APP_WIDGET_ID_DEPENDS;
import static com.orlinskas.cyberpunk.preferences.Preferences.WIDGET_LAST_UPDATE;

public class TroubleshooterUpdateService extends Service {
    private int myWidgetID;
    private Context context;

    public TroubleshooterUpdateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        context = getBaseContext();
        myWidgetID = intent.getIntExtra(MY_WIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        if(myWidgetID == AppWidgetManager.INVALID_APPWIDGET_ID) {
            stopSelf();
        }
        else {
            updateTask();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void updateTask() {
        new Thread(new Runnable() {
            public void run() {
                WidgetUpdateChecker updateChecker = new WidgetUpdateChecker(myWidgetID, context);
                if(updateChecker.check()) {
                    try {
                        Widget widget = findWidgetInRepo(myWidgetID);
                        sendRequest(widget);
                        updateForecastInWidget(widget);
                        updateWidgetInRepository(widget);
                        saveWidgetUpdateDate(widget);
                        sendIntentToUpdate(myWidgetID);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                stopSelf();
            }
        }).start();
    }

    private Widget findWidgetInRepo(int myWidgetID) {
        Widget widget = null;
        WidgetRepository repository = new WidgetRepository(context);
        try {
            widget = repository.find(myWidgetID);
        } catch (Exception e) {
            ToastBuilder.create(getBaseContext(),"Critical error, reinstall");
            e.printStackTrace();
        }
        return widget;
    }

    private void sendRequest(Widget widget) throws Exception {
        WeatherReceiver receiver = new WeatherReceiver(context, widget.getRequest());
        receiver.receive();
    }

    private void updateForecastInWidget(Widget widget) {
        ForecastListBuilder forecastsBuilder = new ForecastListBuilder(widget, context);
        widget.setDaysForecast(forecastsBuilder.build());
    }

    private void updateWidgetInRepository(Widget widget) throws Exception {
        WidgetRepository repository = new WidgetRepository(context);
        repository.update(widget);
    }

    private void saveWidgetUpdateDate(Widget widget) {
        String id = String.valueOf(widget.getId());
        String currentDate = DateHelper.getCurrent(DateFormat.YYYY_MM_DD_HH_MM);

        Preferences preferences = Preferences.getInstance(context, Preferences.SETTINGS);
        preferences.saveData(WIDGET_LAST_UPDATE + id, currentDate);
    }

    private void sendIntentToUpdate(int myWidgetID) {
        Preferences preferences = Preferences.getInstance(context, Preferences.SETTINGS);
        int appWidgetID = preferences.getData(APP_WIDGET_ID_DEPENDS + myWidgetID, AppWidgetManager.INVALID_APPWIDGET_ID);

        Intent update = new Intent(context, WidgetTroubleshooter.class);
        update.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetID);
        update.putExtra(WidgetTroubleshooter.ACTION, WidgetTroubleshooter.ACTION_UPDATE);
        PendingIntent pRightIntent = PendingIntent.getBroadcast(context, appWidgetID + 410, update, 0);
        try {
            pRightIntent.send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }
}
