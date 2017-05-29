package com.yurab.photoapiclient.screens.photos_list.presenter;

import android.util.Log;

import com.yurab.photoapiclient.global.Constants;
import com.yurab.photoapiclient.model.network.UnsplashClient;
import com.yurab.photoapiclient.model.response.LikeResponse;
import com.yurab.photoapiclient.model.response.Photo;
import com.yurab.photoapiclient.screens.base.BaseView;
import com.yurab.photoapiclient.screens.photos_list.view.PhotosListView;
import com.yurab.photoapiclient.tools.Prefs;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class PhotosListPresenterImpl implements PhotosListPresenter {

    private PhotosListView mView;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @Override
    public void onAttach(BaseView view) {
        if (view instanceof PhotosListView) {
            mView = (PhotosListView) view;
        } else {
            throw new RuntimeException(VIEW_CAST_ERROR + PhotosListView.class.getSimpleName());
        }
    }

    @Override
    public void getPhotos(int page, int perPage, String orderBy) {
        mCompositeDisposable.add(UnsplashClient.getApiInterface()
                .getPhotos(Prefs.getHeader(),
                        String.valueOf(page),
                        String.valueOf(perPage),
                        orderBy)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onPhotosLoadSuccess, this::handleError));
    }

    @Override
    public void likePhoto(String photoId) {
        mCompositeDisposable.add(UnsplashClient.getApiInterface()
                .likePhoto(Prefs.getHeader(), photoId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onLikeStateChanged, this::handleError));
    }

    @Override
    public void unlikePhoto(String photoId) {
        mCompositeDisposable.add(UnsplashClient.getApiInterface()
                .unlikePhoto(Prefs.getHeader(), photoId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onLikeStateChanged, this::handleError));
    }

    private void onLikeStateChanged(LikeResponse likeResponse) {
        Log.d(TAG, "onLikeStateChanged: " + likeResponse.toString());
    }


    private void onPhotosLoadSuccess(List<Photo> photos) {
        mView.onPhotosLoadSuccess(photos);
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
