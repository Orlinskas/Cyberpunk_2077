package com.orlinskas.cyberpunk.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.orlinskas.cyberpunk.specification.WidgetEmptySpecification;
import com.orlinskas.cyberpunk.ui.widget.WidgetFragment;
import com.orlinskas.cyberpunk.widget.Widget;
import com.orlinskas.cyberpunk.widget.WidgetRepository;

import java.util.ArrayList;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Widget> widgets;

    SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        WidgetRepository widgetRepository = new WidgetRepository(context);
        try {
            widgets = widgetRepository.query(new WidgetEmptySpecification());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Fragment getItem(int position) {
        WidgetFragment widgetFragment = new WidgetFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("widgetID", widgets.get(position).getId());
        widgetFragment.setArguments(bundle);
        return widgetFragment;
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