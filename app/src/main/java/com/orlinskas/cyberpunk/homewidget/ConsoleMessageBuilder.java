package com.orlinskas.cyberpunk.homewidget;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Environment;
import android.os.StatFs;

import com.orlinskas.cyberpunk.date.DateFormat;
import com.orlinskas.cyberpunk.date.DateHelper;
import com.orlinskas.cyberpunk.forecast.Forecast;
import com.orlinskas.cyberpunk.forecast.Weather;

import java.text.DecimalFormat;

public class ConsoleMessageBuilder {
    private Forecast forecast;
    private Context context;

    public ConsoleMessageBuilder(Forecast forecast, Context context) {
        this.forecast = forecast;
        this.context = context;
    }

    public String buildLastUpdate() {
        StringBuilder message = new StringBuilder();
        message.append("--").append("last update").append(" ");
        String date = DateHelper.getCurrent(DateFormat.HH_MM_SS);
        message.append(date);
        message.append(".");

        return message.toString();
    }

    public String buildTimezone() {
        return getTimezone();
    }

    private String getTimezone() {
        int timezone = forecast.getDayWeathers().get(0).getTimezone();
        if (timezone > 0) {
            return "UTC +" + timezone;
        }
        return "UTC" + timezone;
    }

    public String buildRAM() {
        return "--" + "RAM available" + " " + getRAM() + ".";
    }

    private String getRAM() {
        ActivityManager actManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
        actManager.getMemoryInfo(memInfo);
        if(memInfo.lowMemory) {
            return "LOW!";
        }
        return bytesToHuman(memInfo.availMem);
    }

    public String buildMemoryTotal() {
        return "--" + "memory_total" + " " + getMemoryTotal() + ".";
    }

    private String getMemoryTotal() {
        StatFs statFs = new StatFs(Environment.getRootDirectory().getAbsolutePath());
        long total = (statFs.getBlockCountLong() * statFs.getBlockSizeLong());
        return bytesToHuman(total);
    }

    public String buildMemoryFree() {
        return "--" + "memory_free" + " " + getMemoryFree() + ".";
    }

    private String getMemoryFree() {
        StatFs statFs = new StatFs(Environment.getRootDirectory().getAbsolutePath());
        long   free   = (statFs.getAvailableBlocksLong() * statFs.getBlockSizeLong());

        return bytesToHuman(free);
    }

    private String bytesToHuman (long size) {
        long Kb = 1024;
        long Mb = Kb * 1024;
        long Gb = Mb * 1024;
        long Tb = Gb * 1024;
        long Pb = Tb * 1024;
        long Eb = Pb * 1024;

        if (size <  Kb)   return floatForm(       size      ) + " byte";
        if (size < Mb)    return floatForm((double)size / Kb) + " Kb";
        if (size < Gb)    return floatForm((double)size / Mb) + " Mb";
        if (size < Tb)    return floatForm((double)size / Gb) + " Gb";
        if (size < Pb)    return floatForm((double)size / Tb) + " Tb";
        if (size < Eb)    return floatForm((double)size / Pb) + " Pb";

        return floatForm((double)size / Eb) + " Eb";

    }

    private String floatForm (double d) {
        return new DecimalFormat("#.##").format(d);
    }

    public String buildWifiStatus() {
        return "--" + "wifi" + " " + getWifiName() + ".";
    }

    private String getWifiName() {
        WifiManager manager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (manager.isWifiEnabled()) {
            WifiInfo wifiInfo = manager.getConnectionInfo();
            if (wifiInfo != null) {
                NetworkInfo.DetailedState state = WifiInfo.getDetailedStateOf(wifiInfo.getSupplicantState());
                if (state == NetworkInfo.DetailedState.CONNECTED || state == NetworkInfo.DetailedState.OBTAINING_IPADDR) {
                    return wifiInfo.getSSID();
                }
            }
        }
        return "OFF";
    }

    public String buildPrecipitationValue() {
        StringBuilder message = new StringBuilder();
        String date = DateHelper.getCurrent(DateFormat.MM_SS);
        message.append(date);
        message.append(" ").append("precipitation").append("-");

        message.append(getPrecipitationValue());

        message.append("mm/day.");

        return message.toString();
    }

    private int getPrecipitationValue() {
        int value = 0;
        for(Weather weather : forecast.getDayWeathers()) {
            value = value + (int) (weather.getRainVolume() + weather.getSnowVolume());
        }

        return value;
    }

    public String buildBatteryStatus() {
        return "--" + "battery" + " " + getBatteryStatus() + ".";
    }

    private int getBatteryStatus() {
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, filter);

        assert batteryStatus != null;
        return batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
    }
}
