package com.orlinskas.cyberpunk.widgetHomeSmallWeather;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import static com.orlinskas.cyberpunk.Settings.MY_WIDGET_ID;

public class SmallWeatherUpdateReceiver extends BroadcastReceiver {
    public static final String UPDATE_SMALL_WEATHER = "updateSmallWeather";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && intent.getAction() != null) {
            int myWidgetID = intent.getIntExtra(MY_WIDGET_ID,0);

            switch (intent.getAction()) {
                case Intent.ACTION_BOOT_COMPLETED:
                case UPDATE_SMALL_WEATHER:
                    Intent intentServiceSmallWeather = new Intent(context, SmallWeatherUpdateService.class);
                    intentServiceSmallWeather.putExtra(MY_WIDGET_ID, myWidgetID);
                    context.startService(intentServiceSmallWeather);
                    break;
            }
        }
    }
}
