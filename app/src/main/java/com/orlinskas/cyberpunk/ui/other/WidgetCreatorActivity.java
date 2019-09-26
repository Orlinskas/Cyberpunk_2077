package com.orlinskas.cyberpunk.ui.other;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.orlinskas.cyberpunk.ActivityOpener;
import com.orlinskas.cyberpunk.City;
import com.orlinskas.cyberpunk.Country;
import com.orlinskas.cyberpunk.R;
import com.orlinskas.cyberpunk.ToastBuilder;
import com.orlinskas.cyberpunk.location.CityFinder;
import com.orlinskas.cyberpunk.post.CountryNameWriter;
import com.orlinskas.cyberpunk.ui.main.MainActivity;
import com.orlinskas.cyberpunk.widget.Widget;
import com.orlinskas.cyberpunk.widget.WidgetCopyChecker;
import com.orlinskas.cyberpunk.widget.WidgetCreator;
import com.orlinskas.cyberpunk.widget.WidgetRepository;

@SuppressLint("MissingPermission")
public class WidgetCreatorActivity extends AppCompatActivity {
    private TextView countryName, cityName;
    private TextView indicatorGpsOn, indicatorNetworkOn, indicatorGpsOff, indicatorNetworkOff;
    private Country country;
    private City city;
    private final int REQUEST_CODE_PERMISSION_FINE_LOCATION = 1001;
    private final int REQUEST_CODE_PERMISSION_COARSE_LOCATION = 1002;
    private boolean permissionGPS;
    private boolean permissionNetwork;
    private SearchLocationTask searchLocationTask;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget_creator);
        Button searchLocationBtn = findViewById(R.id.activity_city_data_generator_btn_gps);
        Button openListLocationsBtn = findViewById(R.id.activity_city_data_generator_btn_open_list);
        Button createWidgetButton = findViewById(R.id.activity_city_data_generator_btn_start);

        countryName = findViewById(R.id.activity_city_data_generator_tv_country_name);
        cityName = findViewById(R.id.activity_city_data_generator_tv_city_name);

        indicatorGpsOn = findViewById(R.id.activity_city_data_generator_tv_gps_on);
        indicatorGpsOff = findViewById(R.id.activity_city_data_generator_tv_gps_off);
        indicatorNetworkOn = findViewById(R.id.activity_city_data_generator_tv_network_on);
        indicatorNetworkOff = findViewById(R.id.activity_city_data_generator_tv_network_off);

        checkGPSOn();
        checkNetworkOn();

        searchLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isSearchTaskRunning()){
                    ToastBuilder.create(getApplicationContext(), getString(R.string.search_working));
                }
                else {
                    permissionGPS = checkPermissionGPSLocation();
                    permissionNetwork = checkPermissionNetworkLocation();

                    if (!permissionGPS) {
                        toAskPermissionGPSLocation();
                    }
                    if (!permissionNetwork) {
                        toAskPermissionNetworkLocation();
                    }

                    String provider = chooseAvailableProvider();

                    if (provider == null) {
                        ToastBuilder.create(getApplicationContext(), getString(R.string.acces_permission));
                    } else {
                        runSearchLocationTask(provider);
                    }
                }
            }
        });

        openListLocationsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClickAnim);
                Intent intent = new Intent(getApplicationContext(), CountryListActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        createWidgetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClickAnim);
                createWidget();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION_FINE_LOCATION:
                permissionGPS = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;

            case REQUEST_CODE_PERMISSION_COARSE_LOCATION:
                permissionNetwork = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            city = (City) data.getSerializableExtra("city");
            country = (Country) data.getSerializableExtra("country");
        }
        checkAndShowLocationData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkNetworkOn();
        checkGPSOn();
    }

    @Override
    protected void onPause() {
        super.onPause();
        checkNetworkOn();
        checkGPSOn();
        if(isSearchTaskRunning()) {
            stopSearchLocationTask();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(isSearchTaskRunning()) {
            stopSearchLocationTask();
        }
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

    private void setOnIndicatorGPS() {
        indicatorGpsOn.setVisibility(View.VISIBLE);
        indicatorGpsOff.setVisibility(View.INVISIBLE);
    }

    private void setOffIndicatorGPS() {
        indicatorGpsOff.setVisibility(View.VISIBLE);
        indicatorGpsOn.setVisibility(View.INVISIBLE);
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

    private void setOnIndicatorNetwork() {
        indicatorNetworkOn.setVisibility(View.VISIBLE);
        indicatorNetworkOff.setVisibility(View.INVISIBLE);
    }

    private void setOffIndicatorNetwork() {
        indicatorNetworkOff.setVisibility(View.VISIBLE);
        indicatorNetworkOn.setVisibility(View.INVISIBLE);

    }

    private boolean checkPermissionGPSLocation() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean checkPermissionNetworkLocation() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void toAskPermissionGPSLocation() {
        int permissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_PERMISSION_FINE_LOCATION);
        }
    }

    private void toAskPermissionNetworkLocation() {
        int permissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);

        if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_CODE_PERMISSION_COARSE_LOCATION);
        }
    }


    private String chooseAvailableProvider() {
        if(checkGPSOn() & checkPermissionGPSLocation()) {
            return LocationManager.GPS_PROVIDER;
        }
        if (checkNetworkOn() & checkPermissionNetworkLocation()) {
            return LocationManager.NETWORK_PROVIDER;
        }
        else {
            return null;
        }
    }

    private void runSearchLocationTask(String provider) {
        searchLocationTask = new SearchLocationTask(provider);
        searchLocationTask.execute();
    }

    private boolean isSearchTaskRunning() {
        if(searchLocationTask == null){
            return false;
        }
        return searchLocationTask.getStatus() == AsyncTask.Status.RUNNING;
    }

    private void stopSearchLocationTask() {
        searchLocationTask.cancel(true);
        searchLocationTask = null;
    }

    @SuppressLint("StaticFieldLeak")
    private class SearchLocationTask extends AsyncTask<Void, Void, City> {
        private Location location;
        private String provider;
        private LocationManager locationManager;

        SearchLocationTask(String provider) {
            this.provider = provider;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            ToastBuilder.create(getApplicationContext(), getString(R.string.search));

            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            LocationListener locationListener = new LocationListener() {

                @Override
                public void onLocationChanged(Location location) {
                    setLocation(location);
                }
                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                    setLocation(locationManager.getLastKnownLocation(provider));
                }
                @Override
                public void onProviderEnabled(String provider) {
                    setLocation(locationManager.getLastKnownLocation(provider));
                }
                @Override
                public void onProviderDisabled(String provider) {
                }
            };
            locationManager.requestLocationUpdates(provider, 50, 10, locationListener);
        }

        @Override
        protected City doInBackground(Void... voids) {
            while (true) {
                if(location != null){
                    break;
                }
                if(isCancelled()){
                    break;
                }
            }
            CityFinder cityFinder = new CityFinder(getApplicationContext(), location);
            return cityFinder.find();
        }

        private void setLocation(Location location) {
            this.location = location;
        }

        @Override
        protected void onPostExecute(City city) {
            super.onPostExecute(city);
            progressBar.setVisibility(View.INVISIBLE);
            showResultSearchTask(city);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            progressBar.setVisibility(View.INVISIBLE);
        }

    }

    private void showResultSearchTask(City city) {
        if(city == null) {
            ToastBuilder.create(getApplicationContext(), getString(R.string.not_found));
        }
        else {
            this.city = city;
            CountryNameWriter nameWriter = new CountryNameWriter();
            this.country = new Country(city.getCountryCode(), nameWriter.findNameWith(city.getCountryCode()));
            checkAndShowLocationData();
        }
    }


    private void checkAndShowLocationData() {
        if(isHasCountry()) {
            showCountryName();
            if(isHasCity()) {
                showCityName();
            }
        }
    }

    private boolean isHasCountry() {
        return country != null;
    }

    private boolean isHasCity() {
        return city != null;
    }

    private void showCountryName() {
        countryName.setText(country.getName());
    }

    private void showCityName() {
        cityName.setText(city.getName());
    }


    private void createWidget() {
        if(isCorrectnessWidgetData()) {
            try {
                WidgetCreator widgetCreator = new WidgetCreator();
                WidgetRepository widgetRepository = new WidgetRepository(getApplicationContext());
                Widget widget = widgetCreator.create(city);

                WidgetCopyChecker checker = new WidgetCopyChecker(this, widget);
                if(checker.check()){
                    ToastBuilder.create(getApplicationContext(),getString(R.string.almost_have));
                }
                else {
                    widgetRepository.add(widget);
                    this.finish();
                    ActivityOpener.openActivity(this, MainActivity.class);
                }
            } catch (Exception e) {
                e.printStackTrace();
                ToastBuilder.create(getApplicationContext(),getString(R.string.error_widget_create));
            }
        }
    }

    private boolean isCorrectnessWidgetData() {
        if(country != null & city != null) {
            if(country.getCode().equals(city.getCountryCode())) {
                return true;
            }
            else {
                ToastBuilder.create(getApplicationContext(), getString(R.string.uncorrect_place));
                return false;
            }
        }
        else {
            ToastBuilder.create(getApplicationContext(), getString(R.string.not_writed_plase));
            return false;
        }

    }
}
