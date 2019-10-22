package com.orlinskas.cyberpunk.specification;

import com.orlinskas.cyberpunk.widgetApp.Widget;

public class WidgetCitySpecification implements Specification <Widget> {
    private Widget widget;

    public WidgetCitySpecification(Widget widget) {
        this.widget = widget;
    }

    @Override
    public boolean specified(Widget object) {
        return widget.getCity().equals(object.getCity());
    }
}
