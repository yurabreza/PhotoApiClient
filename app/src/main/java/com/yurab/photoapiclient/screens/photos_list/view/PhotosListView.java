package com.yurab.photoapiclient.screens.photos_list.view;

import com.yurab.photoapiclient.model.response.Photo;
import com.yurab.photoapiclient.screens.base.BaseView;

import java.util.List;


public interface PhotosListView extends BaseView {
    void onPhotosLoadSuccess(List<Photo> photos);
}
