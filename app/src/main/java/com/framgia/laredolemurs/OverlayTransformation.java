package com.framgia.laredolemurs;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.squareup.picasso.Transformation;

public class OverlayTransformation implements Transformation {

    public OverlayTransformation() {
    }

    @Override
    public Bitmap transform(Bitmap source) {
        Bitmap workingBitmap = Bitmap.createBitmap(source);
        Bitmap mutableBitmap = darken(workingBitmap.copy(Bitmap.Config.ARGB_8888, true));
        Canvas canvas = new Canvas(mutableBitmap);
        canvas.drawBitmap(mutableBitmap, 0, 0, null);
        source.recycle();
        return mutableBitmap;
    }

    @Override
    public String key() {
        return "DarkenTransformation";
    }

    private static Bitmap darken(Bitmap bitmap) {
        Canvas canvas = new Canvas(bitmap);
        Paint p = new Paint(Color.RED);
        ColorFilter filter = new LightingColorFilter(0xFF7F7F7F, 0x00000000);
        p.setColorFilter(filter);
        canvas.drawBitmap(bitmap, new Matrix(), p);
        return bitmap;
    }
}
