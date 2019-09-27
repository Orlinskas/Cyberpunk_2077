package com.orlinskas.cyberpunk.ui.other;

import android.content.Context;
import android.location.LocationListener;
import android.location.LocationManager;

import com.orlinskas.cyberpunk.City;
import com.orlinskas.cyberpunk.Country;
import com.orlinskas.cyberpunk.request.Request;
import com.orlinskas.cyberpunk.widget.Widget;
import com.orlinskas.cyberpunk.widget.WidgetCopyChecker;
import com.orlinskas.cyberpunk.widget.WidgetCreator;
import com.orlinskas.cyberpunk.widget.WidgetRepository;

public class WidgetCreatorModel implements WidgetCreatorContract.Model {
    private Context context;
    private WidgetCreatorContract.Presenter presenter;
    private Widget emptyWidget;
    private LocationManager locationManager;
    private LocationListener locationListener;


    WidgetCreatorModel(Context context, WidgetCreatorContract.Presenter presenter) {
        this.context = context;
        this.presenter = presenter;
        City emptyCity = new City(404, "Not found", "Need to create", 1.0, 1.0);
        emptyWidget = new Widget(0, emptyCity, new Request("n/a", emptyCity, "n/a", "n/a" , "n/a", "n/a"));
    }

    @Override
    public void createWidget(Country country, City city) {
        try {
            WidgetCreator widgetCreator = new WidgetCreator();
            WidgetRepository widgetRepository = new WidgetRepository(context);
            Widget widget = widgetCreator.create(city);
            WidgetCopyChecker checker = new WidgetCopyChecker(context, widget);
            if(!checker.isHasCopy() && isCorrectnessWidgetData(country, city)) {
                widgetRepository.add(widget);
                presenter.onResult(widget);
            }
            else {
                presenter.onResult(emptyWidget);
            }
        } catch (Exception e) {
            e.printStackTrace();
            presenter.onResult(emptyWidget);
        }
    }

    private boolean isCorrectnessWidgetData(Country country, City city) {
        if(city != null && country != null) {
            return city.getCountryCode().equals(country.getCode());
        }
        else {
            return false;
        }
    }

    @Override
    public void startSearchLocation() {

    }

    @Override
    public void stopSearchLocation() {

    }

    /*class CustomLocationListener implements LocationListener {
        private final int STATUS_DISABLE = 31;
        private final int STATUS_ENABLE = 15;

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

            if(isCorrectnessWidgetData(city, country)) {
                stopSearchLocation(city, country);
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
    }  */


}
