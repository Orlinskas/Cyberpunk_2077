package com.orlinskas.cyberpunk.ui.other;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.orlinskas.cyberpunk.ActivityOpener;
import com.orlinskas.cyberpunk.R;
import com.orlinskas.cyberpunk.ToastBuilder;

public class HelpActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
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
                sendIntent.putExtra(Intent.EXTRA_TEXT,  "\"Weather\" - удобный виджет погоды. Присоединяйся https://play.google.com/store/apps/developer?id=Orlinskas.Development");
                startActivity(Intent.createChooser(sendIntent,"Поделиться"));
                return true;
            case R.id.action_help_pls:
                ToastBuilder.create(getApplicationContext(), "Уже здесь");
                return true;
            case R.id.action_message:
                ActivityOpener.openActivity(getApplicationContext(), MessageToAuthorActivity.class);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
