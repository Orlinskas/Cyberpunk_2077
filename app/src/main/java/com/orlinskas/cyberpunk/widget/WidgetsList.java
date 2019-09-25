package com.orlinskas.cyberpunk.widget;

import java.io.Serializable;
import java.util.ArrayList;

class WidgetsList implements Serializable {
    private ArrayList<Widget> widgets = new ArrayList<>();

    ArrayList<Widget> getWidgets() {
        return widgets;
    }

    void setWidgets(ArrayList<Widget> widgets) {
        this.widgets = widgets;
    }
}
