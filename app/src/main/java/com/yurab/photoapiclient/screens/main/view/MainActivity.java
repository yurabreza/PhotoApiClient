package com.yurab.photoapiclient.screens.main.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.yurab.photoapiclient.BuildConfig;
import com.yurab.photoapiclient.R;
import com.yurab.photoapiclient.screens.main.adapter.ViewPagerAdapter;
import com.yurab.photoapiclient.screens.main.presenter.MainActivityPresenter;
import com.yurab.photoapiclient.screens.main.presenter.MainActivityPresenterImpl;
import com.yurab.photoapiclient.screens.photos_list.view.PhotosListFragment;
import com.yurab.photoapiclient.screens.random_photo.view.RandomPhotoFragment;
import com.yurab.photoapiclient.tools.Prefs;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainActivityView {

    public static final String TAG = MainActivity.class.getSimpleName();

    public static final String UNSPLASH_ACCESS_CODE = "code=";

    @BindView(R.id.bottom_nav_bar) BottomNavigationView mBottomNavigationView;
    @BindView(R.id.view_pager) ViewPager mViewPager;
    private MainActivityPresenter mPresenter = new MainActivityPresenterImpl();
    private boolean mFromCode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mBottomNavigationView.setOnNavigationItemSelectedListener(this::onBottomBarItemClick);
        setupViewPager();
        mPresenter.onAttach(this);
        getIntentExtras();

        if (Prefs.loadAccessToken(this).isEmpty()) {
            authoriseToUnsplash();
        }
    }

    private void setupViewPager() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new PhotosListFragment());
        fragments.add(new RandomPhotoFragment());
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                mFromCode = true;
                switch (position) {
                    case 0:
                        mBottomNavigationView.setSelectedItemId(R.id.nav_photos_list);
                        break;
                    case 1:
                        mBottomNavigationView.setSelectedItemId(R.id.nav_random_photo);
                        break;
                }
//                mFromCode = false;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    private boolean onBottomBarItemClick(MenuItem menuItem) {
        if (mFromCode) return false;
        boolean stateChanged = false;
        switch (menuItem.getItemId()) {
            case R.id.nav_photos_list:
                mViewPager.setCurrentItem(0);
                stateChanged = true;
                break;
            case R.id.nav_random_photo:
                mViewPager.setCurrentItem(1);
                stateChanged = true;
                break;
        }
        return stateChanged;
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
