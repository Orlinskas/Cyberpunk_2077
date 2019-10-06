package com.orlinskas.cyberpunk.ui.home;

import com.orlinskas.cyberpunk.date.DateFormat;
import com.orlinskas.cyberpunk.date.DateHelper;
import com.orlinskas.cyberpunk.forecast.Forecast;

public class ConsoleMessageBuilder {
    private Forecast forecast;

    public ConsoleMessageBuilder(Forecast forecast) {
        this.forecast = forecast;
    }

    public String buildWarning() {
        StringBuilder message = new StringBuilder();
        message.append("--").append("last update").append(" ");
        String date = DateHelper.getCurrent(DateFormat.HH_MM_SS);
        message.append(date);
        message.append(".");

        return message.toString();
    }

    public String buildTimezone() {
        StringBuilder message = new StringBuilder();
        message.append("--").append("timezone").append(" ");

        message.append(getTimezone());

        message.append(".");

        return message.toString();
    }

    private String getTimezone() {
        int timezone = forecast.getDayWeathers().get(0).getTimezone();
        if (timezone > 0) {
            return "UTC +" + timezone;
        }
        return "UTC" + timezone;
    }

    public String buildBatteryStatus() {
        StringBuilder message = new StringBuilder();
        String date = DateHelper.getCurrent(DateFormat.MM_SS);
        message.append(date);
        message.append(" ").append("battery_status").append("-");

        message.append(getBatteryStatus());

        message.append("%");
        message.append(".");

        return message.toString();
    }

    private int getBatteryStatus() {
        return 65;
    }

    public String buildWifiStatus() {
        StringBuilder message = new StringBuilder();
        String date = DateHelper.getCurrent(DateFormat.MM_SS);
        message.append(date);
        message.append(" ").append("wifi_status").append("-");

        if(isWifiEnable()) {
            message.append("enable");
        }
        else {
            message.append("disable");
        }

        message.append(".");

        return message.toString();
    }

    private boolean isWifiEnable() {
        return true;
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
        return 5;
    }
}
