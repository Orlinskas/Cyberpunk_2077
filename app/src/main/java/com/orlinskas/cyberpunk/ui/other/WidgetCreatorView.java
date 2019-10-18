package com.orlinskas.cyberpunk.ui.other;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orlinskas.cyberpunk.City;
import com.orlinskas.cyberpunk.Country;
import com.orlinskas.cyberpunk.R;
import com.orlinskas.cyberpunk.ToastBuilder;
import com.orlinskas.cyberpunk.ui.main.ForecastActivity;

import static com.orlinskas.cyberpunk.ui.other.WidgetCreatorContract.*;

public class WidgetCreatorView extends AppCompatActivity implements WidgetCreatorContract.View {
    private TextView countryName, cityName;
    private Button searchLocationBtn, chooseLocationBtn, createWidgetBtn;
    private TextView indicatorGpsOn, indicatorNetworkOn, indicatorGpsOff, indicatorNetworkOff;
    private ImageView progressBarImage;
    private RelativeLayout relativeLayout;
    private Country country;
    private City city;
    private WidgetCreatorContract.Presenter presenter;
    private static final int REQUEST_CODE_PERMISSION_FINE_LOCATION = 1312;
    private int backPressedCount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget_creator);
        searchLocationBtn = findViewById(R.id.activity_city_data_generator_btn_gps);
        chooseLocationBtn = findViewById(R.id.activity_city_data_generator_btn_open_list);
        createWidgetBtn = findViewById(R.id.activity_city_data_generator_btn_start);
        countryName = findViewById(R.id.activity_city_data_generator_tv_country_name);
        cityName = findViewById(R.id.activity_city_data_generator_tv_city_name);
        progressBarImage = findViewById(R.id.activity_city_data_generator_progress_bar);
        indicatorGpsOn = findViewById(R.id.activity_city_data_generator_tv_gps_on);
        indicatorGpsOff = findViewById(R.id.activity_city_data_generator_tv_gps_off);
        indicatorNetworkOn = findViewById(R.id.activity_city_data_generator_tv_network_on);
        indicatorNetworkOff = findViewById(R.id.activity_city_data_generator_tv_network_off);
        relativeLayout = findViewById(R.id.activity_city_data_generator_rl);

        final Animation animationClick = AnimationUtils.loadAnimation(this,R.anim.animation_button);

        presenter = new WidgetCreatorPresenter(getApplicationContext(), this,
                (LocationManager) getSystemService(LOCATION_SERVICE));
        presenter.startWork();

        countryName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countryName.startAnimation(animationClick);
                presenter.onClickChooseLocation();
            }
        });

        cityName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cityName.startAnimation(animationClick);
                presenter.onClickChooseLocation();
            }
        });

        chooseLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onClickChooseLocation();
            }
        });

        searchLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onClickSearchLocation();
            }
        });

        createWidgetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onClickCreateWidget(country, city);
            }
        });

        backPressedCount = 0;
    }

    @Override
    public void setProgressImage(int KEY) {
        final int IC_PROGRESS_1 = 1;
        final int IC_PROGRESS_2 = 2;
        final int IC_PROGRESS_3 = 3;
        final int IC_PROGRESS_4 = 4;

        switch (KEY) {
            case IC_PROGRESS_1:
                progressBarImage.setImageResource(R.drawable.im_progress_1);
                break;
            case IC_PROGRESS_2:
                progressBarImage.setImageResource(R.drawable.im_progress_2);
                break;
            case IC_PROGRESS_3:
                progressBarImage.setImageResource(R.drawable.im_progress_3);
                break;
            case IC_PROGRESS_4:
                progressBarImage.setImageResource(R.drawable.im_progress_4);
                break;
        }
    }

    @Override
    public void setButtonStatus(int BUTTON, int STATUS) {
        switch (BUTTON){
            case BUTTON_CREATE_WIDGET:
                if(STATUS == STATUS_ON) {
                    createWidgetBtn.setBackground(getResources().getDrawable(R.drawable.im_create_big_btn_on));
                }
                else {
                    createWidgetBtn.setBackground(getResources().getDrawable(R.drawable.im_create_big_btn_off));
                }
                break;case BUTTON_OPEN_LIST_ACTIVITY: if(STATUS == STATUS_ON) {
                    chooseLocationBtn.setBackground(getResources().getDrawable(R.drawable.im_choose_btn_on));
                }
                else {
                    chooseLocationBtn.setBackground(getResources().getDrawable(R.drawable.im_shoose_btn_off));
                }
                break;case BUTTON_START_SEARCH_LOCATION: if(STATUS == STATUS_ON) {
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
    public void setStatusIndicatorGPS(int STATUS) {
        switch (STATUS){
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
    public void setStatusIndicatorINTERNET(int STATUS) {
        switch (STATUS){
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
    public void openActivityForResult(Class activity) {
        Intent intent = new Intent(getApplicationContext(), CountryListActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    public void openForecastActivity(int myWidgetID) {
        Intent intent = new Intent(getApplicationContext(), ForecastActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("myWidgetID", myWidgetID);
        startActivity(intent);
    }

    @Override
    public void toAskGPSPermission() {
        int permissionStatus = ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_PERMISSION_FINE_LOCATION);
        }
        else {
            presenter.startSearchLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CODE_PERMISSION_FINE_LOCATION) {
            presenter.startSearchLocation();
        }
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
    public void doSnackBar(String message) {
        ToastBuilder.createSnackBar(relativeLayout, message);
    }

    @Override
    public void onBackPressed() {
        if(backPressedCount == 0) {
            backPressedCount++;
            presenter.stopSearchLocation();
            doSnackBar("Click again to exit");
        }
        else {
            backPressedCount++;
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destroy();
    }
}