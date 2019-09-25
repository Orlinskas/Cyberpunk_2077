package com.orlinskas.cyberpunk.preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {
    private static Preferences preferences;
    private SharedPreferences sharedPreferences;
    public static final String SETTINGS = "settings";
    static final String APP_TEST = "test";
    public static final String MY_WIDGET_ID_DEPENDS = "dependenceIDMy-";
    public static final String APP_WIDGET_ID_DEPENDS = "dependenceIDApp-";
    public static final String WIDGET_LAST_UPDATE = "lastUpdateID-";
    public static final String WIDGET_DAY_NUMBER = "dayNumberID-";

    public static Preferences getInstance(Context context, String preferencesName) {
        if (preferences == null) {
            preferences = new Preferences(context, preferencesName);
        }
        return preferences;
    }

    private Preferences(Context context, String preferencesName) {
        sharedPreferences = context.getSharedPreferences(preferencesName,Context.MODE_PRIVATE);
    }

    public void saveData(String key, String value) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putString(key, value);
        prefsEditor.apply();
    }

    public String getData(String key, String defValue) {
        if (sharedPreferences != null) {
            return sharedPreferences.getString(key, "");
        }
        return "";
    }

    public void saveData(String key, int value) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putInt(key, value);
        prefsEditor.apply();
    }

    public int getData(String key, int defValue) {
        if (sharedPreferences != null) {
            return sharedPreferences.getInt(key, defValue);
        }
        return 0;
    }

    public void saveData(String key, boolean value) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putBoolean(key, value);
        prefsEditor.apply();
    }

    public boolean getData(String key, boolean defValue) {
        if (sharedPreferences != null) {
            return sharedPreferences.getBoolean(key, defValue);
        }
        return false;
    }
}