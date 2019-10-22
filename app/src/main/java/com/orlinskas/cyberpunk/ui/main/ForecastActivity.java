package com.orlinskas.cyberpunk.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.orlinskas.cyberpunk.ActivityOpener;
import com.orlinskas.cyberpunk.R;
import com.orlinskas.cyberpunk.ToastBuilder;
import com.orlinskas.cyberpunk.specification.WidgetEmptySpecification;
import com.orlinskas.cyberpunk.ui.app.HelpActivity;
import com.orlinskas.cyberpunk.ui.app.ContactsActivity;
import com.orlinskas.cyberpunk.widgetApp.Widget;
import com.orlinskas.cyberpunk.widgetApp.WidgetRepository;

import java.util.ArrayList;

public class ForecastActivity extends AppCompatActivity {
    private ArrayList<Widget> widgets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ForecastSectionsPagerAdapter forecastSectionsPagerAdapter = new ForecastSectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(forecastSectionsPagerAdapter);
        final TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        int position = 0;

        if(getIntent().hasExtra("myWidgetID")) {
            int currentWidgetID = getIntent().getIntExtra("myWidgetID", 0);
            WidgetRepository widgetRepository = new WidgetRepository(this);
            try {
                widgets = widgetRepository.query(new WidgetEmptySpecification());
            } catch (Exception e) {
                ToastBuilder.create(getBaseContext(),"Critical error, reinstall");
                e.printStackTrace();
            }
            for(Widget widget : widgets) {
                if(widget.getId() == currentWidgetID) {
                    position = widgets.indexOf(widget);
                }
            }
        }
        viewPager.setCurrentItem(position);
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_repost:
                Intent sendIntent = new Intent();
                sendIntent.setType("text/plain");
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,  R.string.share_text);
                startActivity(Intent.createChooser(sendIntent, getString(R.string.share)));
                return true;
            case R.id.action_help_pls:
                ActivityOpener.openActivity(getApplicationContext(), HelpActivity.class);
                return true;
            case R.id.action_message:
                ActivityOpener.openActivity(getApplicationContext(), ContactsActivity.class);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}