package com.orlinskas.cyberpunk.forecast;

import java.util.ArrayList;

class AveragerValue {

    static double average(ArrayList<Double> values) {
        double valueSum = 0;

        for(Double value : values) {
            valueSum = valueSum + value;
        }

        try {
            return valueSum/values.size();
        } catch (Exception e) {
            return 0;
        }
    }

    static double sum(ArrayList<Double> values) {
        double valueSum = 0;

        for(Double value : values) {
            valueSum = valueSum + value;
        }

        return valueSum;
    }

}
