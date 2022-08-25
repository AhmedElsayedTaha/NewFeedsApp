package com.linkdevtask.UI.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.linkdevtask.Models.Article;
import com.linkdevtask.R;
import com.linkdevtask.databinding.ActivityArticleDetailsBinding;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ArticleDetailsActivity extends AppCompatActivity {

    ActivityArticleDetailsBinding binding;
    Article article;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityArticleDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if(getIntent()!=null){
            String  json = getIntent().getStringExtra("json");
            TypeToken<Article> token = new TypeToken<Article>() {};
            article =  new Gson().fromJson(json,token.getType());
            Glide.with(this)
                    .load(article.getUrlToImage())
                    .centerCrop()
                    .placeholder(R.drawable.placeholder)
                    .into(binding.image);

            binding.titleTv.setText(article.getTitle());
            String by =getString(R.string.by)+" "+article.getAuthor();
            binding.byTv.setText(by);

            if(article.getPublishedAt().contains("T")){
                String[] dateArr = article.getPublishedAt().split("T");
                DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                DateFormat targetFormat = new SimpleDateFormat("MMMM dd, yyyy");
                try {
                    Date date = originalFormat.parse(dateArr[0]);
                    assert date != null;
                    String formattedDate = targetFormat.format(date);
                    binding.dateTv.setText(formattedDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            else {
                binding.dateTv.setText(article.getPublishedAt());
            }

            binding.descriptionTv.setText(article.getDescription());

            binding.browsBtn.setOnClickListener(view -> {
                Intent intent = new Intent( Intent.ACTION_VIEW , Uri.parse( article.getUrl() ) );
                startActivity(intent);
            });
        }
    }
}