package com.linkdevtask.Repositories;

import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;

import com.linkdevtask.Models.Article;
import com.linkdevtask.Models.NewsModel;
import com.linkdevtask.Network.APIClient;
import com.linkdevtask.Network.APIService;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


public class NewsRepository {
  private static NewsRepository newsRepository;
  private APIService apiService = APIClient.getApiService();

  public static NewsRepository getNewsRepositoryInstance(){
      if(newsRepository==null){
          newsRepository = new NewsRepository();
      }
      return newsRepository;
  }

  public Single<List<Article>> getFirstArticles(String sourceOne, String apiKeyOne,String sourceTwo){

         //return the Single observable
          return apiService.getFirstArticles(sourceOne, apiKeyOne).
                  subscribeOn(Schedulers.io())
                  .zipWith(apiService.getSecondArticles(sourceTwo, apiKeyOne).subscribeOn(Schedulers.io())
                          , new BiFunction<NewsModel, NewsModel, List<Article>>() {
                              @NonNull
                              @Override
                              public List<Article> apply(@NonNull NewsModel newsModel, @NonNull NewsModel newsModel2) {
                                  List<Article> myArticles = new ArrayList<>();
                                  myArticles.addAll(newsModel.getArticles());
                                  myArticles.addAll(newsModel2.getArticles());
                                  return myArticles;
                              }
                          })
                  .observeOn(AndroidSchedulers.mainThread());



  }


}
