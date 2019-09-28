package com.orlinskas.cyberpunk.ui.forecast;

public interface ForecastUpdateListener {
    void onUpdateFinished(String name);
    void onUpdateFailed(String message);
}
