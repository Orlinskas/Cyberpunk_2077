package com.orlinskas.cyberpunk.ui.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.orlinskas.cyberpunk.City;
import com.orlinskas.cyberpunk.Country;
import com.orlinskas.cyberpunk.location.CityFinder;
import com.orlinskas.cyberpunk.post.CountryNameWriter;
import com.orlinskas.cyberpunk.widgetApp.Widget;

import java.util.concurrent.TimeUnit;

import static com.orlinskas.cyberpunk.ui.app.WidgetCreatorContract.*;

@SuppressLint({"HandlerLeak", "MissingPermission"})
public class WidgetCreatorPresenter implements WidgetCreatorContract.Presenter, LocationListener{
    private Context context;
    private WidgetCreatorContract.Model model;
    private WidgetCreatorContract.View view;
    private Handler viewButtonHandler;
    private Handler viewProgressBarHandler;
    private Thread progressBarThread;
    private boolean isCanceledThread = false;
    private LocationManager locationManager;
    private LocationListener locationListener;

    WidgetCreatorPresenter(Context context, WidgetCreatorContract.View view, LocationManager locationManager) {
        this.view = view;
        this.context = context;
        this.locationManager = locationManager;
    }

    @Override
    public void startWork() {
        model = new WidgetCreatorModel(context, this);

        viewButtonHandler = new Handler() {
            @Override
            public void handleMessage(Message BUTTON_KEY) {
                view.setButtonStatus(BUTTON_KEY.what, BUTTON_KEY.arg1);
            }
        };

        viewProgressBarHandler = new Handler() {
            @Override
            public void handleMessage(Message IMAGE_KEY) {
                view.setProgressImage(IMAGE_KEY.what);
            }
        };
    }

    @Override
    public void onClickChooseLocation() {
        new Thread(new Runnable() {
            Message message;
            public void run() {
                message = viewButtonHandler.obtainMessage(BUTTON_OPEN_LIST_ACTIVITY, STATUS_ON,0);
                viewButtonHandler.sendMessage(message);

                view.openActivityForResult(CountryListActivity.class);

                try {
                    TimeUnit.MILLISECONDS.sleep(150);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                message = viewButtonHandler.obtainMessage(BUTTON_OPEN_LIST_ACTIVITY, STATUS_OFF,0);
                viewButtonHandler.sendMessage(message);
            }
        }).start();
    }

    @Override
    public void onClickSearchLocation() {
        view.toAskGPSPermission();
    }

    @Override
    public void onClickCreateWidget(final Country country, final City city) {
        new Thread(new Runnable() {
            Message message;
            public void run() {
                message = viewButtonHandler.obtainMessage(BUTTON_CREATE_WIDGET,STATUS_ON,0);
                viewButtonHandler.sendMessage(message);

                model.createWidget(country, city);

                try {
                    TimeUnit.MILLISECONDS.sleep(150);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                message = viewButtonHandler.obtainMessage(BUTTON_CREATE_WIDGET,STATUS_OFF,0);
                viewButtonHandler.sendMessage(message);
            }
        }).start();
    }

    @Override
    public void onResult(Widget widget) {
        if(widget != null && widget.getId() != EMPTY_WIDGET) {
            view.openForecastActivity(widget.getId());
        }
        else {
            view.doSnackBar("Some data is incorrect");
        }
    }

    @Override
    public void startSearchLocation() {
        startProgressBar();
        view.setButtonStatus(BUTTON_START_SEARCH_LOCATION, STATUS_ON);
        locationListener = this;
        locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, locationListener, null);
        locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, locationListener, null);
        if (!isGPSEnable()) {
            view.doSnackBar("Turn on GPS!");
        }
    }

    @Override
    public void stopSearchLocation() {
        if(locationManager != null && locationListener != null) {
            locationManager.removeUpdates(locationListener);
            view.setButtonStatus(BUTTON_START_SEARCH_LOCATION, STATUS_OFF);
            stopProgressBar();
        }
    }

    @Override
    public void startProgressBar() {
        if(progressBarThread == null || progressBarThread.getState() == Thread.State.NEW
                || progressBarThread.getState() == Thread.State.TERMINATED) {

            isCanceledThread = false;

            progressBarThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    int count = 1;
                    while (true) {
                        try {
                            Message message = viewProgressBarHandler.obtainMessage(count);
                            viewProgressBarHandler.sendMessage(message);
                            count++;
                            if(count == 5) {
                                count = 1;
                            }
                            if(isCanceledThread) {
                                message = viewProgressBarHandler.obtainMessage(1);
                                viewProgressBarHandler.sendMessage(message);
                                break;
                            }
                            TimeUnit.MILLISECONDS.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            progressBarThread.start();
        }
    }

    @Override
    public void stopProgressBar() {
        isCanceledThread = true;
    }

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
        checkProviderStatus(provider, STATUS_ON);
        readLocation(locationManager.getLastKnownLocation(provider));
    }

    @Override
    public void onProviderDisabled(String provider) {
        checkProviderStatus(provider, STATUS_OFF);
    }

    private void checkProviderStatus(String provider, int STATUS) {
        if(provider.equals(LocationManager.GPS_PROVIDER)) {
            switch (STATUS) {
                case STATUS_ON:
                case LocationProvider.AVAILABLE:
                    view.setStatusIndicatorGPS(STATUS_ON);
                    break;
                case STATUS_OFF:
                case LocationProvider.OUT_OF_SERVICE:
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    view.setStatusIndicatorGPS(STATUS_OFF);
                    break;
            }
        }
        if(provider.equals(LocationManager.NETWORK_PROVIDER)) {
            switch (STATUS) {
                case STATUS_ON:
                case LocationProvider.AVAILABLE:
                    view.setStatusIndicatorINTERNET(STATUS_ON);
                    break;
                case STATUS_OFF:
                case LocationProvider.OUT_OF_SERVICE:
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    view.setStatusIndicatorINTERNET(STATUS_OFF);
                    break;
            }
        }
    }

    private void readLocation(Location lastKnownLocation) {
        if (context != null && lastKnownLocation != null) {
            City city = findCity(lastKnownLocation);
            Country country = findCountry(city);
            view.setCity(city);
            view.setCountry(country);
            stopProgressBar();
        }
    }

    private City findCity(Location lastKnownLocation) {
        CityFinder cityFinder = new CityFinder(context, lastKnownLocation);
        return cityFinder.find();
    }

    private Country findCountry(City city) {
        CountryNameWriter nameWriter = new CountryNameWriter();
        return new Country(city.getCountryCode(), nameWriter.findNameWith(city.getCountryCode()));
    }

    private boolean isGPSEnable() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    @Override
    public void destroy() {
        stopSearchLocation();
    }
}
