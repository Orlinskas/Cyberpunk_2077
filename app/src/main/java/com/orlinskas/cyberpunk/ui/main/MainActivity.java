package com.orlinskas.cyberpunk.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.orlinskas.cyberpunk.ActivityOpener;
import com.orlinskas.cyberpunk.FirstRunner;
import com.orlinskas.cyberpunk.R;
import com.orlinskas.cyberpunk.ToastBuilder;
import com.orlinskas.cyberpunk.preferences.FirstRunVerifier;
import com.orlinskas.cyberpunk.ui.other.HelpActivity;
import com.orlinskas.cyberpunk.ui.other.MessageToAuthorActivity;
import com.orlinskas.cyberpunk.ui.other.WidgetCreatorActivity;
import com.orlinskas.cyberpunk.ui.widget.AnimatedBackgroundView;

public class MainActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private boolean haveFirstRun;
    private FirstRunVerifier firstRunVerifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        final TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R.id.fab);

        firstRunVerifier = new FirstRunVerifier(getApplicationContext());
        haveFirstRun = firstRunVerifier.check();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityOpener.openActivity(getApplicationContext(), WidgetCreatorActivity.class);
                finish();
            }
        });

        RelativeLayout relativeLayout = findViewById(R.id.activity_main_rl_background_anim);
        relativeLayout.addView(new AnimatedBackgroundView(this));

        processFirstRun(getBaseContext());
    }

    private void processFirstRun(Context applicationContext) {
        if(!haveFirstRun) {
            ToastBuilder.createSnackBar(viewPager, getString(R.string.open_data));

            FirstRunner firstRunner = new FirstRunner(applicationContext);
            firstRunner.doFirstRun();

            firstRunVerifier.setFirstRun(true);
        }
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
                sendIntent.putExtra(Intent.EXTRA_TEXT,  "\"Weather\" - удобный виджет погоды. Присоединяйся https://play.google.com/store/apps/developer?id=Orlinskas.Development");
                startActivity(Intent.createChooser(sendIntent,"Поделиться"));
                return true;
            case R.id.action_help_pls:
                ActivityOpener.openActivity(getApplicationContext(), HelpActivity.class);
                return true;
            case R.id.action_message:
                ActivityOpener.openActivity(getApplicationContext(), MessageToAuthorActivity.class);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}