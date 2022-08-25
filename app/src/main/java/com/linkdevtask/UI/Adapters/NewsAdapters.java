package com.linkdevtask.UI.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.linkdevtask.Models.Article;
import com.linkdevtask.R;
import com.linkdevtask.databinding.NewsItemLayoutBinding;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NewsAdapters extends RecyclerView.Adapter<NewsAdapters.MyViewHolder> implements Filterable {

    private Context context;
    OnArticleClickListener onArticleClickListener;
    private List<Article> articleList = new ArrayList<>();
    private List<Article> filteredList;

    public NewsAdapters(Context context,List<Article> articles, OnArticleClickListener onArticleClickListener) {
        this.context = context;
        this.articleList=articles;
        this.filteredList=new ArrayList<>(articles);
        this.onArticleClickListener = onArticleClickListener;

    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                List<Article> articles = new ArrayList<>();

                if(charSequence==null||charSequence.length()==0){
                    articles.addAll(filteredList);
                }
                else {
                    String searchString = charSequence.toString().toLowerCase();
                    for(Article article:filteredList){
                       if( article.getTitle().toLowerCase().contains(searchString)){
                           articles.add(article);
                       }
                    }

                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = articles;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
               articleList.clear();
               articleList.addAll((Collection<? extends Article>) filterResults.values);
                notifyDataSetChanged();
            }
        };
        return filter;
    }

    public interface OnArticleClickListener{
        void onClick(int position);
    }

    @NonNull
    @Override
    public NewsAdapters.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        NewsItemLayoutBinding binding = NewsItemLayoutBinding.inflate(LayoutInflater.from(context),parent,false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapters.MyViewHolder holder, int position) {

        Glide.with(context)
                .load(articleList.get(position).getUrlToImage())
                .placeholder(R.drawable.placeholder)
                .centerCrop()
                .into(holder.image);

        holder.titleTv.setText(articleList.get(position).getTitle());
        String by =context.getResources().getString(R.string.by)+" "+articleList.get(position).getAuthor();
        holder.byTv.setText(by);

        //converting the date
        if(articleList.get(position).getPublishedAt().contains("T")){
            String[] dateArr = articleList.get(position).getPublishedAt().split("T");
            DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            DateFormat targetFormat = new SimpleDateFormat("MMMM dd, yyyy");

            try {
              Date date = originalFormat.parse(dateArr[0]);
                assert date != null;
                String formattedDate = targetFormat.format(date);
                holder.dateTv.setText(formattedDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }


        }
        else {
            holder.dateTv.setText(articleList.get(position).getPublishedAt());
        }

        //applying the click listener interface to the view
        holder.itemView.setOnClickListener(view -> onArticleClickListener.onClick(position));


    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView titleTv,byTv,dateTv;
        public MyViewHolder(@NonNull NewsItemLayoutBinding binding) {
            super(binding.getRoot());

            image = binding.image;
            titleTv = binding.titleTv;
            byTv = binding.byTv;
            dateTv = binding.dateTv;
        }
    }


}
