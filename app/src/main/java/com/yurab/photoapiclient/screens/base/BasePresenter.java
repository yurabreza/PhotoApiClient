package com.yurab.photoapiclient.screens.base;

public interface BasePresenter {
    String TAG = BasePresenter.class.getSimpleName();
    String VIEW_CAST_ERROR = "view must be instance of ";

    void onAttach(BaseView view);

    void handleError(Throwable throwable);

    void onDetach();
}
