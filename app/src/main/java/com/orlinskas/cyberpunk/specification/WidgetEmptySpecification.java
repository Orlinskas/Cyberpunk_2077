package com.orlinskas.cyberpunk.specification;

import com.orlinskas.cyberpunk.widgetApp.Widget;

public class WidgetEmptySpecification implements Specification <Widget> {
    @Override
    public boolean specified(Widget object) {
        return true;
    }
}
