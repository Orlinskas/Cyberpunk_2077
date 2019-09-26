package com.orlinskas.cyberpunk.ui.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.orlinskas.cyberpunk.City;
import com.orlinskas.cyberpunk.FirstRunner;
import com.orlinskas.cyberpunk.R;
import com.orlinskas.cyberpunk.ToastBuilder;
import com.orlinskas.cyberpunk.preferences.FirstRunVerifier;
import com.orlinskas.cyberpunk.request.Request;
import com.orlinskas.cyberpunk.specification.WidgetEmptySpecification;
import com.orlinskas.cyberpunk.ui.other.HelpActivity;
import com.orlinskas.cyberpunk.ui.other.WidgetCreatorActivity;
import com.orlinskas.cyberpunk.ui.widget.AnimatedBackgroundView;
import com.orlinskas.cyberpunk.widget.Widget;
import com.orlinskas.cyberpunk.widget.WidgetRepository;

import java.util.ArrayList;

@SuppressLint("StaticFieldLeak")
public class MainActivityGeneral extends AppCompatActivity {
    private ImageView btnCreate, btnAuthor, btnHelp, btnShare;
    private ImageView progressMenu, progressList;
    private ListView listView;
    private OpenActivityTask openActivityTask;
    private LoadWidgetsListTask loadWidgetsListTask;
    private ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_general);

        btnCreate = findViewById(R.id.activity_main_general_iv_btn_create);
        btnAuthor = findViewById(R.id.activity_main_general_iv_btn_author);
        btnHelp = findViewById(R.id.activity_main_general_iv_btn_help);
        btnShare = findViewById(R.id.activity_main_general_iv_btn_share);
        progressMenu = findViewById(R.id.activity_main_general_im_progress_menu);
        progressList = findViewById(R.id.activity_main_general_im_progress_list);
        listView = findViewById(R.id.activity_main_general_lv_widgets);
        scrollView = findViewById(R.id.activity_main_general_create_sv);

        showAnimationsHead();
        processFirstRun(getApplicationContext());
        loadAndShowWidgetsList();
    }

    private void showAnimationsHead() {
        Runnable runnable = new Runnable() {
            public void run() {
                RelativeLayout relativeLayout = findViewById(R.id.activity_main_create_rl_head_anim);
                relativeLayout.addView(new AnimatedBackgroundView(getApplicationContext()));
            }
        };
        runnable.run();
    }

    private void processFirstRun(Context applicationContext) {
        FirstRunVerifier firstRunVerifier = new FirstRunVerifier(applicationContext);

        if(!firstRunVerifier.check()) {
            ToastBuilder.createSnackBar(scrollView, "Wake the f*** Up Samurai");

            FirstRunner firstRunner = new FirstRunner(applicationContext);
            firstRunner.doFirstRun();

            firstRunVerifier.setFirstRun(true);
        }
    }

    private void loadAndShowWidgetsList() {
        if(loadWidgetsListTask != null) {
            switch (loadWidgetsListTask.getStatus()) {
                case PENDING:
                case RUNNING:
                    if(!loadWidgetsListTask.isCancelled()){
                        loadWidgetsListTask.cancel(true);
                    }
                    loadWidgetsListTask = new LoadWidgetsListTask(getApplicationContext());
                    loadWidgetsListTask.execute();
                    break;
                case FINISHED:
                    loadWidgetsListTask = new LoadWidgetsListTask(getApplicationContext());
                    loadWidgetsListTask.execute();
                    break;
            }
        }
        else {
            loadWidgetsListTask = new LoadWidgetsListTask(getApplicationContext());
            loadWidgetsListTask.execute();
        }

    }

    public void onClickMenu(View view) {
        switch (view.getId()) {
            case R.id.activity_main_general_iv_btn_create:
                btnCreate.setImageResource(R.drawable.im_create_btn_on);
                startOpenActivityTask(WidgetCreatorActivity.class);
                break;
            case R.id.activity_main_general_iv_btn_author:
                btnAuthor.setImageResource(R.drawable.im_author_btn_on);
                startOpenActivityTask(WidgetCreatorActivity.class);
                break;
            case R.id.activity_main_general_iv_btn_help:
                btnHelp.setImageResource(R.drawable.im_help_btn_on);
                startOpenActivityTask(HelpActivity.class);
                break;
            case R.id.activity_main_general_iv_btn_share:
                btnShare.setImageResource(R.drawable.im_share_btn_on);
                startSharing();
                break;
        }
    }

    private void startSharing() {
        new Thread(new Runnable() {
            public void run() {
                Intent sendIntent = new Intent();
                sendIntent.setType("text/plain");
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,  "\"Cyberpunk 2077\" - Wake the f*** Up Android. https://play.google.com/store/apps/developer?id=Orlinskas.Development");
                startActivity(Intent.createChooser(sendIntent,"Share"));

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        btnShare.setImageResource(R.drawable.im_share_btn_off);
                    }
                });
            }
        }).start();
    }

    private void startOpenActivityTask(Class activity) {
        if(openActivityTask != null) {
            switch (openActivityTask.getStatus()) {
                case PENDING:
                case RUNNING:
                    if(!openActivityTask.isCancelled()){
                        openActivityTask.cancel(true);
                    }
                    openActivityTask = new OpenActivityTask(activity);
                    openActivityTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    break;
                case FINISHED:
                    openActivityTask = new OpenActivityTask(activity);
                    openActivityTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    break;
            }
        }
        else {
            openActivityTask = new OpenActivityTask(activity);
            openActivityTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }

    }

    private class LoadWidgetsListTask extends AsyncTask<Void, Void, Void> {
        private final int IC_PROGRESS_1 = R.drawable.im_progress_1;
        private final int IC_PROGRESS_2 = R.drawable.im_progress_2;
        private final int IC_PROGRESS_3 = R.drawable.im_progress_3;
        private final int IC_PROGRESS_4 = R.drawable.im_progress_4;
        private final int[] progressImage = {IC_PROGRESS_1, IC_PROGRESS_2, IC_PROGRESS_3, IC_PROGRESS_4};
        private int imageCount = 1;
        private ArrayList<Widget> widgets;
        private Context context;

        LoadWidgetsListTask(Context context) {
            this.context = context;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            int roundCount = 0;
            WidgetRepository repository = new WidgetRepository(context);
            try {
                widgets = repository.query(new WidgetEmptySpecification());
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (widgets == null || widgets.size() == 0) {
                addEmptyWidgetElement();
            }
            do {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int imageId = progressImage[imageCount];
                        progressList.setImageResource(imageId);
                    }
                });
                imageCount++;
                roundCount++;
                if(imageCount == 4) {
                    imageCount = 0;
                }
                if (isCancelled()) {
                    break;
                }
            }
            while (roundCount < 20);

            return null;
        }

        private void addEmptyWidgetElement() {
            City emptyCity = new City(404, "Not found","Need to create",1.0,1.0);
            Widget emptyWidget = new Widget(0, emptyCity, new Request("n/a", emptyCity, "n/a", "n/a" , "n/a", "n/a"));
            widgets.add(emptyWidget);
            ToastBuilder.createSnackBar(scrollView,"Create widget");
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            progressList.setImageResource(IC_PROGRESS_1);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressList.setImageResource(IC_PROGRESS_1);
            showWidgetsList(widgets);
        }

        private void showWidgetsList(ArrayList<Widget> widgets) {
            ArrayAdapter<Widget> adapter = new WidgetListAdapter(getApplicationContext(), R.layout.widget_list_view_row, widgets);
            listView.setAdapter(adapter);
        }
    }

    private class WidgetListAdapter extends ArrayAdapter<Widget> {
        private ArrayList<Widget> widgets;

        WidgetListAdapter(Context applicationContext, int widget_list_view_row, ArrayList<Widget> widgets) {
            super(applicationContext, widget_list_view_row, widgets);
            this.widgets = widgets;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            @SuppressLint("ViewHolder")
            View row = inflater.inflate(R.layout.widget_list_view_row, parent, false);
            TextView widgetId = row.findViewById(R.id.widget_list_view_row_tv_id);
            TextView widgetCityName = row.findViewById(R.id.widget_list_view_row_tv_name);
            TextView widgetCountryCode = row.findViewById(R.id.widget_list_view_row_tv_code);

            String id = String.valueOf(widgets.get(position).getId());
            String name = widgets.get(position).getCity().getName();
            String code = widgets.get(position).getCity().getCountryCode();

            widgetId.setText(id);
            widgetCityName.setText(name);
            widgetCountryCode.setText(code);

            return row;
        }
    }

    private class OpenActivityTask extends AsyncTask<Void, Void, Void> {
        private final int IC_PROGRESS_1 = R.drawable.im_progress_1;
        private final int IC_PROGRESS_2 = R.drawable.im_progress_2;
        private final int IC_PROGRESS_3 = R.drawable.im_progress_3;
        private final int IC_PROGRESS_4 = R.drawable.im_progress_4;
        private final int[] progressImage = {IC_PROGRESS_1, IC_PROGRESS_2, IC_PROGRESS_3, IC_PROGRESS_4};
        private int imageCount = 1;
        private Class activity;

        OpenActivityTask(Class activity) {
            this.activity = activity;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            int roundCount = 0;
            do {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int imageId = progressImage[imageCount];
                        progressMenu.setImageResource(imageId);
                    }
                });
                imageCount++;
                roundCount++;
                if(imageCount == 4) {
                    imageCount = 0;
                }
                if (isCancelled())
                    break;
            }
            while (roundCount < 10);
            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            progressMenu.setImageResource(IC_PROGRESS_1);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressMenu.setImageResource(IC_PROGRESS_1);
            openActivity(activity);
        }

        private void openActivity(final Class activityClass) {
            Intent intent = new Intent(getApplicationContext(), activityClass);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            ContextCompat.startActivity(getApplicationContext(), intent, null);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        btnShare.setImageResource(R.drawable.im_share_btn_off);
        btnHelp.setImageResource(R.drawable.im_help_btn_off);
        btnAuthor.setImageResource(R.drawable.im_author_btn_off);
        btnCreate.setImageResource(R.drawable.im_create_btn_off);
    }
}


