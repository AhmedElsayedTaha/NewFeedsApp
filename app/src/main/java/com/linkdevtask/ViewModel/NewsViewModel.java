package com.linkdevtask.ViewModel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.linkdevtask.Models.Article;
import com.linkdevtask.Models.NewsModel;
import com.linkdevtask.Models.Resource;
import com.linkdevtask.Models.SingleLiveEvent;
import com.linkdevtask.Repositories.NewsRepository;

import java.util.List;

import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.exceptions.UndeliverableException;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.plugins.RxJavaPlugins;

public class NewsViewModel extends ViewModel {

    private NewsRepository newsRepository = NewsRepository.getNewsRepositoryInstance();
    private CompositeDisposable disposable = new CompositeDisposable();

    private SingleLiveEvent<Resource<List<Article>>> newsModelMLD = new SingleLiveEvent<>();

    private SingleLiveEvent<Boolean> newsPP = new SingleLiveEvent<>();

    public LiveData<Resource<List<Article>>> getNewsModelMLD() {
        return newsModelMLD;
    }
    public LiveData<Boolean> getNewsPP() {
        return newsPP;
    }


    public void getNews(String sourceOne, String apiKeyOne,String sourceTwo){
        newsPP.postValue(true);
        if (!disposable.isDisposed()) {
            disposable.add(newsRepository.getFirstArticles(sourceOne, apiKeyOne, sourceTwo)
                    .subscribeWith(new DisposableSingleObserver<List<Article>>() {
                        @Override
                        public void onSuccess(@NonNull List<Article> articles) {
                            newsPP.postValue(false);
                            newsModelMLD.postValue(Resource.success(articles));
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            newsPP.postValue(false);
                            newsModelMLD.postValue(Resource.error(e.getMessage(), null));
                        }
                    }));
        }
    }

    @Override
    protected void onCleared() {
        disposable.dispose();
        super.onCleared();

    }
}
