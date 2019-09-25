package com.orlinskas.cyberpunk.ui.widget;

import android.content.Context;
import android.widget.LinearLayout;

import com.github.mikephil.charting.charts.LineChart;
import com.orlinskas.cyberpunk.forecast.InstrumentPerformance;

public interface WidgetContract {

    interface View {
        void setChart(LineChart chart);
        void setIconsLayout(LinearLayout linearLayout);
        void setInstrumentPerformance(InstrumentPerformance performance);
        void updateUI();
        void doToast(String message);
        void doSnackBar(String message);
        void setAlpha(String item, int mode);
        void startProgressDialog();
        void stopProgressDialog();
        void finish();
        String LEFT_BUTTON = "left";
        String RIGHT_BUTTON = "right";
        int DEFAULT_MODE = 0;
        int LOW_MODE = 1;
    }

    interface Presenter {
        void startWork();
        LinearLayout getIconsLayout();
        LineChart getChartLayout();
        InstrumentPerformance getInstrumentPerformance();
        String getChartDescription();
        String getCurrentDate();
        boolean nextDay();
        boolean prevDay();
        void help();
        void removeWidget(int widgetID);
        void refreshWidget(int widgetID);
        void destroy();
    }

    interface WidgetModel {
        void doUpdate(int widgetID, Context appContext);
    }
}
