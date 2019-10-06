package com.orlinskas.cyberpunk.ui.home;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.RemoteViews;

import com.orlinskas.cyberpunk.R;
import com.orlinskas.cyberpunk.background.WidgetUpdateService;
import com.orlinskas.cyberpunk.chart.WeatherIconsSelector;
import com.orlinskas.cyberpunk.date.DateCalculator;
import com.orlinskas.cyberpunk.date.DateFormat;
import com.orlinskas.cyberpunk.date.DateHelper;
import com.orlinskas.cyberpunk.forecast.Forecast;
import com.orlinskas.cyberpunk.forecast.Weather;
import com.orlinskas.cyberpunk.preferences.Preferences;
import com.orlinskas.cyberpunk.widget.Widget;
import com.orlinskas.cyberpunk.widget.WidgetRepository;

import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.orlinskas.cyberpunk.background.Settings.MY_WIDGET_ID;
import static com.orlinskas.cyberpunk.preferences.Preferences.MY_WIDGET_ID_DEPENDS;

public class HomeWidget extends AppWidgetProvider {
    public static final String ACTION = "action";
    public static final String ACTION_CREATE = "create";
    public static final String ACTION_DEFAULT = "default";
    public static final String ACTION_UPDATE = "update";
    private final String ACTION_CLICK_CENTER = "centerAreaClick";
    private Typeface tfZelec;
    private Typeface tfDigit;


    @Override
    public IBinder peekService(Context myContext, Intent service) {
        return super.peekService(myContext, service);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        for (int id : appWidgetIds) {
            updateWidget(id, context);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        int appWidgetID = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        String actionExtra = ACTION_DEFAULT;

        if(intent.hasExtra(ACTION)) {
            actionExtra = intent.getStringExtra(ACTION);
        }

        if(appWidgetID != AppWidgetManager.INVALID_APPWIDGET_ID) {

            switch (actionExtra) {
                case ACTION_CREATE:
                case ACTION_DEFAULT:
                case ACTION_UPDATE:
                    updateWidget(appWidgetID, context);
                case ACTION_CLICK_CENTER:
                    UpdateWidgetTask task = new UpdateWidgetTask(context, appWidgetID);
                    task.execute();
                    break;
            }
        }
    }

    @Override
    public void onRestored(Context context, int[] oldWidgetIds, int[] newWidgetIds) {
        super.onRestored(context, oldWidgetIds, newWidgetIds);
        Preferences preferences = Preferences.getInstance(context, Preferences.SETTINGS);

        for(int i = 0; i < oldWidgetIds.length; i++) {
            Widget widget = findWidget(oldWidgetIds[i], context);
            if (widget != null) {
                preferences.saveData(MY_WIDGET_ID_DEPENDS + newWidgetIds[i], widget.getId());
            }
        }
    }

    public void updateWidget(int appWidgetID, Context context) {
        RemoteViews widgetView = new RemoteViews(context.getPackageName(), R.layout.widget_troubleshooter);

        Intent updateClickIntent = new Intent(context, HomeWidget.class);
        updateClickIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        updateClickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetID);
        updateClickIntent.putExtra(ACTION, ACTION_CLICK_CENTER);
        PendingIntent pUpdateIntent = PendingIntent.getBroadcast(context, appWidgetID + 300, updateClickIntent, 0);

        widgetView.setOnClickPendingIntent(R.id.widget_troubleshooter_im_update_btn, pUpdateIntent);
        widgetView.setViewVisibility(R.id.widget_troubleshooter_pb, View.INVISIBLE);
        widgetView.setImageViewResource(R.id.widget_troubleshooter_im_update_btn, R.drawable.im_update_btn_off);
        widgetView.setImageViewResource(R.id.widget_troubleshooter_iv_troubleshooter, R.drawable.im_trouble_text_2);
        widgetView.setImageViewResource(R.id.widget_troubleshooter_iv_percipitation, R.drawable.im_percipitation_off);
        widgetView.setImageViewResource(R.id.widget_troubleshooter_iv_wind, R.drawable.im_wind_off);
        widgetView.setImageViewResource(R.id.widget_troubleshooter_iv_wet, R.drawable.im_wet_off);
        widgetView.setImageViewResource(R.id.widget_troubleshooter_iv_head, R.drawable.im_head_off);

        AppWidgetManager.getInstance(context).updateAppWidget(appWidgetID, widgetView);

        Widget widget = findWidget(appWidgetID, context);

        if(widget != null && widget.getDaysForecast() != null){
            Forecast forecast = widget.getDaysForecast().get(0);
            updateUI(appWidgetID, forecast, context);
        }
    }

    private Widget findWidget(int appWidgetID, Context context) {
        Preferences preferences = Preferences.getInstance(context, Preferences.SETTINGS);
        int myWidgetID = preferences.getData(MY_WIDGET_ID_DEPENDS + appWidgetID, 0);

        WidgetRepository repository = new WidgetRepository(context);
        try {
            return repository.find(myWidgetID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void updateUI(int appWidgetID, Forecast forecast, Context context) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_troubleshooter);

        views = fillMain(views, forecast, context);

        views = troubleshoot(views, forecast);

        views = fillConsole(views, forecast);

        AppWidgetManager.getInstance(context).updateAppWidget(appWidgetID, views);
    }

    private RemoteViews fillMain(RemoteViews views, Forecast forecast, Context context) {
        tfZelec = Typeface.createFromAsset(context.getAssets(),"fonts/new_zelec.ttf");
        tfDigit = Typeface.createFromAsset(context.getAssets(),"fonts/digit_3.TTF");

        WidgetDataBuilder builder = new WidgetDataBuilder();

        Weather currentWeather = builder.findCurrentWeather(forecast);
        String currentDate = DateHelper.getCurrent(DateFormat.DD_MM_YYYY);
        String cityName = forecast.getDayWeathers().get(0).getCityName();
        String temperature = builder.setTemperature(currentWeather);
        int ID = builder.setIcon(currentWeather);

        views.setTextViewText(R.id.widget_troubleshooter_tv_city_name, cityName);
        views.setImageViewBitmap(R.id.widget_troubleshooter_tv_date, convertToImg(currentDate, tfDigit, 50, ContextCompat.getColor(context, R.color.colorCyberpunkWhite), 500, 100, ContextCompat.getColor(context, R.color.colorCyberpunkGreen)));
        views.setImageViewResource(R.id.widget_troubleshooter_iv_weather_icon, ID);
        views.setTextViewText(R.id.widget_troubleshooter_tv_weather_value, temperature);

        return views;
    }

    private RemoteViews troubleshoot(RemoteViews views, Forecast forecast) {
        TroubleShooter troubleShooter = new TroubleShooter(forecast);

        if(troubleShooter.shootPrecipitation()) {
            views.setImageViewResource(R.id.widget_troubleshooter_iv_percipitation, R.drawable.im_percipitation_on);
        }
        if(troubleShooter.shootWind()) {
            views.setImageViewResource(R.id.widget_troubleshooter_iv_wind,R.drawable.im_wind_on);
        }
        if(troubleShooter.shootWet()) {
            views.setImageViewResource(R.id.widget_troubleshooter_iv_wet, R.drawable.im_wet_on);
        }
        if (troubleShooter.shootHeadPain()) {
            views.setImageViewResource(R.id.widget_troubleshooter_iv_head, R.drawable.im_head_on);
        }

        return views;
    }

    private RemoteViews fillConsole(RemoteViews views, Forecast forecast) {
        ConsoleMessageBuilder builder = new ConsoleMessageBuilder(forecast);

        views.setTextViewText(R.id.widget_troubleshooter_tv_warning, builder.buildWarning());
        views.setTextViewText(R.id.widget_troubleshooter_tv_timezone, builder.buildTimezone());
        views.setTextViewText(R.id.widget_troubleshooter_tv_battery, builder.buildBatteryStatus());
        views.setTextViewText(R.id.widget_troubleshooter_tv_wifi, builder.buildWifiStatus());
        views.setTextViewText(R.id.widget_troubleshooter_tv_precipitation, builder.buildPrecipitationValue());

        return views;
    }

    public Bitmap convertToImg(String text, Typeface typeface, int textSize, int color, int width, int height, int shadowColor) {
        Bitmap btmText = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(btmText);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setSubpixelText(true);
        paint.setTypeface(typeface);
        paint.setColor(color);
        paint.setTextSize(textSize);
        paint.setShadowLayer(6,0,0, shadowColor);
        paint.setTextAlign(Paint.Align.CENTER);

        int xPos = (canvas.getWidth() / 2);
        int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2)) ;

        canvas.drawText(text, xPos, yPos, paint);

        return btmText;
    }

    @SuppressLint("StaticFieldLeak")
    private class UpdateWidgetTask extends AsyncTask<Void, Void, Void> {
        private Context context;
        private int appWidgetID;
        private RemoteViews widgetView;

        UpdateWidgetTask(Context context, int appWidgetID) {
            this.context = context;
            this.appWidgetID = appWidgetID;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            widgetView = new RemoteViews(context.getPackageName(), R.layout.widget_troubleshooter);
            widgetView.setViewVisibility(R.id.widget_troubleshooter_pb, View.VISIBLE);
            widgetView.setImageViewResource(R.id.widget_troubleshooter_im_update_btn, R.drawable.im_update_btn_on);
            AppWidgetManager.getInstance(context).updateAppWidget(appWidgetID, widgetView);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            int myWidgetID = Objects.requireNonNull(findWidget(appWidgetID, context)).getId();
            Intent intentService = new Intent(context, WidgetUpdateService.class);
            intentService.putExtra(MY_WIDGET_ID, myWidgetID);
            context.startService(intentService);
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            widgetView.setViewVisibility(R.id.widget_troubleshooter_pb, View.INVISIBLE);
            widgetView.setImageViewResource(R.id.widget_troubleshooter_im_update_btn,R.drawable.im_update_btn_off);
            AppWidgetManager.getInstance(context).updateAppWidget(appWidgetID, widgetView);
            updateWidget(appWidgetID, context);
        }
    }
}
