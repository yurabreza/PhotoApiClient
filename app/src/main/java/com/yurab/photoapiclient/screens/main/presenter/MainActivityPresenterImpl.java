package com.yurab.photoapiclient.screens.main.presenter;

import android.util.Log;

import com.yurab.photoapiclient.BuildConfig;
import com.yurab.photoapiclient.global.Constants;
import com.yurab.photoapiclient.model.network.UnsplashClient;
import com.yurab.photoapiclient.model.request.GetTokenRequest;
import com.yurab.photoapiclient.model.response.TokenResponse;
import com.yurab.photoapiclient.screens.base.BaseView;
import com.yurab.photoapiclient.screens.main.view.MainActivityView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivityPresenterImpl implements MainActivityPresenter {

    public static final String GRANT_TYPE = "authorization_code";
    public static final String URL_GET_OAUTH_TOKEN = "https://unsplash.com/oauth/token";

    private MainActivityView mView;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @Override
    public void onAttach(BaseView view) {
        if (view instanceof MainActivityView) {
            mView = (MainActivityView) view;
        } else {
            throw new RuntimeException(VIEW_CAST_ERROR + MainActivityView.class.getSimpleName());
        }
    }

    @Override
    public void getUserToken(String code) {
        mCompositeDisposable.add(UnsplashClient.getApiInterface()
                .getToken(URL_GET_OAUTH_TOKEN,
                        new GetTokenRequest(BuildConfig.UNSPLASH_APPLICATION_ID,
                                BuildConfig.UNSPLASH_SECRET,
                                BuildConfig.UNSPLASH_REDIRECT_URI,
                                code,
                                GRANT_TYPE))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onLoadSuccess, this::handleError));
    }

    private void onLoadSuccess(TokenResponse tokenResponse) {
        mView.onTokenLoadSuccess(tokenResponse.getAccessToken());
        Log.d(TAG, "onLoadSuccess: " + tokenResponse.toString());
    }

    @Override
    public void handleError(Throwable throwable) {
        throwable.printStackTrace();
        if (mView != null) mView.onError(Constants.SERVER_ERROR_MESSAGE);
    }

    @Override
    public void onDetach() {
        mCompositeDisposable.dispose();
        mView = null;
    }
}
