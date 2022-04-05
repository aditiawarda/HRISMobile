package com.gelora.absensi.support;

import android.graphics.Color;

import androidx.annotation.ColorInt;

final class ColorUtil {

    public static boolean isLightColor(@ColorInt int color) {
        return Color.red(color) * 0.299 + Color.green(color) * 0.587 + Color.blue(color) * 0.114 > 160;
    }

}
