package com.yurab.photoapiclient.network;

import com.yurab.photoapiclient.model.TokenResponse;

import io.reactivex.Observable;
import retrofit2.http.POST;

public interface ApiInterface {

    @POST("/oauth/token")
    Observable<TokenResponse> getToken();
}
