package com.yurab.photoapiclient.screens.random_photo.view;

import com.yurab.photoapiclient.model.response.random_photo.RandomPhotoResponse;
import com.yurab.photoapiclient.screens.base.BaseView;


public interface RandomPhotoView extends BaseView {
    void onPhotoLoadSuccess(RandomPhotoResponse photo);
}
