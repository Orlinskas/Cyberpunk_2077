package com.orlinskas.cyberpunk.ui.home;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.orlinskas.cyberpunk.homeWidget.CountDownServiceRunner;
import com.orlinskas.cyberpunk.updateWidget.CountDownReceiver;

public class WidgetCountDown extends AppWidgetProvider {

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        String action = intent.getAction();
        int appWidgetID = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

        if (action != null) {
            switch (action){
                case AppWidgetManager.ACTION_APPWIDGET_ENABLED:
                    registerReceiver(context);
                case AppWidgetManager.ACTION_APPWIDGET_RESTORED:
                case AppWidgetManager.ACTION_APPWIDGET_OPTIONS_CHANGED:
                case AppWidgetManager.ACTION_APPWIDGET_UPDATE:
                    if(appWidgetID != AppWidgetManager.INVALID_APPWIDGET_ID){
                        CountDownServiceRunner.start(appWidgetID, context);
                    }
                    break;
                case AppWidgetManager.ACTION_APPWIDGET_DISABLED:
                        unregisterReceiver(context);
                        CountDownServiceRunner.stop(context);
                    break;
            }
        }
    }

    private void registerReceiver(Context context) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        context.getApplicationContext().registerReceiver(new CountDownReceiver(), filter);
    }

    private void unregisterReceiver(Context context) {
        context.getApplicationContext().unregisterReceiver(new CountDownReceiver());
    }
}
