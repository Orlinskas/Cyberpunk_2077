package com.orlinskas.cyberpunk.widgetHomeCountDown;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

public class CountDownWidget extends AppWidgetProvider {

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        String action = intent.getAction();
        int appWidgetID = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

        if (action != null) {
            switch (action){
                case AppWidgetManager.ACTION_APPWIDGET_ENABLED:
                case AppWidgetManager.ACTION_APPWIDGET_RESTORED:
                case AppWidgetManager.ACTION_APPWIDGET_OPTIONS_CHANGED:
                case AppWidgetManager.ACTION_APPWIDGET_UPDATE:
                    if(appWidgetID != AppWidgetManager.INVALID_APPWIDGET_ID){
                        CountDownServiceRunner.start(appWidgetID, context);
                    }
                    break;
                case AppWidgetManager.ACTION_APPWIDGET_DISABLED:
                        CountDownServiceRunner.stop(context);
                    break;
            }
        }
    }
}
