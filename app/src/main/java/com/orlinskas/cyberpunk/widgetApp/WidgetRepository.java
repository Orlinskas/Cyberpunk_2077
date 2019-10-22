package com.orlinskas.cyberpunk.widgetApp;

import android.content.Context;

import com.orlinskas.cyberpunk.specification.Specification;

import java.util.ArrayList;

public class WidgetRepository {
    private WidgetSerializer serializer;

    public WidgetRepository(Context context) {
        serializer = new WidgetSerializer(context);
    }

    public boolean add(Widget widget) {
        try {
            WidgetsList widgetsList = serializer.loadList();
            ArrayList<Widget> widgets = widgetsList.getWidgets();
            widgets.add(widget);
            widgetsList.setWidgets(widgets);
            serializer.saveList(widgetsList);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void update(Widget widget) throws Exception {
        WidgetsList widgetsList = serializer.loadList();
        ArrayList<Widget> widgets = widgetsList.getWidgets();
        int index = widgets.indexOf(widget);
        widgets.remove(widget);
        widgets.add(index, widget);
        widgetsList.setWidgets(widgets);
        serializer.saveList(widgetsList);
    }

    public boolean remote(Widget widget) {
        try {
            WidgetsList widgetsList = serializer.loadList();
            ArrayList<Widget> widgets = widgetsList.getWidgets();
            widgets.remove(widget);
            widgetsList.setWidgets(widgets);
            serializer.saveList(widgetsList);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Widget find(int widgetId) throws Exception {
        WidgetsList widgetsList = serializer.loadList();
        ArrayList<Widget> widgets = widgetsList.getWidgets();
        for (Widget widget : widgets) {
            if(widget.getId() == widgetId){
                return widget;
            }
        }

        return null;
    }

    public ArrayList<Widget> query(Specification<Widget> specification) throws Exception {
        ArrayList<Widget> queryWidgets = new ArrayList<>();

        WidgetsList widgetsList = serializer.loadList();
        ArrayList<Widget> widgets = widgetsList.getWidgets();
        for (Widget widget : widgets) {
            if(specification.specified(widget)){
                queryWidgets.add(widget);
            }
        }

        return queryWidgets;
    }
}
