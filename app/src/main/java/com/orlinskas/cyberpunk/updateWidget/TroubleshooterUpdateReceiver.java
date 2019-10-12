package com.orlinskas.cyberpunk.updateWidget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import static com.orlinskas.cyberpunk.updateWidget.Settings.MY_WIDGET_ID;

public class TroubleshooterUpdateReceiver extends BroadcastReceiver {
    public static final String UPDATE_TROUBLESHOOTER = "updateTroubleshooter";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && intent.getAction() != null) {
            int myWidgetID = intent.getIntExtra(MY_WIDGET_ID,0);

            switch (intent.getAction()) {
                case UPDATE_TROUBLESHOOTER:
                case Intent.ACTION_BOOT_COMPLETED:
                    Intent intentService = new Intent(context, TroubleshooterUpdateService.class);
                    intentService.putExtra(MY_WIDGET_ID, myWidgetID);
                    context.startService(intentService);
                    break;
            }
        }
    }
}
