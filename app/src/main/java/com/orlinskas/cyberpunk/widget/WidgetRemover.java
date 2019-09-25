package com.orlinskas.cyberpunk.widget;

import android.content.Context;

public class WidgetRemover {
    private Context context;

    public WidgetRemover(Context context) {
        this.context = context;
    }

    public void remove(int widgetID) {
        Widget widget = findWidgetInRepo(widgetID);
        removeWidgetFromRepo(widget);
    }

    private void removeWidgetFromRepo(Widget widget) {
        WidgetRepository repository = new WidgetRepository(context);
        try {
            repository.remote(widget);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Widget findWidgetInRepo(int widgetID) {
        Widget widget = null;
        WidgetRepository repository = new WidgetRepository(context);
        try {
            widget = repository.find(widgetID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return widget;
    }
}
