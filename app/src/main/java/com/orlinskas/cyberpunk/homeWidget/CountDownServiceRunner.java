package com.orlinskas.cyberpunk.homeWidget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;

import com.orlinskas.cyberpunk.updateWidget.CountDownUpdateService;

public class CountDownServiceRunner {

    public static void start(int appWidgetID, Context context) {
        Intent intentService = new Intent(context, CountDownUpdateService.class);
        intentService.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetID);
        context.getApplicationContext().startService(intentService);
    }

    public static void stop(Context context) {
        Intent stopIntentService = new Intent(context, CountDownUpdateService.class);
        context.getApplicationContext().stopService(stopIntentService);
    }
}
