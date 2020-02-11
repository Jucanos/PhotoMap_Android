package com.jucanos.photomap.photoPicker;


import com.jucanos.photomap.GlobalApplication;

/**
 * Created by sky on 17/3/1.
 */

public class ViewUtils {

    public static int dip2px(float dpValue) {
        final float scale = GlobalApplication.getGlobalApplicationContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dip(float pxValue) {
        final float scale = GlobalApplication.getGlobalApplicationContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int getScreenWidth() {
        return GlobalApplication.getGlobalApplicationContext().getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return GlobalApplication.getGlobalApplicationContext().getResources().getDisplayMetrics().heightPixels;
    }
}
