package com.linkdevtask.UI.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.linkdevtask.Models.DrawerModel;
import com.linkdevtask.UI.Activities.MainActivity;
import com.linkdevtask.databinding.DrawerItemLayoutBinding;

import java.util.List;

public class MenuItemsAdapter extends RecyclerView.Adapter<MenuItemsAdapter.MyViewHolder> {

    private List<DrawerModel> drawerModels ;
    private Activity activity;
    int pos=-1;



    public MenuItemsAdapter(List<DrawerModel> drawerModels, Activity activity) {
        this.drawerModels = drawerModels;
        this.activity = activity;
    }


    @NonNull
    @Override
    public MenuItemsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DrawerItemLayoutBinding binding = DrawerItemLayoutBinding.inflate(LayoutInflater.from(activity),parent,false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuItemsAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.image.setImageResource(drawerModels.get(position).getImage());
        holder.name.setText(drawerModels.get(position).getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(activity!=null){
                    ((MainActivity)activity).showToast(drawerModels.get(position).getName());
                   // ((MainActivity)activity).closeDrawer();
                }
                pos = position;
                notifyDataSetChanged();
            }
        });

        if(pos==position){
            holder.selectedImage.setVisibility(View.VISIBLE);
        }
        else{
            holder.selectedImage.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return drawerModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView selectedImage,image;
        public MyViewHolder(@NonNull DrawerItemLayoutBinding binding) {
            super(binding.getRoot());

            name = binding.name;
            selectedImage = binding.selectedImage;
            image = binding.image;
        }
    }
}
