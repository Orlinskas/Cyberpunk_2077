package com.orlinskas.cyberpunk.ui.other;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.orlinskas.cyberpunk.City;
import com.orlinskas.cyberpunk.Country;
import com.orlinskas.cyberpunk.R;
import com.orlinskas.cyberpunk.ToastBuilder;

import java.util.concurrent.TimeUnit;

public class WidgetCreatorView extends AppCompatActivity implements WidgetCreatorContract.View {
    private TextView countryName, cityName;
    private Button searchLocationBtn, openListLocationsBtn, createWidgetButton;
    private TextView indicatorGpsOn, indicatorNetworkOn, indicatorGpsOff, indicatorNetworkOff;
    private ImageView progressBarImage;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private ScrollView scrollView;
    private Country country;
    private City city;
    private WidgetCreatorContract.Presenter presenter;
    private ProgressBarTask progressBarTask;
    private Handler buttonHandler;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget_creator);
        searchLocationBtn = findViewById(R.id.activity_city_data_generator_btn_gps);
        openListLocationsBtn = findViewById(R.id.activity_city_data_generator_btn_open_list);
        createWidgetButton = findViewById(R.id.activity_city_data_generator_btn_start);

        countryName = findViewById(R.id.activity_city_data_generator_tv_country_name);
        cityName = findViewById(R.id.activity_city_data_generator_tv_city_name);
        progressBarImage = findViewById(R.id.activity_city_data_generator_progress_bar);
        indicatorGpsOn = findViewById(R.id.activity_city_data_generator_tv_gps_on);
        indicatorGpsOff = findViewById(R.id.activity_city_data_generator_tv_gps_off);
        indicatorNetworkOn = findViewById(R.id.activity_city_data_generator_tv_network_on);
        indicatorNetworkOff = findViewById(R.id.activity_city_data_generator_tv_network_off);
        scrollView = findViewById(R.id.activity_city_data_generator_sv);

        presenter = new WidgetCreatorPresenter(getApplicationContext(), this);
        presenter.startWork();

        buttonHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                setButtonStatus(msg.what, msg.arg1);
            }
        };

        openListLocationsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openListLocationsBtn.setBackground(getResources().getDrawable(R.drawable.im_choose_btn_on));
                Intent intent = new Intent(getApplicationContext(), CountryListActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        searchLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchLocationBtn.setBackground(getResources().getDrawable(R.drawable.im_location_btn_on));
                presenter.startSearchLocation();
            }
        });

        createWidgetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    Message message;
                    public void run() {
                        message = buttonHandler.obtainMessage(BUTTON_CREATE_WIDGET,1,0);
                        buttonHandler.sendMessage(message);
                        if(isCorrectnessWidgetData(city, country)) {
                            presenter.createWidget(city);
                        }
                        else {
                            doSnackBar("Incorrect data");
                        }
                        try {
                            TimeUnit.MILLISECONDS.sleep(150);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        message = buttonHandler.obtainMessage(BUTTON_CREATE_WIDGET,0,0);
                        buttonHandler.sendMessage(message);
                    }
                }).start();
            }
        });
    }

    private void setButtonStatus(int BUTTON, int STATUS) {
            switch (BUTTON){
                case BUTTON_CREATE_WIDGET:
                    if(STATUS == STATUS_ON) {
                        createWidgetButton.setBackground(getResources().getDrawable(R.drawable.im_create_big_btn_on));
                    }
                    else {
                        createWidgetButton.setBackground(getResources().getDrawable(R.drawable.im_create_big_btn_off));
                    }
                    break;
                case BUTTON_OPEN_LIST_ACTIVITY:
                    if(STATUS == STATUS_ON) {
                        openListLocationsBtn.setBackground(getResources().getDrawable(R.drawable.im_choose_btn_on));
                    }
                    else {
                        openListLocationsBtn.setBackground(getResources().getDrawable(R.drawable.im_shoose_btn_off));
                    }
                    break;
                case BUTTON_START_SEARCH_LOCATION:
                    if(STATUS == STATUS_ON) {
                        searchLocationBtn.setBackground(getResources().getDrawable(R.drawable.im_location_btn_on));
                    }
                    else {
                        searchLocationBtn.setBackground(getResources().getDrawable(R.drawable.im_location_btn_off));
                    }
                    break;
            }
    }

    @Override
    public void setCountry(Country country) {
        if (country != null) {
            this.country = country;
            countryName.setText(country.getName());
        }
    }

    @Override
    public void setCity(City city) {
        if(city != null) {
            this.city = city;
            cityName.setText(city.getName());
        }
    }

    @Override
    public void setStatusIndicatorGPS(int status) {
        switch (status){
            case STATUS_ON:
                indicatorGpsOn.setVisibility(View.VISIBLE);
                indicatorGpsOff.setVisibility(View.INVISIBLE);
                break;
            case STATUS_OFF:
                indicatorGpsOff.setVisibility(View.VISIBLE);
                indicatorGpsOn.setVisibility(View.INVISIBLE);
                break;
        }

    }

    @Override
    public void setStatusIndicatorINTERNET(int status) {
        switch (status){
            case STATUS_ON:
                indicatorNetworkOn.setVisibility(View.VISIBLE);
                indicatorNetworkOff.setVisibility(View.INVISIBLE);
                break;
            case STATUS_OFF:
                indicatorNetworkOff.setVisibility(View.VISIBLE);
                indicatorNetworkOn.setVisibility(View.INVISIBLE);
                break;
        }

    }

    @Override
    public void startAnimationButton(int button) {
        switch (button){
            case BUTTON_CREATE_WIDGET:

                break;
            case BUTTON_OPEN_LIST_ACTIVITY:

                break;
            case BUTTON_START_SEARCH_LOCATION:

                break;
        }
    }

    private boolean isCorrectnessWidgetData(City city, Country country) {
        if(city != null && country != null) {
            return city.getCountryCode().equals(country.getCode());
        }
        else {
            return false;
        }
    }

    @Override
    public void startProgressBar() {
        progressBarTask = new ProgressBarTask();
        progressBarTask.execute();
    }

    @Override
    public void stopProgressBar() {
        progressBarTask.cancel(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            City city = (City) data.getSerializableExtra("city");
            Country country = (Country) data.getSerializableExtra("country");
            setCity(city);
            setCountry(country);
        }
    }

    @Override
    public void doToast(String message) {
        ToastBuilder.create(getApplicationContext(),message);
    }

    @Override
    public void doSnackBar(String message) {
        ToastBuilder.createSnackBar(scrollView,message);
    }

    @SuppressLint("StaticFieldLeak")
    private class ProgressBarTask extends AsyncTask<Void, Void, Void> {
        private final int IC_PROGRESS_1 = R.drawable.im_progress_1;
        private final int IC_PROGRESS_2 = R.drawable.im_progress_2;
        private final int IC_PROGRESS_3 = R.drawable.im_progress_3;
        private final int IC_PROGRESS_4 = R.drawable.im_progress_4;
        private final int[] progressImage = {IC_PROGRESS_1, IC_PROGRESS_2, IC_PROGRESS_3, IC_PROGRESS_4};
        private int imageCount = 1;

        @Override
        protected Void doInBackground(Void... voids) {
            int roundCount = 0;
            do {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int imageId = progressImage[imageCount];
                        progressBarImage.setImageResource(imageId);
                    }
                });
                imageCount++;
                roundCount++;
                if(imageCount == 4) {
                    imageCount = 0;
                }
                if (isCancelled()) {
                    break;
                }
            }
            while (true);

            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            progressBarImage.setImageResource(IC_PROGRESS_1);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBarImage.setImageResource(IC_PROGRESS_1);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        openListLocationsBtn.setBackground(getResources().getDrawable(R.drawable.im_shoose_btn_off));
        searchLocationBtn.setBackground(getResources().getDrawable(R.drawable.im_location_btn_off));
        createWidgetButton.setBackground(getResources().getDrawable(R.drawable.im_create_big_btn_off));
    }

}