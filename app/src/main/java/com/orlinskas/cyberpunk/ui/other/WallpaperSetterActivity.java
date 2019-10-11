package com.orlinskas.cyberpunk.ui.other;

import android.annotation.SuppressLint;
import android.app.WallpaperManager;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;

import com.orlinskas.cyberpunk.ImageAdapter;
import com.orlinskas.cyberpunk.R;

import java.io.IOException;

public class WallpaperSetterActivity extends AppCompatActivity {
    private GridView gridView;
    private final String FLAG_OLD_API = "oldApiScreen";
    private final String FLAG_HOME = "homeScreen";
    private final String FLAG_LOCK = "lockScreen";
    private final String FLAG_BOTH = "home&lock";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpaper_setter);
        gridView = findViewById(R.id.actvity_wallpaper_setter_gv);
        gridView.setAdapter(new ImageAdapter(this));
        adjustGridView();
    }

    private void adjustGridView() {
        gridView.setNumColumns(GridView.AUTO_FIT);
        gridView.setColumnWidth(400);
        gridView.setHorizontalSpacing(10);
        gridView.setVerticalSpacing(10);
        gridView.setStretchMode(GridView.STRETCH_SPACING_UNIFORM);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Animation animationClick = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation_button);
                view.startAnimation(animationClick);
                createDialog(position);
            }
        });
    }

    private void createDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            builder.setTitle("Set Wallpaper");
            builder.setMessage("Choose screen");
            builder.setNegativeButton("Lock", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    setWall(position, FLAG_LOCK);
                }
            });
            builder.setPositiveButton("Home", new DialogInterface.OnClickListener() { // Кнопка ОК
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    setWall(position, FLAG_HOME);
                }
            });

            builder.setNeutralButton("Both", new DialogInterface.OnClickListener() { // Кнопка ОК
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    setWall(position, FLAG_BOTH);
                }
            });
        }
        else {
            builder.setTitle("Warning");
            builder.setMessage("Set as wallpaper?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    setWall(position, FLAG_OLD_API);
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @SuppressLint("NewApi")
    private void setWall(int position, String FLAG) {
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
        Drawable drawable = getResources().getDrawable(ImageAdapter.wallpapersPath[position]);
        Bitmap wallpaper = ((BitmapDrawable) drawable).getBitmap();
        switch (FLAG) {
            case FLAG_OLD_API:
                try {
                    wallpaperManager.setBitmap(wallpaper);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case FLAG_HOME:
                try {
                    wallpaperManager.setBitmap(wallpaper,null,true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case FLAG_LOCK:
                try {
                    wallpaperManager.setBitmap(wallpaper, null, true, WallpaperManager.FLAG_LOCK);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case FLAG_BOTH:
                try {
                    wallpaperManager.setBitmap(wallpaper, null, true, WallpaperManager.FLAG_SYSTEM);
                    wallpaperManager.setBitmap(wallpaper, null, true, WallpaperManager.FLAG_LOCK);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }

    }
}
