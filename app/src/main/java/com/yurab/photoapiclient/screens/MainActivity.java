package com.yurab.photoapiclient.screens;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.yurab.photoapiclient.BuildConfig;
import com.yurab.photoapiclient.R;
import com.yurab.photoapiclient.tools.Prefs;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    public static final String UNSPLASH_ACCESS_TOKEN = "code=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getIntent().getData() != null) getIntentExtras();

    }

    private void authoriseToUnsplash() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data = Uri.parse(BuildConfig.UNSPLASH_AUTH_URL);
        intent.setData(data);
        startActivity(intent);
    }

    private void getIntentExtras() {
        Uri data = getIntent().getData();
        String scheme = data.getScheme(); // "http"
        if (scheme.equalsIgnoreCase(BuildConfig.UNSPLASH_CALLBACK_SCHEME)) {
            String dataString = data.toString();
            if (dataString.contains(UNSPLASH_ACCESS_TOKEN)) {
                String code = dataString.substring(dataString.indexOf(UNSPLASH_ACCESS_TOKEN));
                code = code.replace(UNSPLASH_ACCESS_TOKEN, "");
                Log.d(TAG, "getIntentExtras: code " + code);
                Prefs.storeAccessToken(this, code);
            }
        }
    }
}
