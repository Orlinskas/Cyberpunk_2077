package com.orlinskas.cyberpunk.ui.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.orlinskas.cyberpunk.R;
import com.orlinskas.cyberpunk.ui.other.HelpActivity;
import com.orlinskas.cyberpunk.ui.other.MessageToAuthorActivity;
import com.orlinskas.cyberpunk.ui.other.WidgetCreatorActivity;
import com.orlinskas.cyberpunk.ui.widget.AnimatedBackgroundView;
import com.orlinskas.cyberpunk.widget.Widget;

import java.util.ArrayList;

public class MainActivityGeneral extends AppCompatActivity {
    private ImageView btnCreate, btnOptions, btnHelp, btnShare;
    private ImageView progressMenu, progressList;
    private ListView listWidgets;
    private ArrayList<Widget> widgets;
    private MenuProgressBarTask menuProgressBarTask;
    private WidgetListProgressBarTask widgetListProgressBarTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_general);

        btnCreate = findViewById(R.id.activity_main_general_iv_btn_create);
        btnOptions = findViewById(R.id.activity_main_general_iv_btn_options);
        btnHelp = findViewById(R.id.activity_main_general_iv_btn_help);
        btnShare = findViewById(R.id.activity_main_general_iv_btn_share);
        progressMenu = findViewById(R.id.activity_main_general_im_progress_menu);
        progressList = findViewById(R.id.activity_main_general_im_progress_list);
        listWidgets = findViewById(R.id.activity_main_general_lv_widgets);

        showAnimationsHead();


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

    public void onClickMenu(View view) {
        switch (view.getId()) {
            case R.id.activity_main_general_iv_btn_create:
                btnCreate.setImageResource(R.drawable.im_create_btn_on);
                startMenuProgressTask();
                openActivity(WidgetCreatorActivity.class);
                break;
            case R.id.activity_main_general_iv_btn_options:
                btnOptions.setImageResource(R.drawable.im_options_btn_on);
                startMenuProgressTask();
                openActivity(MessageToAuthorActivity.class);
                break;
            case R.id.activity_main_general_iv_btn_help:
                btnHelp.setImageResource(R.drawable.im_help_btn_on);
                startMenuProgressTask();
                openActivity(HelpActivity.class);
                break;
            case R.id.activity_main_general_iv_btn_share:
                btnShare.setImageResource(R.drawable.im_share_btn_on);
                startMenuProgressTask();
                startSharing();
                break;

        }
    }

    private void openActivity(final Class activityClass) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(getApplicationContext(), activityClass);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                ContextCompat.startActivity(getApplicationContext(), intent, null);
            }
        }).start();
    }

    private void startSharing() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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

    @SuppressLint("StaticFieldLeak")
    private class MenuProgressBarTask extends AsyncTask<Void, Void, Void> {
        private final int IC_PROGRESS_1 = R.drawable.im_progress_1;
        private final int IC_PROGRESS_2 = R.drawable.im_progress_2;
        private final int IC_PROGRESS_3 = R.drawable.im_progress_3;
        private final int IC_PROGRESS_4 = R.drawable.im_progress_4;
        private final int[] progressImage = {IC_PROGRESS_1, IC_PROGRESS_2, IC_PROGRESS_3, IC_PROGRESS_4};
        private int imageCount = 1;

        @Override
        protected Void doInBackground(Void... voids) {
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
                if(imageCount == 4) {
                    imageCount = 0;
                }
                if (isCancelled())
                    break;
            }
            while (true);
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
        }
    }

    private void startMenuProgressTask() {
        if(menuProgressBarTask != null) {
            switch (menuProgressBarTask.getStatus()) {
                case PENDING:
                case RUNNING:
                    if(!menuProgressBarTask.isCancelled()){
                        menuProgressBarTask.cancel(true);
                    }
                    menuProgressBarTask = new MenuProgressBarTask();
                    menuProgressBarTask.execute();
                    break;
                case FINISHED:
                    menuProgressBarTask = new MenuProgressBarTask();
                    menuProgressBarTask.execute();
                    break;
            }
        }
        else {
            menuProgressBarTask = new MenuProgressBarTask();
            menuProgressBarTask.execute();
        }

    }

    private void stopMenuProgressTask() {
        if(menuProgressBarTask != null) {
            switch (menuProgressBarTask.getStatus()) {
                case FINISHED:
                case RUNNING:
                case PENDING:
                    menuProgressBarTask.cancel(true);
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class WidgetListProgressBarTask extends AsyncTask<Void, Void, Void> {
        private final int IC_PROGRESS_1 = R.drawable.im_progress_1;
        private final int IC_PROGRESS_2 = R.drawable.im_progress_2;
        private final int IC_PROGRESS_3 = R.drawable.im_progress_3;
        private final int IC_PROGRESS_4 = R.drawable.im_progress_4;
        private final int[] progressImage = {IC_PROGRESS_1, IC_PROGRESS_2, IC_PROGRESS_3, IC_PROGRESS_4};
        private int imageCount = 1;

        @Override
        protected Void doInBackground(Void... voids) {
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
                if(imageCount == 4) {
                    imageCount = 0;
                }
                if (isCancelled())
                    break;
            }
            while (true);
            return null;
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
        }
    }

    private void startWidgetListProgressTask() {
        if(widgetListProgressBarTask != null) {
            switch (widgetListProgressBarTask.getStatus()) {
                case PENDING:
                case RUNNING:
                    if(!widgetListProgressBarTask.isCancelled()){
                        widgetListProgressBarTask.cancel(true);
                    }
                    widgetListProgressBarTask = new WidgetListProgressBarTask();
                    widgetListProgressBarTask.execute();
                    break;
                case FINISHED:
                    widgetListProgressBarTask = new WidgetListProgressBarTask();
                    widgetListProgressBarTask.execute();
                    break;
            }
        }
        else {
            widgetListProgressBarTask = new WidgetListProgressBarTask();
            widgetListProgressBarTask.execute();
        }

    }

    private void stopListProgressTask() {
        if(widgetListProgressBarTask != null) {
            switch (widgetListProgressBarTask.getStatus()) {
                case FINISHED:
                case RUNNING:
                case PENDING:
                    widgetListProgressBarTask.cancel(true);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopMenuProgressTask();
        stopListProgressTask();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopMenuProgressTask();
        stopListProgressTask();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        recreate();
    }
}


