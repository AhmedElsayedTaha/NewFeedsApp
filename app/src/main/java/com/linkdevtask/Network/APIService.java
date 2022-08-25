package com.linkdevtask.Network;

import com.linkdevtask.Models.NewsModel;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface APIService {

    @GET("articles")
    Single<NewsModel> getFirstArticles(@Query("source") String source,
                                       @Query("apiKey") String apiKey);

    @GET("articles")
    Single<NewsModel> getSecondArticles(@Query("source") String source,
                                       @Query("apiKey") String apiKey);
}
