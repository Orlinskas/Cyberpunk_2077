package com.orlinskas.cyberpunk.ui.home;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.IBinder;
import android.widget.RemoteViewsService;

public class WidgetService extends RemoteViewsService {
    /*
     * So pretty simple just defining the Adapter of the listview
     * here Adapter is ListProvider
     * */
    public WidgetService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        int appWidgetId = intent.getIntExtra(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);

        return (new ConsoleProvider(this.getApplicationContext(), intent));
    }

}