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
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.RemoteViews;

import com.orlinskas.cyberpunk.R;
import com.orlinskas.cyberpunk.ToastBuilder;
import com.orlinskas.cyberpunk.updateWidget.TroubleshooterUpdateService;
import com.orlinskas.cyberpunk.date.DateFormat;
import com.orlinskas.cyberpunk.date.DateHelper;
import com.orlinskas.cyberpunk.forecast.Forecast;
import com.orlinskas.cyberpunk.forecast.Weather;
import com.orlinskas.cyberpunk.homeWidget.ConsoleMessageBuilder;
import com.orlinskas.cyberpunk.homeWidget.TroubleShooter;
import com.orlinskas.cyberpunk.homeWidget.WidgetDataBuilder;
import com.orlinskas.cyberpunk.preferences.Preferences;
import com.orlinskas.cyberpunk.ui.main.ForecastActivity;
import com.orlinskas.cyberpunk.widget.Widget;
import com.orlinskas.cyberpunk.widget.WidgetRepository;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.orlinskas.cyberpunk.updateWidget.Settings.MY_WIDGET_ID;
import static com.orlinskas.cyberpunk.preferences.Preferences.MY_WIDGET_ID_DEPENDS;

public class WidgetTroubleshooter extends AppWidgetProvider {
    public static final String ACTION = "action";
    public static final String ACTION_CREATE = "create";
    public static final String ACTION_DEFAULT = "default";
    public static final String ACTION_UPDATE = "updateTroubleshooter";
    private final String ACTION_CLICK_UPDATE_BUTTON = "centerAreaClick";
    private final String ACTION_CLICK_FLASHLIGHT = "flashlightAreaClick";
    private final String ACTION_CLICK_WIFI = "wifiAreaClick";
    private final String ACTION_CLICK_WEATHER = "weatherAreaClick";

    @Override
    public IBinder peekService(Context myContext, Intent service) {
        return super.peekService(myContext, service);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        for (int id : appWidgetIds) {
            createWidget(id, context);
        }
    }

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
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
                    createWidget(appWidgetID, context);
                    break;
                case ACTION_CLICK_UPDATE_BUTTON:
                    UpdateWidgetTask task = new UpdateWidgetTask(context, appWidgetID);
                    task.execute();
                    break;
                case ACTION_CLICK_FLASHLIGHT:
                    openFlashlightActivity(context);
                    break;
                case ACTION_CLICK_WIFI:
                    enableWifi(context, appWidgetID);
                    break;
                case ACTION_CLICK_WEATHER:
                    openWeatherPage(context, appWidgetID);
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

    public void createWidget(int appWidgetID, Context context) {
        RemoteViews widgetView = new RemoteViews(context.getPackageName(), R.layout.widget_troubleshooter);

        Intent updateClickIntent = new Intent(context, WidgetTroubleshooter.class);
        updateClickIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        updateClickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetID);
        updateClickIntent.putExtra(ACTION, ACTION_CLICK_UPDATE_BUTTON);
        PendingIntent pUpdateIntent = PendingIntent.getBroadcast(context, appWidgetID + 400, updateClickIntent, 0);

        Intent flashLightClickIntent = new Intent(context, WidgetTroubleshooter.class);
        flashLightClickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetID);
        flashLightClickIntent.putExtra(ACTION, ACTION_CLICK_FLASHLIGHT);
        PendingIntent pFlashlightIntent = PendingIntent.getBroadcast(context, appWidgetID + 500, flashLightClickIntent, 0);

        Intent wifiClickIntent = new Intent(context, WidgetTroubleshooter.class);
        wifiClickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetID);
        wifiClickIntent.putExtra(ACTION, ACTION_CLICK_WIFI);
        PendingIntent pWifiIntent = PendingIntent.getBroadcast(context, appWidgetID + 600, wifiClickIntent, 0);

        Intent openWeatherPageIntent = new Intent(context, WidgetTroubleshooter.class);
        openWeatherPageIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetID);
        openWeatherPageIntent.putExtra(ACTION, ACTION_CLICK_WEATHER);
        PendingIntent pWeatherPageIntent = PendingIntent.getBroadcast(context, appWidgetID + 700, openWeatherPageIntent, 0);

        widgetView.setOnClickPendingIntent(R.id.widget_troubleshooter_im_update_btn, pUpdateIntent);
        widgetView.setOnClickPendingIntent(R.id.widget_troubleshooter_iv_flashlight, pFlashlightIntent);
        widgetView.setOnClickPendingIntent(R.id.widget_troubleshooter_iv_wifi, pWifiIntent);
        widgetView.setOnClickPendingIntent(R.id.widget_troubleshooter_rl_clock, pWeatherPageIntent);
        widgetView.setOnClickPendingIntent(R.id.widget_troubleshooter_ll_troubleshooter, pWeatherPageIntent);

        widgetView.setImageViewResource(R.id.widget_troubleshooter_im_update_btn, R.drawable.im_update_btn_off);
        widgetView.setImageViewResource(R.id.widget_troubleshooter_iv_troubleshooter, R.drawable.im_trouble_text_2);
        widgetView.setImageViewResource(R.id.widget_troubleshooter_iv_percipitation, R.drawable.im_percipitation_off);
        widgetView.setImageViewResource(R.id.widget_troubleshooter_iv_wind, R.drawable.im_wind_off);
        widgetView.setImageViewResource(R.id.widget_troubleshooter_iv_wet, R.drawable.im_wet_off);
        widgetView.setImageViewResource(R.id.widget_troubleshooter_iv_head, R.drawable.im_head_off);
        widgetView.setImageViewResource(R.id.widget_troubleshooter_iv_flashlight, R.drawable.im_btn_flashlight_off);

        if(isWifiEnabled(context)) {
            widgetView.setImageViewResource(R.id.widget_troubleshooter_iv_wifi, R.drawable.im_btn_wifi_on);
        }
        else {
            widgetView.setImageViewResource(R.id.widget_troubleshooter_iv_wifi, R.drawable.im_btn_wifi_off);
        }

        widgetView.setViewVisibility(R.id.widget_troubleshooter_pb, View.INVISIBLE);

        AppWidgetManager.getInstance(context).updateAppWidget(appWidgetID, widgetView);

        Widget widget = findWidget(appWidgetID, context);
        if(widget != null && widget.getDaysForecast() != null){
            Forecast forecast = widget.getDaysForecast().get(0);
            updateUI(appWidgetID, forecast, context);
        }
    }

    private Widget findWidget(int appWidgetID, Context context) {
        int myWidgetID = readMyWidgetID(appWidgetID, context);

        WidgetRepository repository = new WidgetRepository(context);
        try {
            return repository.find(myWidgetID);
        } catch (Exception e) {
            ToastBuilder.create(context,"Critical error, reinstall");
            e.printStackTrace();
        }
        return null;
    }

    private int readMyWidgetID(int appWidgetID, Context context) {
        Preferences preferences = Preferences.getInstance(context, Preferences.SETTINGS);
        return preferences.getData(MY_WIDGET_ID_DEPENDS + appWidgetID, 0);
    }

    private void updateUI(int appWidgetID, Forecast forecast, Context context) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_troubleshooter);

        updateMain(views, forecast, context);

        updateTroubleshooter(views, forecast);

        updateConsole(views, forecast, context);

        AppWidgetManager.getInstance(context).updateAppWidget(appWidgetID, views);
    }

    private void updateMain(RemoteViews views, Forecast forecast, Context context) {
        Typeface tfDigit = Typeface.createFromAsset(context.getAssets(), "fonts/digit_3.TTF");

        WidgetDataBuilder builder = new WidgetDataBuilder();

        Weather currentWeather = builder.findCurrentWeather(forecast);
        String currentDate = DateHelper.getCurrent(DateFormat.DD_MM_YYYY);
        String cityName = forecast.getDayWeathers().get(0).getCityName();
        String temperature = builder.setTemperature(currentWeather);
        int ID = builder.setIcon(currentWeather);

        views.setTextViewText(R.id.widget_troubleshooter_tv_city_name, cityName);
        views.setImageViewBitmap(R.id.widget_troubleshooter_tv_date, convertToImg(currentDate, tfDigit, 50, ContextCompat.getColor(context, R.color.colorCyberpunkYellow), 500, 100, ContextCompat.getColor(context, R.color.colorCyberpunkGreen)));
        views.setImageViewResource(R.id.widget_troubleshooter_iv_weather_icon, ID);
        views.setTextViewText(R.id.widget_troubleshooter_tv_weather_value, temperature);

    }

    private void updateTroubleshooter(RemoteViews views, Forecast forecast) {
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

    }

    private void updateConsole(RemoteViews views, Forecast forecast, Context context) {
        ConsoleMessageBuilder builder = new ConsoleMessageBuilder(forecast, context);

        views.setTextViewText(R.id.widget_troubleshooter_tv_warning, builder.buildLastUpdate());
        views.setTextViewText(R.id.widget_troubleshooter_tv_timezone_utc, builder.buildTimezone());
        views.setTextViewText(R.id.widget_troubleshooter_tv_RAM, builder.buildRAM());
        views.setTextViewText(R.id.widget_troubleshooter_tv_memory, builder.buildMemoryTotal());
        views.setTextViewText(R.id.widget_troubleshooter_tv_wifi, builder.buildMemoryFree());
        views.setTextViewText(R.id.widget_troubleshooter_tv_precipitation, builder.buildWifiStatus());
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
            Intent intentService = new Intent(context, TroubleshooterUpdateService.class);
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
            createWidget(appWidgetID, context);
        }
    }

    private void openWeatherPage(Context context, int appWidgetID) {
        Intent intent = new Intent(context, ForecastActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("myWidgetID", readMyWidgetID(appWidgetID, context));
        context.startActivity(intent);
    }

    private void enableWifi(Context context, int appWidgetID) {
        if(isWifiEnabled(context)) {
            try {
                WifiManager wifi = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                wifi.setWifiEnabled(false);

                RemoteViews widgetView = new RemoteViews(context.getPackageName(), R.layout.widget_troubleshooter);
                widgetView.setImageViewResource(R.id.widget_troubleshooter_iv_wifi, R.drawable.im_btn_wifi_off);
                AppWidgetManager.getInstance(context).updateAppWidget(appWidgetID, widgetView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                WifiManager wifi = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                wifi.setWifiEnabled(true);

                RemoteViews widgetView = new RemoteViews(context.getPackageName(), R.layout.widget_troubleshooter);
                widgetView.setImageViewResource(R.id.widget_troubleshooter_iv_wifi, R.drawable.im_btn_wifi_on);
                AppWidgetManager.getInstance(context).updateAppWidget(appWidgetID, widgetView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isWifiEnabled(Context context) {
        WifiManager manager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        return manager.isWifiEnabled();
    }

    private void openFlashlightActivity(Context context) {
        Intent intent = new Intent(context, FlashlightActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
