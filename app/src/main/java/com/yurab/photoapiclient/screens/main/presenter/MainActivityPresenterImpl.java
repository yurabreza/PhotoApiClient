package com.yurab.photoapiclient.screens.main.presenter;

import android.util.Log;

import com.yurab.photoapiclient.BuildConfig;
import com.yurab.photoapiclient.global.Constants;
import com.yurab.photoapiclient.model.Response.TokenResponse;
import com.yurab.photoapiclient.model.request.GetTokenRequest;
import com.yurab.photoapiclient.network.UnsplashClient;
import com.yurab.photoapiclient.screens.base.BaseView;
import com.yurab.photoapiclient.screens.main.view.MainActivityView;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivityPresenterImpl implements MainActivityPresenter {

    public static final String GRANT_TYPE = "authorization_code";
    public static final String URL_GET_OAUTH_TOKEN = "https://unsplash.com/oauth/token";

    private MainActivityView mView;
    private Observable<TokenResponse> mTokenResponseObservable;

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
        mTokenResponseObservable = UnsplashClient.getApiInterface()
                .getToken(URL_GET_OAUTH_TOKEN,
                        new GetTokenRequest(BuildConfig.UNSPLASH_APPLICATION_ID,
                                BuildConfig.UNSPLASH_SECRET,
                                BuildConfig.UNSPLASH_REDIRECT_URI,
                                code,
                                GRANT_TYPE));

        mTokenResponseObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onLoadSuccess, this::handleError);
    }

    private void onLoadSuccess(TokenResponse tokenResponse) {
        mView.onTokenLoadSuccess(tokenResponse.getAccessToken());
        Log.d(TAG, "onLoadSuccess: " + tokenResponse.toString());
    }

    @Override
    public void handleError(Throwable throwable) {
        mView.onError(Constants.SERVER_ERROR_MESSAGE);
        throwable.printStackTrace();
    }

    @Override
    public void onDetach() {
        if (mTokenResponseObservable != null) {
            mTokenResponseObservable.unsubscribeOn(Schedulers.io());
        }
        mView = null;
    }
}
