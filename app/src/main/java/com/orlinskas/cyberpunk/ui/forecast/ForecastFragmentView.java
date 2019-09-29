package com.orlinskas.cyberpunk.ui.forecast;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.orlinskas.cyberpunk.R;
import com.orlinskas.cyberpunk.ToastBuilder;
import com.orlinskas.cyberpunk.forecast.InstrumentPerformance;

import java.util.Objects;

public class ForecastFragmentView extends Fragment implements ForecastContract.View {
    private int widgetID;
    private ProgressBar progressBar;
    private RelativeLayout iconsLayoutCase;
    private RelativeLayout chartLayoutCase;
    private ImageView leftBtn;
    private ImageView rightBtn;
    private TextView currentDateTV, chartDescriptionTV, pressureValTV, humidityValTV, windSpeedValTV, rainValTV, snowValTV;
    private ForecastContract.Presenter presenter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_widget, container, false);
        progressBar = root.findViewById(R.id.fragment_widget_pb);
        iconsLayoutCase = root.findViewById(R.id.fragment_widget_rl_icons_case);
        chartLayoutCase = root.findViewById(R.id.fragment_widget_rl_chart_case);
        leftBtn = root.findViewById(R.id.fragment_widget_iv_left);
        rightBtn = root.findViewById(R.id.fragment_widget_iv_right);
        chartDescriptionTV = root.findViewById(R.id.fragment_widget_tv_description);
        currentDateTV = root.findViewById(R.id.fragment_widget_tv_date);

        pressureValTV = root.findViewById(R.id.fragment_widget_rl_info_pressure_tv_value);
        humidityValTV  = root.findViewById(R.id.fragment_widget_rl_info_humidity_tv_value);
        windSpeedValTV = root.findViewById(R.id.fragment_widget_rl_info_wind_tv_value);
        rainValTV  = root.findViewById(R.id.fragment_widget_rl_info_rain_tv_value);
        snowValTV = root.findViewById(R.id.fragment_widget_rl_info_snow_tv_value);

        Button deleteBtn = root.findViewById(R.id.fragment_widget_rl_menu_delete_btn);
        Button refreshBtn = root.findViewById(R.id.fragment_widget_rl_menu_refresh_btn);
        ImageView helpBtn = root.findViewById(R.id.fragment_widget_rl_info_help_iv);
        final SwipeRefreshLayout swipeRefreshLayout = root.findViewById(R.id.fragment_widget_sr);

        widgetID = getWidgetIDArgument();

        final Animation buttonClickAnim = AnimationUtils.loadAnimation(getContext(), R.anim.animation_button);
        final Animation chartOpenAnim = AnimationUtils.loadAnimation(getContext(), R.anim.animation_open_chart);

        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(presenter.prevDay()){
                    chartLayoutCase.startAnimation(chartOpenAnim);
                    iconsLayoutCase.startAnimation(chartOpenAnim);
                    v.startAnimation(buttonClickAnim);
                }
            }
        });

        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(presenter.nextDay()) {
                    chartLayoutCase.startAnimation(chartOpenAnim);
                    iconsLayoutCase.startAnimation(chartOpenAnim);
                    v.startAnimation(buttonClickAnim);
                }
            }
        });

        helpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClickAnim);
                presenter.help();
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClickAnim);
                showDeleteDialog();
            }
        });

        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClickAnim);
                presenter.refreshWidget(widgetID);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                       swipeRefreshLayout.setRefreshing(false);
                    }
                }, 2000);
                presenter.refreshWidget(widgetID);
            }
        });

        return root;
    }

    private int getWidgetIDArgument() {
        int id = 0;
        if (getArguments() != null) {
            id = getArguments().getInt("widgetID");
        }
        return id;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new ForecastFragmentPresenter(this, widgetID,
                getContext(), Objects.requireNonNull(getActivity()).getBaseContext());
        presenter.startWork();
    }

    @Override
    public void updateUI() {
        setIconsLayout(presenter.getIconsLayout());
        setChart(presenter.getChartLayout());
        setChartDate(presenter.getCurrentDate());
        setChartDescription(presenter.getChartDescription());
        setInstrumentPerformance(presenter.getInstrumentPerformance());
    }

    @Override
    public void setIconsLayout(LinearLayout linearLayout) {
        iconsLayoutCase.removeAllViews();
        iconsLayoutCase.addView(linearLayout);
    }

    @Override
    public void setChart(LineChart chart) {
        chartLayoutCase.removeAllViews();
        chartLayoutCase.addView(chart);
    }

    @Override
    public void setInstrumentPerformance(InstrumentPerformance performance) {
        humidityValTV.setText(String.valueOf(performance.getAverageHumidity()));
        windSpeedValTV.setText(String.valueOf(performance.getAverageWindSpeed()));
        pressureValTV.setText(String.valueOf(performance.getAveragePressure()));
        rainValTV.setText(String.valueOf(performance.getAverageRainVolume()));
        snowValTV.setText(String.valueOf(performance.getAverageSnowVolume()));
    }

    private void setChartDate(String currentDate) {
        currentDateTV.setText(currentDate);
    }

    private void setChartDescription(String chartDescription) {
        chartDescriptionTV.setText(chartDescription);
    }

    @Override
    public void doToast(String message) {
        ToastBuilder.create(getContext(), message);
    }

    @Override
    public void doSnackBar(String message) {
        ToastBuilder.createSnackBar(getView(), message);
    }

    @Override
    public void setAlpha(String itemName, int mode) {
        int alpha = 250;

        switch (mode) {
            case DEFAULT_MODE:
                alpha = 250;
                break;
            case LOW_MODE:
                alpha = 50;
                break;
        }

        switch (itemName) {
            case LEFT_BUTTON:
                leftBtn.setImageAlpha(alpha);
                break;
            case RIGHT_BUTTON:
                rightBtn.setImageAlpha(alpha);
                break;
        }
    }

    @Override
    public void startProgressDialog() {
        iconsLayoutCase.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void stopProgressDialog() {
        iconsLayoutCase.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }

    public void showDeleteDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());

        dialog.setTitle(getString(R.string.warning));
        dialog.setMessage(getString(R.string.delete_widget) + "?");
        dialog.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                presenter.removeWidget(widgetID);
            }
        });
        dialog.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                dialog.cancel();
            }
        });

        dialog.show();
    }

    @Override
    public void finish() {
        Intent intent = Objects.requireNonNull(getActivity()).getIntent();
        Objects.requireNonNull(getActivity()).finish();
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.destroy();
    }
}