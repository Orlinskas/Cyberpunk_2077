package com.orlinskas.cyberpunk.ui.home;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.RemoteViews;

import com.orlinskas.cyberpunk.R;
import com.orlinskas.cyberpunk.ToastBuilder;
import com.orlinskas.cyberpunk.updateWidget.SmallWeatherUpdateService;
import com.orlinskas.cyberpunk.chart.WeatherIconsSelector;
import com.orlinskas.cyberpunk.forecast.Forecast;
import com.orlinskas.cyberpunk.forecast.Weather;
import com.orlinskas.cyberpunk.preferences.Preferences;
import com.orlinskas.cyberpunk.widget.Widget;
import com.orlinskas.cyberpunk.widget.WidgetRepository;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.orlinskas.cyberpunk.updateWidget.Settings.MY_WIDGET_ID;
import static com.orlinskas.cyberpunk.preferences.Preferences.MY_WIDGET_ID_DEPENDS;
import static com.orlinskas.cyberpunk.preferences.Preferences.WIDGET_DAY_NUMBER;

public class WidgetSmallWeather extends AppWidgetProvider {
    public static final String ACTION = "action";
    public static final String ACTION_CREATE = "create";
    public static final String ACTION_DEFAULT = "default";
    public static final String ACTION_UPDATE = "updateSmallWeather";
    private final String ACTION_CLICK_LEFT = "leftAreaClick";
    private final String ACTION_CLICK_RIGHT = "rightAreaClick";
    private final String ACTION_CLICK_CENTER = "centerAreaClick";

    private final int[] imageViewIDsIcons =
            new int[]{R.id.layout_widget_ll_chart_case_icon_1, R.id.layout_widget_ll_chart_case_icon_2,
                    R.id.layout_widget_ll_chart_case_icon_3, R.id.layout_widget_ll_chart_case_icon_4,
                    R.id.layout_widget_ll_chart_case_icon_5, R.id.layout_widget_ll_chart_case_icon_6,
                    R.id.layout_widget_ll_chart_case_icon_7, R.id.layout_widget_ll_chart_case_icon_8};
    private final int[] textViewIDsTemperatures =
            new int[]{R.id.layout_widget_ll_chart_case_temp_1, R.id.layout_widget_ll_chart_case_temp_2,
                    R.id.layout_widget_ll_chart_case_temp_3, R.id.layout_widget_ll_chart_case_temp_4,
                    R.id.layout_widget_ll_chart_case_temp_5, R.id.layout_widget_ll_chart_case_temp_6,
                    R.id.layout_widget_ll_chart_case_temp_7, R.id.layout_widget_ll_chart_case_temp_8};
    private final int[] textViewIDsDates =
            new int[]{R.id.layout_widget_ll_chart_case_date_1, R.id.layout_widget_ll_chart_case_date_2,
                    R.id.layout_widget_ll_chart_case_date_3, R.id.layout_widget_ll_chart_case_date_4,
                    R.id.layout_widget_ll_chart_case_date_5, R.id.layout_widget_ll_chart_case_date_6,
                    R.id.layout_widget_ll_chart_case_date_7, R.id.layout_widget_ll_chart_case_date_8};

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
            int dayNumber = readDayNumber(appWidgetID, context);

            switch (actionExtra) {
                case ACTION_CREATE:
                case ACTION_DEFAULT:
                case ACTION_UPDATE:
                    writeDayNumber(0, appWidgetID, context);
                    updateWidget(appWidgetID, context);
                case ACTION_CLICK_CENTER:
                    UpdateWidgetTask task = new UpdateWidgetTask(context, appWidgetID);
                    task.execute();
                    break;
                case ACTION_CLICK_LEFT:
                    PrevDayTask prevDayTask = new PrevDayTask(context, appWidgetID, dayNumber);
                    prevDayTask.execute();
                    break;
                case ACTION_CLICK_RIGHT:
                    NextDayTask nextDayTask = new NextDayTask(context, appWidgetID, dayNumber);
                    nextDayTask.execute();
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
        RemoteViews widgetView = new RemoteViews(context.getPackageName(), R.layout.widget_small_weather);

        Intent leftClickIntent = new Intent(context, WidgetSmallWeather.class);
        leftClickIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        leftClickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetID);
        leftClickIntent.putExtra(ACTION, ACTION_CLICK_LEFT);
        PendingIntent pLeftIntent = PendingIntent.getBroadcast(context, appWidgetID + 100, leftClickIntent, 0);

        Intent rightClickIntent = new Intent(context, WidgetSmallWeather.class);
        rightClickIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        rightClickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetID);
        rightClickIntent.putExtra(ACTION, ACTION_CLICK_RIGHT);
        PendingIntent pRightIntent = PendingIntent.getBroadcast(context, appWidgetID + 200, rightClickIntent, 0);

        Intent centerClickIntent = new Intent(context, WidgetSmallWeather.class);
        centerClickIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        centerClickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetID);
        centerClickIntent.putExtra(ACTION, ACTION_CLICK_CENTER);
        PendingIntent pCenterIntent = PendingIntent.getBroadcast(context, appWidgetID + 300, centerClickIntent, 0);

        widgetView.setOnClickPendingIntent(R.id.layout_widget_btn_left_click_area, pLeftIntent);
        widgetView.setOnClickPendingIntent(R.id.layout_widget_btn_right_click_area, pRightIntent);
        widgetView.setOnClickPendingIntent(R.id.layout_widget_btn_center_click_area, pCenterIntent);

        widgetView.setImageViewResource(R.id.layout_widget_iv_left, R.drawable.ic_left_shew);
        widgetView.setImageViewResource(R.id.layout_widget_iv_right, R.drawable.ic_right_shew);
        widgetView.setViewVisibility(R.id.widget_layout_pb, View.INVISIBLE);

        AppWidgetManager.getInstance(context).updateAppWidget(appWidgetID, widgetView);

        Widget widget = findWidget(appWidgetID, context);

        if(widget != null && widget.getDaysForecast() != null){
            int dayNumber = readDayNumber(appWidgetID, context);

            if(dayNumber < 0 || dayNumber > widget.getDaysForecast().size() - 1) {
                dayNumber = 0;
                writeDayNumber(dayNumber, appWidgetID, context);
            }
            Forecast forecast = widget.getDaysForecast().get(dayNumber);
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
            ToastBuilder.create(context,"Critical error, reinstall");
            e.printStackTrace();
        }
        return null;
    }

    private int readDayNumber(int appWidgetID, Context context) {
        Preferences preferences = Preferences.getInstance(context, Preferences.SETTINGS);
        return preferences.getData(WIDGET_DAY_NUMBER + appWidgetID, 0);
    }

    private void writeDayNumber(int value, int appWidgetID, Context context) {
        Preferences preferences = Preferences.getInstance(context, Preferences.SETTINGS);
        if(value < 0) {
            value = 0;
        }
        preferences.saveData(WIDGET_DAY_NUMBER + appWidgetID, value);
    }

    private void updateUI(int appWidgetID, Forecast forecast, Context context) {
        RemoteViews widgetView = new RemoteViews(context.getPackageName(), R.layout.widget_small_weather);

        String cityName = forecast.getDayWeathers().get(0).getCityName();

        String timezoneUTC;
        int timezone = forecast.getDayWeathers().get(0).getTimezone();

        if(timezone > 0) {
            timezoneUTC = "UTC +" + timezone;
        }
        else {
            timezoneUTC = "UTC " + timezone;
        }

        String description = cityName + "  " + timezoneUTC;
        String todayDate = forecast.getDayDate();
        widgetView.setTextViewText(R.id.layout_widget_tv_description, description);
        widgetView.setTextViewText(R.id.layout_widget_tv_date, todayDate);

        ArrayList<Weather> weathers = forecast.getDayWeathers();
        int indexWeather = weathers.size() - 1;

        for(int indexView = 7; indexView >= 0; indexView--) {
            int ID = R.drawable.ic_na_icon;
            String temperature = "—";
            String dateTime = "—:—";
            if(indexWeather >= 0) {
                try {
                    WeatherIconsSelector selector = new WeatherIconsSelector();
                    ID = selector.findIcon(weathers.get(indexWeather));

                    int temp = weathers.get(indexWeather).getCurrentTemperature();
                    temperature = temp + "°";

                    dateTime = weathers.get(indexWeather).getTimeOfDataForecast().substring(11);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            indexWeather--;

            widgetView.setImageViewResource(imageViewIDsIcons[indexView], ID);
            widgetView.setTextViewText(textViewIDsTemperatures[indexView], temperature);
            widgetView.setTextViewText(textViewIDsDates[indexView], dateTime);
        }
        AppWidgetManager.getInstance(context).updateAppWidget(appWidgetID, widgetView);
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
            widgetView = new RemoteViews(context.getPackageName(), R.layout.widget_small_weather);
            widgetView.setViewVisibility(R.id.widget_layout_pb, View.VISIBLE);
            AppWidgetManager.getInstance(context).updateAppWidget(appWidgetID, widgetView);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            int myWidgetID = Objects.requireNonNull(findWidget(appWidgetID, context)).getId();
            Intent intentService = new Intent(context, SmallWeatherUpdateService.class);
            intentService.putExtra(MY_WIDGET_ID, myWidgetID);
            context.startService(intentService);
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            widgetView.setViewVisibility(R.id.widget_layout_pb, View.INVISIBLE);
            AppWidgetManager.getInstance(context).updateAppWidget(appWidgetID, widgetView);
            updateWidget(appWidgetID, context);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class NextDayTask extends AsyncTask<Void, Void, Void> {
        private Context context;
        private int appWidgetID;
        private int dayNumber;

        NextDayTask(Context context, int appWidgetID, int dayNumber) {
            this.context = context;
            this.appWidgetID = appWidgetID;
            this.dayNumber = dayNumber;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            writeDayNumber(dayNumber + 1, appWidgetID, context);
            updateWidget(appWidgetID, context);
            return null;
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class PrevDayTask extends AsyncTask<Void, Void, Void> {
        private Context context;
        private int appWidgetID;
        private int dayNumber;

        PrevDayTask(Context context, int appWidgetID, int dayNumber) {
            this.context = context;
            this.appWidgetID = appWidgetID;
            this.dayNumber = dayNumber;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            writeDayNumber(dayNumber - 1, appWidgetID, context);
            updateWidget(appWidgetID, context);
            return null;
        }
    }
}
