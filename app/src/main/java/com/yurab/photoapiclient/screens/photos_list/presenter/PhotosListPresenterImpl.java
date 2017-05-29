package com.yurab.photoapiclient.screens.photos_list.presenter;

import android.util.Log;

import com.yurab.photoapiclient.global.Constants;
import com.yurab.photoapiclient.model.response.LikeResponse;
import com.yurab.photoapiclient.model.response.Photo;
import com.yurab.photoapiclient.network.UnsplashClient;
import com.yurab.photoapiclient.screens.base.BaseView;
import com.yurab.photoapiclient.screens.photos_list.view.PhotosListView;
import com.yurab.photoapiclient.tools.Prefs;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PhotosListPresenterImpl implements PhotosListPresenter {

    private PhotosListView mView;
    private Observable<List<Photo>> mPhotosObservable;
    private Observable<LikeResponse> mLikePhotoObservable;
    private Observable<LikeResponse> mUnLikePhotoObservable;

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
        mPhotosObservable = UnsplashClient.getApiInterface()
                .getPhotos(Prefs.getHeader(),
                        String.valueOf(page),
                        String.valueOf(perPage),
                        orderBy);

        mPhotosObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onPhotosLoadSuccess, this::handleError);
    }

    @Override
    public void likePhoto(String photoId) {
        mLikePhotoObservable = UnsplashClient.getApiInterface().likePhoto(Prefs.getHeader(), photoId);

        mLikePhotoObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onLikeStateChanged, this::handleError);
    }

    @Override
    public void unlikePhoto(String photoId) {
        mUnLikePhotoObservable = UnsplashClient.getApiInterface().likePhoto(Prefs.getHeader(), photoId);

        mUnLikePhotoObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onLikeStateChanged, this::handleError);
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
        mView.onError(Constants.SERVER_ERROR_MESSAGE);
    }

    @Override
    public void onDetach() {
        mPhotosObservable.unsubscribeOn(Schedulers.io());
        mLikePhotoObservable.unsubscribeOn(Schedulers.io());
        mUnLikePhotoObservable.unsubscribeOn(Schedulers.io());
        mView = null;
    }
}
