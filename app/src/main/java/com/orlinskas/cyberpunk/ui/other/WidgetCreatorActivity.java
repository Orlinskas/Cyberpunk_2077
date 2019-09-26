package com.orlinskas.cyberpunk.ui.other;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import com.orlinskas.cyberpunk.location.CityFinder;
import com.orlinskas.cyberpunk.post.CountryNameWriter;
import com.orlinskas.cyberpunk.widget.Widget;
import com.orlinskas.cyberpunk.widget.WidgetCopyChecker;
import com.orlinskas.cyberpunk.widget.WidgetCreator;
import com.orlinskas.cyberpunk.widget.WidgetRepository;

@SuppressLint("MissingPermission")
public class WidgetCreatorActivity extends AppCompatActivity {
    private TextView countryName, cityName;
    private TextView indicatorGpsOn, indicatorNetworkOn, indicatorGpsOff, indicatorNetworkOff;
    private ImageView progressBarImage;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private ScrollView scrollView;
    private Country country;
    private City city;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget_creator);
        Button searchLocationBtn = findViewById(R.id.activity_city_data_generator_btn_gps);
        Button openListLocationsBtn = findViewById(R.id.activity_city_data_generator_btn_open_list);
        Button createWidgetButton = findViewById(R.id.activity_city_data_generator_btn_start);

        countryName = findViewById(R.id.activity_city_data_generator_tv_country_name);
        cityName = findViewById(R.id.activity_city_data_generator_tv_city_name);
        progressBarImage = findViewById(R.id.activity_city_data_generator_progress_bar);
        indicatorGpsOn = findViewById(R.id.activity_city_data_generator_tv_gps_on);
        indicatorGpsOff = findViewById(R.id.activity_city_data_generator_tv_gps_off);
        indicatorNetworkOn = findViewById(R.id.activity_city_data_generator_tv_network_on);
        indicatorNetworkOff = findViewById(R.id.activity_city_data_generator_tv_network_off);
        scrollView = findViewById(R.id.activity_city_data_generator_sv);

        //checkGPSOn();
        //checkNetworkOn();

        searchLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if(isSearchTaskRunning()){
                //    ToastBuilder.create(getApplicationContext(), getString(R.string.search_working));
                //}
                //else {
                //    permissionGPS = checkPermissionGPSLocation();
                //    permissionNetwork = checkPermissionNetworkLocation();
//
                //    if (!permissionGPS) {
                //        toAskPermissionGPSLocation();
                //    }
                //    if (!permissionNetwork) {
                //        toAskPermissionNetworkLocation();
                //    }
//
                //    String provider = chooseAvailableProvider();
//
                //    if (provider == null) {
                //        ToastBuilder.create(getApplicationContext(), getString(R.string.acces_permission));
                //    } else {
                //        runSearchLocationTask(provider);
                //    }
                //}
            }
        });

        openListLocationsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CountryListActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        createWidgetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isCorrectnessWidgetData(city, country)) {
                    Widget widget = createWidget(city);
                    if(widget != null) {
                        startNextActivity(widget);
                    }
                    else {
                        showErrorMessage();
                    }
                }
                else {
                    showErrorMessage();
                }
            }
        });

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListenerClass();
        //searchLocationTask = new SearchLocationTask();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            city = (City) data.getSerializableExtra("city");
            country = (Country) data.getSerializableExtra("country");
        }
        showResult(city, country);
    }

    private void showResult(City city, Country country) {
        try {
            countryName.setText(country.getName());
            cityName.setText(city.getName());
        } catch (Exception e) {
            e.printStackTrace();
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
    protected void onResume() {
        super.onResume();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                1000, 10, locationListener);
        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 1000, 10,
                locationListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(locationListener);
    }



    private Widget createWidget(City city) {
        try {
            WidgetCreator widgetCreator = new WidgetCreator();
            WidgetRepository widgetRepository = new WidgetRepository(getApplicationContext());
            Widget widget = widgetCreator.create(city);
            WidgetCopyChecker checker = new WidgetCopyChecker(getApplicationContext(), widget);
            if(checker.check()){
                return null;
            }
            else {
                widgetRepository.add(widget);
                return widget;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void startNextActivity(Widget widget) {

    }

    private void showErrorMessage() {
        ToastBuilder.createSnackBar(scrollView,"Something wrong");
    }


    private boolean checkGPSOn() {
        LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            setOnIndicatorGPS();
            return true;
        } else {
            setOffIndicatorGPS();
            return false;
        }
    }

    private boolean checkNetworkOn() {
        LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            setOnIndicatorNetwork();
            return true;
        } else {
            setOffIndicatorNetwork();
            return false;
        }
    }


    private boolean checkPermissionGPSLocation() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void toAskPermissionGPSLocation() {
        int permissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        }
    }

    class LocationListenerClass implements LocationListener {
        private final int STATUS_DISABLE = 31415;
        private final int STATUS_ENABLE = 15143;

        @Override
        public void onLocationChanged(Location location) {
            readLocation(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            checkProviderStatus(provider, status);
        }

        @Override
        public void onProviderEnabled(String provider) {
            checkProviderStatus(provider, STATUS_ENABLE);
            readLocation(locationManager.getLastKnownLocation(provider));
        }

        @Override
        public void onProviderDisabled(String provider) {
            checkProviderStatus(provider, STATUS_DISABLE);
        }

        private void checkProviderStatus(String provider, int status) {
            if(provider.equals(LocationManager.GPS_PROVIDER)) {
                switch (status) {
                    case STATUS_ENABLE:
                    case LocationProvider.AVAILABLE:
                        setOnIndicatorGPS();
                        break;
                    case STATUS_DISABLE:
                    case LocationProvider.OUT_OF_SERVICE:
                    case LocationProvider.TEMPORARILY_UNAVAILABLE:
                        setOffIndicatorGPS();
                        break;
                }
            }
            if(provider.equals(LocationManager.NETWORK_PROVIDER)) {
                switch (status) {
                    case STATUS_ENABLE:
                    case LocationProvider.AVAILABLE:
                        setOnIndicatorNetwork();
                        break;
                    case STATUS_DISABLE:
                    case LocationProvider.OUT_OF_SERVICE:
                    case LocationProvider.TEMPORARILY_UNAVAILABLE:
                        setOffIndicatorNetwork();
                        break;
                }
            }
        }

        private void readLocation(Location lastKnownLocation) {
            City city = findCity(lastKnownLocation);
            Country country = findCountry(city);
            Widget widget = null;

            if(isCorrectnessWidgetData(city, country)) {
                widget = createWidget(city);
            }

            if(widget == null) {
                showErrorMessage();
            }
            else {
                showResult(city, country);
            }
        }

        private City findCity(Location lastKnownLocation) {
            CityFinder cityFinder = new CityFinder(getApplicationContext(), lastKnownLocation);
            return cityFinder.find();
        }

        private Country findCountry(City city) {
            CountryNameWriter nameWriter = new CountryNameWriter();
            return new Country(city.getCountryCode(), nameWriter.findNameWith(city.getCountryCode()));
        }
    }

    private void setOnIndicatorGPS() {
        indicatorGpsOn.setVisibility(View.VISIBLE);
        indicatorGpsOff.setVisibility(View.INVISIBLE);
    }

    private void setOffIndicatorGPS() {
        indicatorGpsOff.setVisibility(View.VISIBLE);
        indicatorGpsOn.setVisibility(View.INVISIBLE);
    }

    private void setOnIndicatorNetwork() {
        indicatorNetworkOn.setVisibility(View.VISIBLE);
        indicatorNetworkOff.setVisibility(View.INVISIBLE);
    }

    private void setOffIndicatorNetwork() {
        indicatorNetworkOff.setVisibility(View.VISIBLE);
        indicatorNetworkOn.setVisibility(View.INVISIBLE);

    }

    //@Override
    //public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    //    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    //    switch (requestCode) {
    //        case REQUEST_CODE_PERMISSION_FINE_LOCATION:
    //            permissionGPS = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
//
    //        case REQUEST_CODE_PERMISSION_COARSE_LOCATION:
    //            permissionNetwork = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
    //    }
    //}




}
