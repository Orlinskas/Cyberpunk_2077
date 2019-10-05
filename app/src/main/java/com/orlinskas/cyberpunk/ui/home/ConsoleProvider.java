package com.orlinskas.cyberpunk.ui.home;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.orlinskas.cyberpunk.R;

import java.util.ArrayList;

public class ConsoleProvider implements RemoteViewsService.RemoteViewsFactory {
    private ArrayList<ConsoleItem> listItemList = new ArrayList<>();
    private Context context;
    private int appWidgetId;

    public ConsoleProvider(Context context, Intent intent) {
        this.context = context;

        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);

        populateListItem();
    }

    private void populateListItem() {
        for (int i = 0; i < 10; i++) {
            ConsoleItem consoleItem = new ConsoleItem();
            consoleItem.command = "Heading" + i;
            listItemList.add(consoleItem);
        }
    }

    @Override
    public int getCount() {
        return listItemList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /*
     *Similar to getView of Adapter where instead of View
     *we return RemoteViews
     *
     */
    @Override
    public RemoteViews getViewAt(int position) {
        final RemoteViews remoteView = new RemoteViews(
                context.getPackageName(), R.layout.console_row);
        ConsoleItem consoleItem = listItemList.get(position);
        remoteView.setTextViewText(R.id.console_row_command, consoleItem.command);

        return remoteView;
    }


    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
    }

    @Override
    public void onDestroy() {
    }

}
