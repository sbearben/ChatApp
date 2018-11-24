package uk.co.victoriajanedavis.chatapp.util;

import android.content.res.Resources;
import android.util.DisplayMetrics;

public class DisplayMetricsUtil {

    private static final int SCREEN_TABLET_DP_WIDTH = 600;


    public static boolean isTabletLayout() {
        return isScreenW(SCREEN_TABLET_DP_WIDTH);
    }

    // Return true if the width in DP of the device is equal or greater than the given value
    private static boolean isScreenW(int widthDp) {
        DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
        float screenWidth = displayMetrics.widthPixels / displayMetrics.density;
        return screenWidth >= widthDp;
    }
}