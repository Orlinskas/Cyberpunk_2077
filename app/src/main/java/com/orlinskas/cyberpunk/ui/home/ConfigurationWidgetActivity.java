package com.orlinskas.cyberpunk.ui.home;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.orlinskas.cyberpunk.R;
import com.orlinskas.cyberpunk.ToastBuilder;
import com.orlinskas.cyberpunk.preferences.Preferences;
import com.orlinskas.cyberpunk.specification.WidgetEmptySpecification;
import com.orlinskas.cyberpunk.widget.Widget;
import com.orlinskas.cyberpunk.widget.WidgetRepository;

import java.util.ArrayList;

import static com.orlinskas.cyberpunk.preferences.Preferences.APP_WIDGET_ID_DEPENDS;
import static com.orlinskas.cyberpunk.preferences.Preferences.MY_WIDGET_ID_DEPENDS;
import static com.orlinskas.cyberpunk.preferences.Preferences.SETTINGS;

public class ConfigurationWidgetActivity extends Activity {
    private int appWidgetID = AppWidgetManager.INVALID_APPWIDGET_ID;
    private Intent resultValue;
    private Spinner spinnerWidgetList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            appWidgetID = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        if (appWidgetID == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }

        resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetID);
        resultValue.putExtra(WidgetTroubleshooter.ACTION, WidgetTroubleshooter.ACTION_CREATE);
        setResult(RESULT_CANCELED, resultValue); //сформировал негативный ответ на случай выхода

        setContentView(R.layout.config_layout);

        try {
            ArrayList<Widget> widgets = findWidgetsInRepository();
            spinnerWidgetList = findViewById(R.id.config_layout_spn);
            spinnerWidgetList.getBackground().setColorFilter(getResources().getColor(R.color.colorCyberpunkYellow), PorterDuff.Mode.SRC_ATOP);
            setSpinnerAdapter(widgets);
            if(widgets.size() == 0) {
                ToastBuilder.create(getBaseContext(),getString(R.string.greate_widget_in_app));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private ArrayList<Widget> findWidgetsInRepository() throws Exception {
        WidgetRepository repository = new WidgetRepository(this);
        return repository.query(new WidgetEmptySpecification());
    }

    private void setSpinnerAdapter(ArrayList<Widget> widgets) {
        ArrayAdapter<Widget> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, widgets);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerWidgetList.setAdapter(adapter);
    }

    public void onClickCreate(View view) {
        final Animation buttonClickAnim = AnimationUtils.loadAnimation(this, R.anim.animation_button);
        view.startAnimation(buttonClickAnim);

        if(spinnerWidgetList.getSelectedItem() == null) {
            ToastBuilder.create(getBaseContext(),"First, create a widget in the app");
        }
        else {
            Widget widget = (Widget) spinnerWidgetList.getSelectedItem();
            int myWidgetID = widget.getId();
            Preferences preferences = Preferences.getInstance(this, SETTINGS);
            //запомнил соотношение - ID который дал андроид/ID моего объекта виджета
            preferences.saveData(MY_WIDGET_ID_DEPENDS + appWidgetID, myWidgetID);
            //и наоборот
            preferences.saveData(APP_WIDGET_ID_DEPENDS + myWidgetID, appWidgetID);

            setResult(RESULT_OK, resultValue); //отправил положительный ответ
            finish();
        }
    }
}

