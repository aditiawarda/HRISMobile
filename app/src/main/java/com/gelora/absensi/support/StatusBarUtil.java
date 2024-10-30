package com.gelora.absensi.support;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class StatusBarUtil {

//    public static int getStatusHeight(Activity activity){
//        int statusHeight;
//        Rect localRect = new Rect();
//        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
//        statusHeight = localRect.top;
//        if (0 == statusHeight){
//            Class<?> localClass;
//            try {
//                localClass = Class.forName("com.android.internal.R$dimen");
//                Object localObject = localClass.newInstance();
//                int id = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
//                statusHeight = activity.getResources().getDimensionPixelSize(id);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return statusHeight;
//    }

    public static int getStatusHeight(Activity activity) {
        int result = 0;
        @SuppressLint({"DiscouragedApi", "InternalInsetResource"})
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = activity.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    public static void setMiuiStatusBarIconDarkMode(Activity activity, boolean dark) {
        boolean result = false;
        Class<? extends Window> clazz = activity.getWindow().getClass();
        try {
            int darkModeFlag = 0;
            @SuppressLint("PrivateApi") Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(activity.getWindow(), dark ? darkModeFlag : 0, darkModeFlag);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setFlymeStatusBarIconDarkMode(Activity activity, boolean dark) {
        boolean result = false;
        try {
            Window window = activity.getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
            Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
            darkFlag.setAccessible(true);
            meizuFlags.setAccessible(true);
            int bit = darkFlag.getInt(null);
            int value = meizuFlags.getInt(lp);
            if (dark) {
                value |= bit;
            } else {
                value &= ~bit;
            }
            meizuFlags.setInt(lp, value);
            window.setAttributes(lp);
            result = true;
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    public static void setTranslucentStatus(Activity activity) {
        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setWindowFlag(activity, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
        activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
    }

    public static int getActionBarHeight(Context context) {
        int result = 0;
        TypedValue tv = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true);
        result = TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
        return result;
    }

}
