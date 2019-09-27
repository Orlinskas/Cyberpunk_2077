package com.orlinskas.cyberpunk.ui.other;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.orlinskas.cyberpunk.City;
import com.orlinskas.cyberpunk.Country;
import com.orlinskas.cyberpunk.ui.main.MainActivity;
import com.orlinskas.cyberpunk.widget.Widget;

import java.util.concurrent.TimeUnit;

import static com.orlinskas.cyberpunk.ui.other.WidgetCreatorContract.*;

public class WidgetCreatorPresenter implements WidgetCreatorContract.Presenter {
    private Context context;
    private WidgetCreatorContract.Model model;
    private WidgetCreatorContract.View view;
    private Handler viewButtonHandler;
    private Handler viewProgressBarHandler;
    private Thread progressBarThread;
    private boolean isCanceledThread = false;

    WidgetCreatorPresenter(Context context, WidgetCreatorContract.View view) {
        this.view = view;
        this.context = context;
    }

    @SuppressLint("HandlerLeak")
    @Override
    public void startWork() {
        model = new WidgetCreatorModel(context, this);

        viewButtonHandler = new Handler() {
            @Override
            public void handleMessage(Message BUTTON_KEY) {
                view.setButtonStatus(BUTTON_KEY.what, BUTTON_KEY.arg1);
            }
        };

        viewProgressBarHandler = new Handler() {
            @Override
            public void handleMessage(Message IMAGE_KEY) {
                view.setProgressImage(IMAGE_KEY.what);
            }
        };
    }

    @SuppressLint("HandlerLeak")
    @Override
    public void onClickChooseLocation() {
        new Thread(new Runnable() {
            Message message;
            public void run() {
                message = viewButtonHandler.obtainMessage(BUTTON_OPEN_LIST_ACTIVITY, STATUS_ON,0);
                viewButtonHandler.sendMessage(message);

                view.openActivityForResult(CountryListActivity.class);

                try {
                    TimeUnit.MILLISECONDS.sleep(150);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                message = viewButtonHandler.obtainMessage(BUTTON_OPEN_LIST_ACTIVITY, STATUS_OFF,0);
                viewButtonHandler.sendMessage(message);
            }
        }).start();
    }

    @Override
    public void onClickSearchLocation() {
        model.startSearchLocation();
    }

    @Override
    public void onClickCreateWidget(final Country country, final City city) {
        new Thread(new Runnable() {
            Message message;
            public void run() {
                message = viewButtonHandler.obtainMessage(BUTTON_CREATE_WIDGET,STATUS_ON,0);
                viewButtonHandler.sendMessage(message);

                model.createWidget(country, city);

                try {
                    TimeUnit.MILLISECONDS.sleep(150);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                message = viewButtonHandler.obtainMessage(BUTTON_CREATE_WIDGET,STATUS_OFF,0);
                viewButtonHandler.sendMessage(message);
            }
        }).start();
    }

    @Override
    public void onResult(Widget widget) {
        if(widget != null && widget.getId() != EMPTY_WIDGET) {
            view.openWidgetActivity(MainActivity.class);
        }
        else {
            view.doSnackBar("Some data is incorrect");
        }
    }

    @SuppressLint("HandlerLeak")
    @Override
    public void startProgressBar() {
        if(progressBarThread == null || progressBarThread.getState() == Thread.State.NEW
                || progressBarThread.getState() == Thread.State.TERMINATED) {

            isCanceledThread = false;

            progressBarThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    int count = 1;
                    while (true) {
                        try {
                            Message message = viewProgressBarHandler.obtainMessage(count);
                            viewProgressBarHandler.sendMessage(message);
                            count++;
                            if(count == 5) {
                                count = 1;
                            }
                            if(isCanceledThread) {
                                message = viewProgressBarHandler.obtainMessage(1);
                                viewProgressBarHandler.sendMessage(message);
                                break;
                            }
                            TimeUnit.MILLISECONDS.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            progressBarThread.start();
        }
    }

    @Override
    public void stopProgressBar() {
        isCanceledThread = true;
    }

    @Override
    public void destroy() {
        context = null;
        model.stopSearchLocation();
        model = null;
        stopProgressBar();
    }
}
