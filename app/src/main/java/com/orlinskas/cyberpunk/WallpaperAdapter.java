package com.orlinskas.cyberpunk;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

public class WallpaperAdapter extends BaseAdapter {
    private Context context;
    public final static Integer[] wallpapersPath = {
            R.drawable.wallpaper_1, R.drawable.wallpaper_2, R.drawable.wallpaper_3,
            R.drawable.wallpaper_4, R.drawable.wallpaper_5, R.drawable.wallpaper_6,
            R.drawable.wallpaper_7, R.drawable.wallpaper_8, R.drawable.wallpaper_9,
            R.drawable.wallpaper_11, R.drawable.wallpaper_12, R.drawable.wallpaper_13,
            R.drawable.wallpaper_14, R.drawable.wallpaper_15, R.drawable.wallpaper_16,
            R.drawable.wallpaper_17, R.drawable.wallpaper_18, R.drawable.wallpaper_19,
            R.drawable.wallpaper_20, R.drawable.wallpaper_21, R.drawable.wallpaper_22};

    public WallpaperAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return wallpapersPath.length;
    }

    @Override
    public Object getItem(int position) {
        return wallpapersPath[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View image_row;

        if (convertView == null) {
            image_row = new View(context);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            image_row = inflater.inflate(R.layout.image_row, parent, false);
        } else {
            image_row = convertView;
        }

        int imagePath = wallpapersPath[position];

        ImageView imageView = image_row.findViewById(R.id.image_row_iv);
        imageView.setImageBitmap(compressBitmap(context.getResources(), imagePath));

        Drawable d = context.getResources().getDrawable(imagePath);
        int h = d.getIntrinsicHeight();
        int w = d.getIntrinsicWidth();

        TextView sizeImage = image_row.findViewById(R.id.image_row_tv);
        sizeImage.setText(String.format(Locale.ENGLISH, "%d X %d", w, h));

        return image_row;
    }

    private Bitmap compressBitmap(Resources resources, int id) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;

            return BitmapFactory.decodeResource(resources, id, options);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
