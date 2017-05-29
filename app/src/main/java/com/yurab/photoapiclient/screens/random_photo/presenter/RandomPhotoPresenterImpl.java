package com.yurab.photoapiclient.screens.random_photo.presenter;

import com.yurab.photoapiclient.global.Constants;
import com.yurab.photoapiclient.model.network.UnsplashClient;
import com.yurab.photoapiclient.model.response.random_photo.RandomPhotoResponse;
import com.yurab.photoapiclient.screens.base.BaseView;
import com.yurab.photoapiclient.screens.random_photo.view.RandomPhotoView;
import com.yurab.photoapiclient.tools.Prefs;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class RandomPhotoPresenterImpl implements RandomPhotoPresenter {

    private RandomPhotoView mView;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @Override
    public void onAttach(BaseView view) {
        if (view instanceof RandomPhotoView) {
            mView = (RandomPhotoView) view;
        } else {
            throw new RuntimeException(VIEW_CAST_ERROR + RandomPhotoView.class.getSimpleName());
        }
    }

    @Override
    public void getRandomPhoto() {
        mCompositeDisposable.add(UnsplashClient.getApiInterface()
                .getRandomPhoto(Prefs.getHeader())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onPhotoLoadSuccess, this::handleError));
    }

    @Override
    public void getPhoto(String id) {
        mCompositeDisposable.add(UnsplashClient.getApiInterface()
                .getPhoto(Prefs.getHeader(), id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onPhotoLoadSuccess, this::handleError));
    }

    private void onPhotoLoadSuccess(RandomPhotoResponse randomPhotoResponse) {
        mView.onPhotoLoadSuccess(randomPhotoResponse);
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
