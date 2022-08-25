package com.linkdevtask.UI.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.linkdevtask.Models.DrawerModel;
import com.linkdevtask.R;
import com.linkdevtask.SessionManager.Consts;
import com.linkdevtask.UI.Adapters.MenuItemsAdapter;
import com.linkdevtask.UI.Fragments.HomeFragment;
import com.linkdevtask.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    ImageView arrow;
    List<DrawerModel> drawerModels = new ArrayList<>();
    MenuItemsAdapter menuItemsAdapter;
    HomeFragment homeFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binding.drawer, binding.toolbar, R.string.nav_open, R.string.nav_close);
        binding.drawer.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();



        binding.search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag(Consts.HOME);
                if(homeFragment!=null&&homeFragment.isVisible()){
                    homeFragment.applyFilterInAdapter(newText);
                }
                return false;
            }
        });


        View header_view = binding.navigationView.getHeaderView(0);
        arrow = header_view.findViewById(R.id.arrow);
        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               closeDrawer();
            }
        });

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout,new HomeFragment(), Consts.HOME);
        transaction.commit();



        //filling the navigation view menu
        drawerModels.add(new DrawerModel(R.drawable.explore,getString(R.string.explore)));
        drawerModels.add(new DrawerModel(R.drawable.live,getString(R.string.live_chat)));
        drawerModels.add(new DrawerModel(R.drawable.gallery,getString(R.string.gallery)));
        drawerModels.add(new DrawerModel(R.drawable.wishlist,getString(R.string.wish_list)));
        drawerModels.add(new DrawerModel(R.drawable.emagazine,getString(R.string.e_magazine)));

        menuItemsAdapter = new MenuItemsAdapter(drawerModels,this);
        binding.menuRec.setLayoutManager(new LinearLayoutManager(this));
        binding.menuRec.setAdapter(menuItemsAdapter);

        //for handling exceptions fo the RXJava
        RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {

                Log.e("exception is ",throwable.getMessage());
            }
        });




    }



    public void showToast(String name){
        Toast.makeText(this, name, Toast.LENGTH_LONG).show();
    }

    public void closeDrawer(){
        binding.drawer.closeDrawer(Gravity.LEFT);
    }

}