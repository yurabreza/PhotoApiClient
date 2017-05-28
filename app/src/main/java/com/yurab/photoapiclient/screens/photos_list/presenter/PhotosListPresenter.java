package com.yurab.photoapiclient.screens.photos_list.presenter;

import com.yurab.photoapiclient.screens.base.BasePresenter;


public interface PhotosListPresenter extends BasePresenter {
    void getPhotos(int page, int perPage, String orderBy);
}
