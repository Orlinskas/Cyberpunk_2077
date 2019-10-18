package com.orlinskas.cyberpunk.ui.other;

import android.annotation.SuppressLint;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.orlinskas.cyberpunk.R;
import com.orlinskas.cyberpunk.ToastBuilder;
import com.orlinskas.cyberpunk.WallpaperAdapter;

import java.io.IOException;

public class WallpaperSetterActivity extends AppCompatActivity {
    private GridView gridView;
    private final String FLAG_OLD_API = "oldApiScreen";
    private final String FLAG_HOME = "homeScreen";
    private final String FLAG_LOCK = "lockScreen";
    private final String FLAG_BOTH = "home&lock";
    private ProgressBar progressBar;
    private SetWallpaperTask wallpaperTask;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpaper_setter);
        progressBar = findViewById(R.id.activity_wallpaper_setter_pb);
        gridView = findViewById(R.id.actvity_wallpaper_setter_gv);
        gridView.setAdapter(new WallpaperAdapter(this));
        adjustGridView();
    }

    private void adjustGridView() {
        gridView.setNumColumns(2);
        gridView.setColumnWidth(300);
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
        if(wallpaperTask != null && wallpaperTask.getStatus() == AsyncTask.Status.RUNNING) {
            ToastBuilder.create(getBaseContext(),"Wait!");
        }
        else {
            wallpaperTask = new SetWallpaperTask(getBaseContext(), position, FLAG);
            wallpaperTask.execute();
        }
    }

    @SuppressLint("StaticFieldLeak")
    class SetWallpaperTask extends AsyncTask <Void, Void, Void> {
        private Context context;
        private int position;
        private String FLAG;


        SetWallpaperTask(Context context, int position, String FLAG) {
            this.context = context;
            this.position = position;
            this.FLAG = FLAG;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            ToastBuilder.create(getBaseContext(), "Please wait...");
        }

        @SuppressLint("NewApi")
        @Override
        protected Void doInBackground(Void... voids) {
            WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);
            Bitmap wallpaper = compressBitmap(WallpaperAdapter.wallpapersPath[position]);
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
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setVisibility(View.INVISIBLE);
            ToastBuilder.create(getBaseContext(), "Done");
        }

        private Bitmap compressBitmap(int id) {
            try {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeResource(getResources(), id, options);

                Point size = new Point();
                Display display = getWindowManager().getDefaultDisplay();
                display.getRealSize(size);

                options.inJustDecodeBounds = false;
                options.inSampleSize = calculateInSampleSize(options, size.x, size.y);

                return BitmapFactory.decodeResource(getResources(), id, options);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = 1;

            if (height > reqHeight || width > reqWidth) {

                final int halfHeight = height / 2;
                final int halfWidth = width / 2;

                while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                    inSampleSize *= 2;
                }
            }

            return inSampleSize;
        }
    }

    @Override
    public void onBackPressed() {
        if(wallpaperTask != null && wallpaperTask.getStatus() == AsyncTask.Status.RUNNING) {
            ToastBuilder.create(getBaseContext(),"Wait!");
        }
        else {
            super.onBackPressed();
        }
    }
}
