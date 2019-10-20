package com.orlinskas.cyberpunk.updateWidget;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.orlinskas.cyberpunk.preferences.Preferences;

public class CountDownReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && intent.getAction() != null) {
            Preferences preferences = Preferences.getInstance(context, Preferences.WIDGET_SETTINGS);
            int widgetID = preferences.getData(AppWidgetManager.EXTRA_APPWIDGET_ID,0);

            switch (intent.getAction()) {
                case Intent.ACTION_BOOT_COMPLETED:
                case Intent.ACTION_SCREEN_ON:
                    if(widgetID != AppWidgetManager.INVALID_APPWIDGET_ID) {
                        Intent startIntentService = new Intent(context, CountDownUpdateService.class);
                        startIntentService.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,widgetID);
                        context.getApplicationContext().startService(startIntentService);
                    }
                    break;
                case Intent.ACTION_SCREEN_OFF:
                    Intent stopIntentService = new Intent(context, CountDownUpdateService.class);
                    context.getApplicationContext().stopService(stopIntentService);
                    break;
            }
        }
    }
}

