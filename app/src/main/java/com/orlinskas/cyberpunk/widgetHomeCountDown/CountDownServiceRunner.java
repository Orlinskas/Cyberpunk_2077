package com.orlinskas.cyberpunk.widgetHomeCountDown;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;

class CountDownServiceRunner {

    static void start(int appWidgetID, Context context) {
        Intent intentService = new Intent(context, CountDownTimerUpdateService.class);
        intentService.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetID);
        context.getApplicationContext().startService(intentService);
    }

    static void stop(Context context) {
        Intent stopIntentService = new Intent(context, CountDownTimerUpdateService.class);
        context.getApplicationContext().stopService(stopIntentService);
    }
}
