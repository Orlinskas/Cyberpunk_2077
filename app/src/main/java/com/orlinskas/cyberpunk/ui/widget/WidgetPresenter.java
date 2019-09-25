package com.orlinskas.cyberpunk.ui.widget;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.LinearLayout;

import com.github.mikephil.charting.charts.LineChart;
import com.orlinskas.cyberpunk.ActivityOpener;
import com.orlinskas.cyberpunk.R;
import com.orlinskas.cyberpunk.background.WidgetUpdateChecker;
import com.orlinskas.cyberpunk.chart.ChartBuilder;
import com.orlinskas.cyberpunk.chart.WeatherIconsLayoutBuilder;
import com.orlinskas.cyberpunk.forecast.Forecast;
import com.orlinskas.cyberpunk.forecast.InstrumentPerformance;
import com.orlinskas.cyberpunk.forecast.InstrumentPerformanceBuilder;
import com.orlinskas.cyberpunk.ui.other.HelpActivity;
import com.orlinskas.cyberpunk.widget.Widget;
import com.orlinskas.cyberpunk.widget.WidgetRemover;
import com.orlinskas.cyberpunk.widget.WidgetRepository;

public class WidgetPresenter implements WidgetContract.Presenter, WidgetUpdateListener {
    private WidgetContract.View view;
    private WidgetContract.WidgetModel model;
    private int widgetID;
    private Widget widget;
    private Context viewContext;
    private Context appContext;
    private int dayNumber;
    private int dayCount;

    WidgetPresenter(WidgetContract.View view, int widgetID, Context viewContext, Context appContext) {
        this.viewContext = viewContext;
        this.appContext = appContext;
        this.view = view;
        this.widgetID = widgetID;
    }

    @Override
    public void startWork() {
        widget = findWidgetInRepo(widgetID);
        model = new WidgetModel(this);

        if(widget.getDaysForecast() == null) {
            goWithEmptyData(widgetID, viewContext);
        }
        else {
            goWithData();
        }
    }

    private Widget findWidgetInRepo(int widgetID) {
        Widget widget = null;
        WidgetRepository repository = new WidgetRepository(viewContext);
        try {
            widget = repository.find(widgetID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return widget;
    }

    private void goWithEmptyData(int widgetID, Context appContext) {
        model.doUpdate(widgetID, appContext);
        view.startProgressDialog();
    }

    private void goWithData() {
        dayNumber = 0;
        dayCount = this.widget.getDaysForecast().size() - 1;
        showViewElements();
    }

    private void showViewElements() {
        view.updateUI();
        if(checkAvailableUpdate(widgetID)) {
            startUpdate();
        }
    }

    private boolean checkAvailableUpdate(int widgetID) {
        WidgetUpdateChecker checker = new WidgetUpdateChecker(widgetID, viewContext);
        return checker.check();
    }

    private void startUpdate() {
        int sendWidgetID = widget.getId();

        if(isInternetConnection(viewContext)) {
            model.doUpdate(sendWidgetID, appContext);
        }
        else {
            view.doToast(viewContext.getString(R.string.internet_is_off));
        }
    }

    private boolean isInternetConnection(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    public LinearLayout getIconsLayout() {
        WeatherIconsLayoutBuilder builder = new WeatherIconsLayoutBuilder(getCurrentForecast(), viewContext);
        return builder.buildLayout();
    }

    @Override
    public LineChart getChartLayout() {
        ChartBuilder builder = new ChartBuilder(getCurrentForecast(), viewContext);
        return builder.buildChart();
    }

    @Override
    public InstrumentPerformance getInstrumentPerformance() {
        InstrumentPerformanceBuilder builder = new InstrumentPerformanceBuilder(getCurrentForecast());
        return builder.build();
    }

    @Override
    public String getCurrentDate() {
        return getCurrentForecast().getDayDate();
    }

    private Forecast getCurrentForecast() {
        setButtonAlpha();
        return widget.getDaysForecast().get(dayNumber);
    }

    private void setButtonAlpha() {
        if(dayNumber == 0) {
            view.setAlpha(view.LEFT_BUTTON, view.LOW_MODE);
        }
        else if(dayNumber == dayCount) {
            view.setAlpha(view.RIGHT_BUTTON, view.LOW_MODE);
        }
        else {
            view.setAlpha(view.RIGHT_BUTTON, view.DEFAULT_MODE);
            view.setAlpha(view.LEFT_BUTTON, view.DEFAULT_MODE);
        }
    }

    @Override
    public String getChartDescription() {
        String countryCode = widget.getCity().getCountryCode();
        String cityName = widget.getCity().getName();
        int timeZone = widget.getDaysForecast().get(dayNumber).getDayWeathers().get(0).getTimezone();
        String timeZoneFormat;
        if(timeZone > 0) {
            timeZoneFormat = "UTC +" + timeZone;
        }
        else {
            timeZoneFormat = "UTC " + timeZone;
        }

        return countryCode + "  " + cityName + "  " + timeZoneFormat;
    }

    @Override
    public boolean prevDay() {
        if(dayNumber > 0){
            dayNumber--;
            view.updateUI();
            return true;
        }
        view.doToast(buildMessageAvailableDates());
        return false;
    }

    @Override
    public boolean nextDay() {
        if(dayNumber < dayCount){
            dayNumber++;
            view.updateUI();
            return true;
        }
        view.doToast(buildMessageAvailableDates());
        return false;
    }

    private String buildMessageAvailableDates() {
        try {
            return String.format(
                    "%1$s %2$s %3$s %4$s.",
                    viewContext.getString(R.string.avialable_from),
                    widget.getDaysForecast().get(0).getDayDate(),
                    viewContext.getString(R.string.to),
                    widget.getDaysForecast().get(dayCount).getDayDate()
            );
        } catch (Exception e) {
            e.printStackTrace();
            return viewContext.getString(R.string.empty_data);
        }
    }

    @Override
    public void onUpdateFinished(String name) {
        this.widget = findWidgetInRepo(widgetID);
        view.stopProgressDialog();
        view.doSnackBar(String.format("%s %s", name, viewContext.getString(R.string.update)));
        dayNumber = 0;
        dayCount = this.widget.getDaysForecast().size() - 1;
        showViewElements();
    }

    @Override
    public void onUpdateFailed(String message) {
        view.doSnackBar(viewContext.getString(R.string.error_get_data));
    }

    @Override
    public void removeWidget(int widgetID) {
        WidgetRemover remover = new WidgetRemover(viewContext);
        remover.remove(widgetID);
        view.finish();
    }

    @Override
    public void refreshWidget(int widgetID) {
        if(checkAvailableUpdate(widgetID)) {
            startUpdate();
        }
        else {
            view.doSnackBar(viewContext.getString(R.string.almost_refresh));
        }
    }

    @Override
    public void help() {
        ActivityOpener.openActivity(viewContext, HelpActivity.class);
    }

    @Override
    public void destroy() {
        view = null;
        model = null;
        widget = null;
    }
}
