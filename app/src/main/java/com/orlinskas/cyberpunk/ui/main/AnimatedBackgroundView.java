package com.orlinskas.cyberpunk.ui.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Movie;
import android.graphics.Paint;
import android.view.View;

import com.orlinskas.cyberpunk.R;

import java.io.InputStream;

public class AnimatedBackgroundView extends View {
    Movie movie;
    InputStream is;
    long startTime;

    @SuppressLint("ResourceType")
    public AnimatedBackgroundView(Context context) {
        super(context);
        try {
            is = context.getResources().openRawResource(R.drawable.head_pic_anim);
            movie = Movie.decodeStream(is);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        super.onDraw(canvas);
        long now = System.currentTimeMillis();
        if (startTime == 0) // first time
            startTime = now;
        int relTime = (int) ((now - startTime) % movie.duration());
        movie.setTime(relTime);
        float scaleFactorX = (float) this.getWidth() / (float) movie.width();
        float scaleFactorY = (float) this.getHeight() / (float) movie.height();
        canvas.scale(scaleFactorX, scaleFactorY);
        movie.draw(canvas, 0, 0);
        @SuppressLint("DrawAllocation") Paint p = new Paint();
        p.setAntiAlias(true);
        setLayerType(LAYER_TYPE_SOFTWARE, p);
        this.invalidate();
    }
}
