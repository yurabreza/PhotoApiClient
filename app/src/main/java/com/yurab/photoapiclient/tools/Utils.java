package com.yurab.photoapiclient.tools;

import android.view.View;

public class Utils {

    private static final long BUTTON_CLICK_DISABLED_TIME = 1000;

    public static void disableViewAfterClick(final View v) {
        if (v == null) return;
        v.setEnabled(false);
        v.postDelayed(() -> {
            try {
                v.setEnabled(true);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }, BUTTON_CLICK_DISABLED_TIME);
    }
}
