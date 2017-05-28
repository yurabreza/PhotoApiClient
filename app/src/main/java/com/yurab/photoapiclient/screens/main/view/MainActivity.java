package com.yurab.photoapiclient.screens.main.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.yurab.photoapiclient.BuildConfig;
import com.yurab.photoapiclient.R;
import com.yurab.photoapiclient.screens.main.presenter.MainActivityPresenter;
import com.yurab.photoapiclient.screens.main.presenter.MainActivityPresenterImpl;
import com.yurab.photoapiclient.screens.photos_list.view.PhotosListFragment;
import com.yurab.photoapiclient.tools.Prefs;

public class MainActivity extends AppCompatActivity implements MainActivityView {

    public static final String TAG = MainActivity.class.getSimpleName();

    public static final String UNSPLASH_ACCESS_CODE = "code=";

    private MainActivityPresenter mPresenter = new MainActivityPresenterImpl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPresenter.onAttach(this);
        getIntentExtras();
        if (Prefs.loadAccessToken(this).isEmpty()) {
            authoriseToUnsplash();
        } else {
            initContent();
        }
    }

    private void getIntentExtras() {
        if (getIntent().getData() == null) return;

        Uri data = getIntent().getData();
        String scheme = data.getScheme();
        if (scheme.equalsIgnoreCase(BuildConfig.UNSPLASH_CALLBACK_SCHEME)) {
            String dataString = data.toString();
            if (dataString.contains(UNSPLASH_ACCESS_CODE)) {
                String code = dataString.substring(dataString.indexOf(UNSPLASH_ACCESS_CODE));
                code = code.replace(UNSPLASH_ACCESS_CODE, "");
                Log.d(TAG, "getIntentExtras: " + code);
                mPresenter.getUserToken(code);
            }
        }
    }

    private void authoriseToUnsplash() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data = Uri.parse(BuildConfig.UNSPLASH_AUTH_URL);
        intent.setData(data);
        startActivity(intent);
    }

    private void initContent() {
        changeFragment(new PhotosListFragment(), PhotosListFragment.class.getSimpleName(), false);
    }

    private void changeFragment(Fragment fragment, String tag, boolean fragmentToBackStack) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fl_container, fragment, tag);
        if (fragmentToBackStack) ft.addToBackStack(null);
        ft.commit();
    }

    //region MainActivityView
    @Override
    public void onTokenLoadSuccess(String token) {
        Log.d(TAG, "onTokenLoadSuccess: " + token);
        Prefs.storeAccessToken(this, token);
    }

    @Override
    public void onError(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
    //endregion

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDetach();
    }
}
