package com.orlinskas.cyberpunk.ui.forecast;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import com.orlinskas.cyberpunk.date.DateFormat;
import com.orlinskas.cyberpunk.date.DateHelper;
import com.orlinskas.cyberpunk.forecast.ForecastListBuilder;
import com.orlinskas.cyberpunk.forecast.WeatherReceiver;
import com.orlinskas.cyberpunk.preferences.Preferences;
import com.orlinskas.cyberpunk.widget.Widget;
import com.orlinskas.cyberpunk.widget.WidgetRepository;

import static com.orlinskas.cyberpunk.preferences.Preferences.SETTINGS;
import static com.orlinskas.cyberpunk.preferences.Preferences.WIDGET_LAST_UPDATE;

public class ForecastFragmentModel implements ForecastContract.WidgetModel {
    private ForecastUpdateListener presenter;

    ForecastFragmentModel(ForecastUpdateListener presenter) {
        this.presenter = presenter;
    }

    @Override
    public void doUpdate(int widgetID, Context appContext) {
        UpdateWidgetTask task = new UpdateWidgetTask(appContext, widgetID, presenter);
        task.execute();
    }

    @SuppressLint("StaticFieldLeak")
    private class UpdateWidgetTask extends AsyncTask<Void, Void, Void> {
        private Context context;
        private int widgetID;
        private ForecastUpdateListener presenter;
        private Throwable error;
        private Widget widget;

        UpdateWidgetTask(Context context, int widgetID, ForecastUpdateListener presenter) {
            this.context = context;
            this.widgetID = widgetID;
            this.presenter = presenter;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                widget = findWidgetInRepo(widgetID);
                sendRequest(widget);
                updateForecastInWidget(widget);
                updateWidgetInRepository(widget);
                saveWidgetUpdateDate(widget);
            } catch (Exception e) {
                e.printStackTrace();
                error = e;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            generateCallBack();
        }

        private void generateCallBack() {
            if(error == null) {
                presenter.onUpdateFinished(widget.getCity().getName());
            }
            else {
                presenter.onUpdateFailed(error.getMessage());
            }
        }

        private Widget findWidgetInRepo(int widgetID) {
            Widget widget = null;
            WidgetRepository repository = new WidgetRepository(context);
            try {
                widget = repository.find(widgetID);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return widget;
        }

        private void sendRequest(Widget widget) throws Exception {
            WeatherReceiver receiver = new WeatherReceiver(context, widget.getRequest());
            receiver.receive();
        }

        private void updateForecastInWidget(Widget widget) {
            ForecastListBuilder forecastsBuilder = new ForecastListBuilder(widget, context);
            widget.setDaysForecast(forecastsBuilder.build());
        }

        private void updateWidgetInRepository(Widget widget) throws Exception {
            WidgetRepository repository = new WidgetRepository(context);
            repository.update(widget);
        }

        private void saveWidgetUpdateDate(Widget widget) {
            String id = String.valueOf(widget.getId());
            String currentDate = DateHelper.getCurrent(DateFormat.YYYY_MM_DD_HH_MM);

            Preferences preferences = Preferences.getInstance(context, SETTINGS);
            preferences.saveData(WIDGET_LAST_UPDATE + id, currentDate);
        }
    }
}