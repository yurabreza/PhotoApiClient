package com.yurab.photoapiclient.screens.main.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.yurab.photoapiclient.BuildConfig;
import com.yurab.photoapiclient.R;
import com.yurab.photoapiclient.screens.main.presenter.MainActivityPresenter;
import com.yurab.photoapiclient.screens.main.presenter.MainActivityPresenterImpl;
import com.yurab.photoapiclient.screens.photos_list.view.PhotosListFragment;
import com.yurab.photoapiclient.screens.random_photo.view.RandomPhotoFragment;
import com.yurab.photoapiclient.tools.Prefs;

public class MainActivity extends AppCompatActivity implements MainActivityView,
        PhotosListFragment.PhotosListInteractionListener {

    public static final String TAG = MainActivity.class.getSimpleName();
    public static final String UNSPLASH_ACCESS_CODE = "code=";
    public static final String TEMPORARY_TOKEN_FLAG = "temp";

    private MainActivityPresenter mPresenter = new MainActivityPresenterImpl();
    private boolean mIsInAuthProcess = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPresenter.onAttach(this);
        getIntentExtras();
        if (Prefs.loadAccessToken(this).isEmpty()) {
            displayAuthDialog();
        } else if (!Prefs.loadAccessToken(this).equals(TEMPORARY_TOKEN_FLAG)) {
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

    private void displayAuthDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.txt_auth_dialog_title)
                .setMessage(R.string.txt_auth_dialog_subtitle)
                .setCancelable(false)
                .setIcon(R.drawable.ic_alert)
                .setPositiveButton(getString(R.string.action_ok), (dialog, which) -> authoriseToUnsplash(dialog))
                .create()
                .show();
    }

    private void authoriseToUnsplash(DialogInterface alertDialog) {
        alertDialog.dismiss();
        Prefs.storeAccessToken(this, TEMPORARY_TOKEN_FLAG);
        mIsInAuthProcess = true;
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
        ft.add(R.id.fl_container, fragment, tag);
        if (fragmentToBackStack) ft.addToBackStack(null);
        ft.commit();
    }

    //region MainActivityView
    @Override
    public void onTokenLoadSuccess(String token) {
        Prefs.storeAccessToken(this, token);
        initContent();
    }

    @Override
    public void onError(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
    //endregion


    //region PhotosListInteractionListener
    @Override
    public void startPhotoFragment(String id) {
        changeFragment(RandomPhotoFragment.newInstance(id), RandomPhotoFragment.class.getSimpleName(), true);
    }
    //endregion

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDetach();
    }

}
