package com.orlinskas.cyberpunk.updateWidget;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.widget.RemoteViews;

import com.orlinskas.cyberpunk.R;
import com.orlinskas.cyberpunk.preferences.Preferences;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CountDownUpdateService extends Service {
    private RemoteViews widgetView;
    private CountDownTimer countDownTimer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        widgetView = new RemoteViews(getPackageName(), R.layout.widget_count_down);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int appWidgetID = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        try {
            updateTimer(appWidgetID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void updateTimer(final int appWidgetID) {
        //16.04.2020 дата выхода игры
        long currentTimeMillis = System.currentTimeMillis();
        long releaseTimeMillis = 1586995199000L;
        long millisInFuture = releaseTimeMillis - currentTimeMillis;

        countDownTimer = new CountDownTimer(millisInFuture, 1000) {

            @Override
            public void onTick(final long millisUntilFinished) {
                Runnable runnable = new Runnable() {
                    public void run() {
                        String timerValue = buildTimerValue(millisUntilFinished);
                        widgetView.setImageViewBitmap(R.id.widget_count_down_iv, convertToImg(timerValue));
                        AppWidgetManager.getInstance(getApplicationContext()).updateAppWidget(appWidgetID, widgetView);
                    }
                };
                runnable.run();
            }

            @Override
            public void onFinish() {
                widgetView.setTextViewText(R.id.widget_count_down_iv, "Game out!");
                AppWidgetManager.getInstance(getApplicationContext()).updateAppWidget(appWidgetID, widgetView);
            }
        }.start();

        Preferences preferences = Preferences.getInstance(getApplicationContext(), Preferences.WIDGET_SETTINGS);
        preferences.saveData(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetID);
    }

    private String buildTimerValue(long millisUntilFinished) {
        StringBuilder dateToRelease = new StringBuilder();
        //1 day = 86400000 millis
        long DD = millisUntilFinished / 86400000L;
        SimpleDateFormat commonFormat = new SimpleDateFormat(": HH: mm:ss", Locale.ENGLISH);
        String HH_MM_SS = commonFormat.format(new Date(millisUntilFinished));
        dateToRelease.append(DD).append(HH_MM_SS);

        return dateToRelease.toString();
    }

    public Bitmap convertToImg(String text) {
        Bitmap btmText = Bitmap.createBitmap(200, 50, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(btmText);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setSubpixelText(true);
        paint.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/digit_1.TTF"));
        paint.setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorCyberpunkYellow));
        paint.setTextSize(30);
        paint.setShadowLayer(6,0,0, ContextCompat.getColor(getApplicationContext(), R.color.colorCyberpunkGreen));
        paint.setTextAlign(Paint.Align.CENTER);

        int xPos = (canvas.getWidth() / 2);
        int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2)) ;

        canvas.drawText(text, xPos, yPos, paint);

        return btmText;
    }

    @Override
    public void onDestroy() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        super.onDestroy();
    }
}
