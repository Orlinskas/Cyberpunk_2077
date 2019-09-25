package com.orlinskas.cyberpunk.chart;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.LinearLayout;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.orlinskas.cyberpunk.R;
import com.orlinskas.cyberpunk.forecast.Forecast;
import com.orlinskas.cyberpunk.forecast.Weather;

import java.util.ArrayList;
import java.util.List;

public class ChartBuilder {
    private Context context;
    private Forecast forecast;

    public ChartBuilder(Forecast forecast, Context context) {
        this.context = context;
        this.forecast = forecast;
    }

    public LineChart buildChart() {
        LineChart chart = new LineChart(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        chart.setLayoutParams(params);

        chart.setDragEnabled(false);
        chart.setScaleEnabled(false);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        LineDataSet dataSet = createLineDataSet();
        dataSets.add(dataSet);
        LineData lineData = new LineData(dataSets);
        chart.setData(lineData);

        Description description = new Description();
        description.setEnabled(false);
        chart.setDescription(description);

        Legend legend = chart.getLegend();
        legend.setEnabled(false);

        chart = buildXAxis(chart);
        chart = buildYAxis(chart);

        return chart;
    }

    private LineDataSet createLineDataSet() {
        List<Entry> entries = collectEntries();
        LineDataSet dataSet = new LineDataSet(entries, null);

        dataSet.setMode(LineDataSet.Mode.LINEAR);
        dataSet.setFillAlpha(100);
        dataSet.setLineWidth(1f);
        dataSet.setCircleRadius(4f);
        dataSet.setValueTextSize(12f);
        dataSet.setCubicIntensity(2f);

        Typeface typeface = Typeface.defaultFromStyle(Typeface.NORMAL);
        dataSet.setValueTypeface(typeface);

        dataSet.setColor(context.getResources().getColor(R.color.colorChartLine));
        dataSet.setCircleColor(context.getResources().getColor(R.color.colorChartCircle));
        dataSet.setCircleHoleColor(context.getResources().getColor(R.color.colorChartCircleSmall));
        dataSet.setValueTextColor(context.getResources().getColor(R.color.colorChartTemperature));

        dataSet.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                String result = Float.toString(value);
                if(result.contains(".")) {
                    result = " " + result.substring(0, result.indexOf(".")) + "°";
                }
                return result;
            }
        });

        return dataSet;
    }

    private List<Entry> collectEntries() {
        ArrayList<Weather> weathers = forecast.getDayWeathers();

        List<Entry> entries = new ArrayList<>();

        for(int i = 0; i < weathers.size(); i++) {
            if(weathers.get(i) != null) {
                float temperature = (float) weathers.get(i).getCurrentTemperature();
                entries.add(new Entry(i, temperature));
            }
        }

        return entries;
    }

    private LineChart buildXAxis(LineChart chart) {
        XAxis xAxis = chart.getXAxis();
        Typeface typeface = Typeface.defaultFromStyle(Typeface.NORMAL);
        xAxis.setTypeface(typeface);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(12f);
        xAxis.setTextColor(context.getResources().getColor(R.color.colorChartTimeX));
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(true);

        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                try {
                    if(value % 1 == 0) {
                        String date = forecast.getDayWeathers().get((int) value).getTimeOfDataForecast();
                        return date.substring(11);
                    }
                    else if(value % 1 >= 0.6) {
                        String date = forecast.getDayWeathers().get((int) value + 1).getTimeOfDataForecast();
                        return date.substring(11);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return "";
            }
        });

        return chart;
    }

    private LineChart buildYAxis(LineChart chart) {
        YAxis yAxisLeft = chart.getAxisLeft();
        yAxisLeft.setEnabled(false);

        YAxis yAxisRight = chart.getAxisRight();
        yAxisRight.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        yAxisRight.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return "";
            }
        });

        return chart;
    }
}
