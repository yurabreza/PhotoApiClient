package com.yurab.photoapiclient.network;

import com.yurab.photoapiclient.model.request.GetTokenRequest;
import com.yurab.photoapiclient.model.response.LikeResponse;
import com.yurab.photoapiclient.model.response.Photo;
import com.yurab.photoapiclient.model.response.TokenResponse;
import com.yurab.photoapiclient.model.response.random_photo.RandomPhotoResponse;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApiInterface {
    /**
     * gets Access_token after user logged in for further interactions
     *
     * @param grantType Value “authorization_code”.
     */
    @POST()
    Observable<TokenResponse> getToken(@Url String url, @Body GetTokenRequest getTokenRequest);

    @GET("/photos")
    Observable<List<Photo>> getPhotos(@Header("Authorization") String header,
                                      @Query("page") String page,
                                      @Query("per_page") String perPage,
                                      @Query("order_by") String orderBy);
    @GET("/photos/random")
    Observable<RandomPhotoResponse> getRandomPhoto(@Header("Authorization") String header);

    @POST("/photos/{id}/like")
    Observable<LikeResponse> likePhoto(@Header("Authorization") String header,
                                       @Path("id") String photoId);

    @DELETE("/photos/{id}/like")
    Observable<LikeResponse> unlikePhoto(@Header("Authorization") String header,
                                       @Path("id") String photoId);
}
