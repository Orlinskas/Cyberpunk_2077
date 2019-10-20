package com.orlinskas.cyberpunk.ui.home;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.orlinskas.cyberpunk.updateWidget.CountDownReceiver;
import com.orlinskas.cyberpunk.updateWidget.CountDownUpdateService;

public class WidgetCountDown extends AppWidgetProvider {
    private boolean isServiceStart = false;
    private boolean isReceiverRegister = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        String action = intent.getAction();
        int appWidgetID = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

        if (action != null) {
            switch (action){
                case AppWidgetManager.ACTION_APPWIDGET_OPTIONS_CHANGED:
                case AppWidgetManager.ACTION_APPWIDGET_UPDATE:
                    if(appWidgetID != AppWidgetManager.INVALID_APPWIDGET_ID){
                        createWidget(appWidgetID, context);
                    }
                    break;
                case AppWidgetManager.ACTION_APPWIDGET_DELETED:
                    if(appWidgetID != AppWidgetManager.INVALID_APPWIDGET_ID){
                        stopServiceAndUnregisterReceiver(context);
                    }
                    break;
            }
        }
    }

    private void createWidget(int appWidgetID, Context context) {
        if(!isServiceStart){
            Intent intentService = new Intent(context, CountDownUpdateService.class);
            intentService.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetID);
            context.getApplicationContext().startService(intentService);
            isServiceStart = true;
        }
        if(!isReceiverRegister){
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_SCREEN_ON);
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            context.getApplicationContext().registerReceiver(new CountDownReceiver(), filter);
            isReceiverRegister = true;
        }
    }

    private void stopServiceAndUnregisterReceiver(Context context) {
        if(isServiceStart){
            Intent stopIntentService = new Intent(context, CountDownUpdateService.class);
            context.getApplicationContext().stopService(stopIntentService);
            isServiceStart = false;
        }
        if(isReceiverRegister){
            context.getApplicationContext().unregisterReceiver(new CountDownReceiver());
            isReceiverRegister = false;
        }
    }
}
