package com.example.bitter;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

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

import static java.lang.Integer.parseInt;

public class TabsActivity extends AppCompatActivity{

    private ViewPager viewPager;            // Visualizzatore di pagine
    private TabLayout tabLayout;            // Layout delle tabs
    private MapFragment mapFragment;        // Fragment che genera la mappa
    private ShopFragment shopFragment;      // Fragment che genera i negozi

    private int capacity;

    // thread che controlla se i sensori sono attivi
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            isPermissionOk();
            handler.postDelayed(this, 8500);
        }
    };

    // creazione della tab activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs);

        Query currcap= MainActivity.info.getInfo().child("Mall_List").child("Nave_de_Vero").child("Currcap");
        currcap.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    capacity=parseInt(dataSnapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        MainActivity.info.pushInfo("Mall_List/Nave_de_Vero/Inside_User/List", MainActivity.info.getUser().getCode().toString(), MainActivity.info.getUser().getCode().toString());

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

    @Override
    public void onResume() {
        super.onResume();
        handler.postDelayed(runnable, 500);
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    // funzione che si occupa di gestire la gesture di chiusura con il tasto fisico
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

    @Override
    public void onDestroy(){
        super.onDestroy();
        capacity--;
        MainActivity.info.pushInfo("Mall_List/Nave_de_Vero", "Currcap", ""+capacity);
        MainActivity.info.removeInfo("Mall_List/Nave_de_Vero/Inside_User/List", MainActivity.info.getUser().getCode().toString());
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

    private void showToast (String text){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    // funzione che controlla se i permessi sono attivi
    private boolean isPermissionOk(){
        if(isConnected() && isGPSEnabled() && isBluetoothEnabled()){
            return true;
        }else{
            finish();
        }
        return false;
    }

    // funzione che si occupa di controllare se il telefono è connesso ad internet
    private boolean isConnected(){
        ConnectivityManager connectivityManager= (ConnectivityManager) this.getSystemService(this.CONNECTIVITY_SERVICE);

        NetworkInfo wifiConn= connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn= connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if((wifiConn!=null && wifiConn.isConnected()) || (mobileConn!=null && mobileConn.isConnected())){
            return true;
        }
        return false;
    }

    // funzione che si occupa di controllare se il telefono ha il GPS attivo
    private boolean isGPSEnabled(){
        LocationManager locationManager= (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean providerEnabled= locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(providerEnabled){
            return true;
        }
        return false;
    }

    // funzione che si occupa di controllare se il telefono ha il bluetooth attivo
    private boolean isBluetoothEnabled(){
        if(MainActivity.mAdapter.isEnabled()) {
            return true;
        }
        return false;
    }

}