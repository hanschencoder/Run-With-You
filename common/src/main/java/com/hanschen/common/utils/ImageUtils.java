package com.hanschen.common.utils;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.widget.ImageView;

/**
 * Created by Hans.Chen on 2015-11-26.
 */
public class ImageUtils {

    public static final int BRIGHTNESS_NORMAL  = 0;
    public static final int BRIGHTNESS_PRESSED = -50;

    public static void changeLight(ImageView imageView, int brightness) {
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.set(new float[]{1, 0, 0, 0, brightness, 0, 1, 0, 0, brightness, 0, 0, 1, 0, brightness, 0, 0, 0, 1, 0});
        imageView.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
    }
}
