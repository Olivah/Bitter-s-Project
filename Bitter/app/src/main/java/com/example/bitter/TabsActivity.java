package com.example.bitter;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class TabsActivity extends AppCompatActivity {

    private ViewPager viewPager;            // Visualizzatore di pagine
    private TabLayout tabLayout;            // Layout delle tabs
    private MapFragment mapFragment;        // Fragment che genera la mappa
    private ShopFragment shopFragment;      // Fragment che genera i negozi

    // creazione della tab activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs);

        viewPager=findViewById(R.id.view_pager);
        tabLayout=findViewById(R.id.tabs);

        mapFragment=new MapFragment();
        shopFragment=new ShopFragment();

        tabLayout.setupWithViewPager(viewPager);

        // Inserisco elementi nel viewPager creando un viewPagerAdapter
        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager(), 0);
        viewPagerAdapter.addFragment(mapFragment, "MAPPA");
        viewPagerAdapter.addFragment(shopFragment, "NEGOZI");
        viewPager.setAdapter(viewPagerAdapter);

        showToast("Accesso eseguito");
    }

    // Classe che si occupa dell'adattazione delle pagine
    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments=new ArrayList<>();
        private List<String> fragmentTitle=new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        public void addFragment(Fragment fragment, String title){
            fragments.add(fragment);
            fragmentTitle.add(title);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitle.get(position);
        }
    }

    public void addSignal(View v){
        Intent i= new Intent(this, SignalActivity.class);
        startActivity(i);
        this.onPause();
    }

    public void exitHandler(View v){
        this.onBackPressed();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Sei sicuro di voler uscire? ");
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void showToast (String text){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}