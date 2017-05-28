package com.yurab.photoapiclient.tools;

import android.content.Context;
import android.content.SharedPreferences;

public class Prefs {
    private static final String PARAM_USER_ACCESS_TOKEN = "PARAM_USER_ACCESS_TOKEN";

    public static void storeStrParam(final Context context, final String key, final String value) {
        if (context == null) return;
        final SharedPreferences prefs = context.getSharedPreferences(
                Prefs.class.getSimpleName(), Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String loadStrParam(final Context context, final String key) {
        if (context == null) return "";
        final SharedPreferences prefs = context.getSharedPreferences(Prefs.class.getSimpleName(), Context.MODE_PRIVATE);
        return prefs.getString(key, "");
    }

    public static void storeAccessToken(final Context context, final String value) {
        if (context == null) return;
        final SharedPreferences prefs = context.getSharedPreferences(
                Prefs.class.getSimpleName(), Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PARAM_USER_ACCESS_TOKEN, value);
        editor.apply();
    }

    public static String loadAccessToken(final Context context) {
        if (context == null) return "";
        final SharedPreferences prefs = context.getSharedPreferences(Prefs.class.getSimpleName(), Context.MODE_PRIVATE);
        return prefs.getString(PARAM_USER_ACCESS_TOKEN, "");
    }
}
