package com.yurab.photoapiclient.screens.main.presenter;

import com.yurab.photoapiclient.screens.base.BasePresenter;

public interface MainActivityPresenter extends BasePresenter {
    void getUserToken(String code);
}
