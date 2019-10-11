package com.orlinskas.cyberpunk.widget;

class WidgetIDGenerator {
    static int getID() {
        int minimum = 1000;
        int maximum = 9999;
        maximum -= minimum;
        return (int) (Math.random() * ++maximum) + minimum;
    }
}
