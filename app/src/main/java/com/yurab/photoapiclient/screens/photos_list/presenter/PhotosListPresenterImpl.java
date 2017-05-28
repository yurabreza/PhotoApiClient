package com.yurab.photoapiclient.screens.photos_list.presenter;

import com.yurab.photoapiclient.global.Constants;
import com.yurab.photoapiclient.global.MainApp;
import com.yurab.photoapiclient.model.Response.Photo;
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
                .getPhotos(Constants.HEADER_PREFIX_BEARER + Prefs.loadAccessToken(MainApp.getAppContext()),
                        String.valueOf(page),
                        String.valueOf(page),
                        orderBy);

        mPhotosObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onLoadSuccess, this::handleError);
    }

    private void onLoadSuccess(List<Photo> photos) {
        mView.onPhotosLoadSuccess(photos);
    }

    @Override
    public void handleError(Throwable throwable) {
        throwable.printStackTrace();
        mView.onError(Constants.SERVER_ERROR_MESSAGE);
    }

    @Override
    public void onDetach() {
        mView = null;
    }
}
