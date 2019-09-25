package com.orlinskas.cyberpunk.background;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.util.Calendar;

import static com.orlinskas.cyberpunk.background.Settings.ALARM_INTERVAL_MINUTES;
import static com.orlinskas.cyberpunk.background.Settings.MY_WIDGET_ID;
import static com.orlinskas.cyberpunk.background.UpdateReceiver.UPDATE;

public class AlarmManagerSetter {
    public void setAlarm(Context context, int myWidgetID) {
        AlarmManager alarm = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, UpdateReceiver.class);
        intent.setAction(UPDATE);
        intent.putExtra(MY_WIDGET_ID, myWidgetID);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, myWidgetID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar timeToRepeat = Calendar.getInstance();
        timeToRepeat.setTimeInMillis(System.currentTimeMillis());
        timeToRepeat.add(Calendar.MINUTE, ALARM_INTERVAL_MINUTES);

        if (Build.VERSION.SDK_INT >= 23) {
            alarm.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeToRepeat.getTimeInMillis(), pendingIntent);
        } else if (Build.VERSION.SDK_INT >= 19) {
            alarm.setExact(AlarmManager.RTC_WAKEUP, timeToRepeat.getTimeInMillis(), pendingIntent);
        } else {
            alarm.set(AlarmManager.RTC_WAKEUP, timeToRepeat.getTimeInMillis(), pendingIntent);
        }
    }
}
