package com.linkdevtask.UI.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.linkdevtask.Models.Article;
import com.linkdevtask.Models.Resource;
import com.linkdevtask.SessionManager.Consts;
import com.linkdevtask.UI.Activities.ArticleDetailsActivity;
import com.linkdevtask.UI.Adapters.NewsAdapters;
import com.linkdevtask.UI.Activities.MainActivity;
import com.linkdevtask.ViewModel.NewsViewModel;
import com.linkdevtask.databinding.FragmentHomeBinding;

import java.util.List;
import java.util.Objects;


public class HomeFragment extends Fragment {

    NewsViewModel newsViewModel;
    FragmentHomeBinding binding;
    NewsAdapters newsAdapters;
    ProgressDialog progressDialog;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater,container,false);
        newsViewModel = ViewModelProviders.of(this).get(NewsViewModel.class);
        newsViewModel.getNews(Consts.SOURCE_ONE,Consts.KEY_ONE,Consts.SOURCE_TWO);

        binding.newsRec.setLayoutManager(new LinearLayoutManager(requireActivity()));




        newsViewModel.getNewsModelMLD().observe(requireActivity(), new Observer<Resource<List<Article>>>() {
            @Override
            public void onChanged(Resource<List<Article>> articleListResource) {
                if(articleListResource!=null){
                    if(articleListResource.message!=null){
                        Toast.makeText(getActivity(), articleListResource.message, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        if(articleListResource.data!=null&&articleListResource.data.size()>0){
                            newsAdapters = new NewsAdapters(getActivity(),articleListResource.data, new NewsAdapters.OnArticleClickListener() {
                                @Override
                                public void onClick(int position) {
                                    Gson gson = new Gson();
                                    String json = gson.toJson(articleListResource.data.get(position));
                                    Intent intent = new Intent(requireActivity(), ArticleDetailsActivity.class) ;
                                    intent.putExtra("json",json);
                                    requireActivity().startActivity(intent);
                                }
                            });
                            binding.newsRec.setAdapter(newsAdapters);
                        }
                    }
                }
            }
        });

        newsViewModel.getNewsPP().observe(requireActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean running) {
                if(running)
                    binding.progressBar.setVisibility(View.VISIBLE);
                else
                    binding.progressBar.setVisibility(View.GONE);
            }
        });
        return binding.getRoot();
    }

    public void applyFilterInAdapter(String filterText){
        if(newsAdapters!=null){
            newsAdapters.getFilter().filter(filterText);
        }
    }

}