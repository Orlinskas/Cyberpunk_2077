package com.orlinskas.cyberpunk.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.orlinskas.cyberpunk.ToastBuilder;
import com.orlinskas.cyberpunk.specification.WidgetEmptySpecification;
import com.orlinskas.cyberpunk.ui.forecast.ForecastFragmentView;
import com.orlinskas.cyberpunk.widget.Widget;
import com.orlinskas.cyberpunk.widget.WidgetRepository;

import java.util.ArrayList;

public class ForecastSectionsPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Widget> widgets;

    ForecastSectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        WidgetRepository widgetRepository = new WidgetRepository(context);
        try {
            widgets = widgetRepository.query(new WidgetEmptySpecification());
        } catch (Exception e) {
            ToastBuilder.create(context,"Critical error, reinstall");
            e.printStackTrace();
        }
    }

    @Override
    public Fragment getItem(int position) {
        ForecastFragmentView forecastFragmentView = new ForecastFragmentView();
        Bundle bundle = new Bundle();
        bundle.putInt("widgetID", widgets.get(position).getId());
        forecastFragmentView.setArguments(bundle);
        return forecastFragmentView;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return widgets.get(position).getCity().getName();
    }

    @Override
    public int getCount() {
        return widgets.size();
    }
}