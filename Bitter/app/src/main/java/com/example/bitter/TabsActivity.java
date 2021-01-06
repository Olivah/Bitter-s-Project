package com.example.bitter;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;
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
import java.util.Timer;

import static com.google.android.gms.nearby.Nearby.getMessagesClient;

public class TabsActivity extends AppCompatActivity{

    private ViewPager viewPager;              // Visualizzatore di pagine
    private TabLayout tabLayout;              // Layout delle tabs
    private MapFragment mapFragment;          // Fragment che genera la mappa
    private ShopFragment shopFragment;        // Fragment che genera i negozi
    private SignalFragment signalFragment;    // Fragment che genera le segnalazioni

    // variabili utili per la gestione della vicinanza degli utenti
    private MessageListener mMessageListener;
    private Message mMessage;
    private Activity context=this;
    private BluetoothAdapter myAdapter= BluetoothAdapter.getDefaultAdapter();
    private String myName= myAdapter.getName();
    private ArrayList<String> devices= new ArrayList<>();

    // thread che controlla se i sensori sono attivi
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            isPermissionOk();
            handler.postDelayed(this, 8500);
        }
    };

    private Timer timer= new Timer();

    // creazione della tab activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs);

        viewPager=findViewById(R.id.view_pager);
        tabLayout=findViewById(R.id.tabs);

        mapFragment=new MapFragment();
        shopFragment=new ShopFragment();
        signalFragment=new SignalFragment();

        tabLayout.setupWithViewPager(viewPager);

        // Inserisco elementi nel viewPager creando un viewPagerAdapter
        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager(), 0);
        viewPagerAdapter.addFragment(mapFragment, "MAPPA");
        viewPagerAdapter.addFragment(shopFragment, "NEGOZI");
        viewPagerAdapter.addFragment(signalFragment, "SEGNALAZIONI");
        viewPager.setAdapter(viewPagerAdapter);

        mMessageListener= new MessageListener() {
            @Override
            public void onFound(Message message) {
                if(!devices.contains(message.toString())) {
                    Log.d("NEARBY-CONN", "Found message: " + new String(message.getContent()));
                    devices.add(message.toString());
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);

                    builder.setTitle("");
                    builder.setMessage("Stai attento hai dispositivi nelle vicinanze");
                    builder.setCancelable(true);
                    builder.show();
                }
            }

            @Override
            public void onLost(Message message) {
                Log.d("NEARBY-CONN", "Lost sight of message: " + new String(message.getContent()));
            }
        };
        mMessage= new Message(myName.getBytes());
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

    @Override
    public void onStart() {
        super.onStart();
        Nearby.
        getMessagesClient(this).publish(mMessage);
        getMessagesClient(this).subscribe(mMessageListener);
    }

    @Override
    public void onStop() {
        getMessagesClient(this).unpublish(mMessage);
        getMessagesClient(this).unsubscribe(mMessageListener);
        super.onStop();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        timer.cancel();
        timer.purge();
        timer=null;
        MainActivity.info.dequeueMall("Nave_de_Vero");
        showToast("Arrivederci, a presto!");
    }

    // funzione che si occupa di gestire la gesture di chiusura con il tasto fisico
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Sei sicuro di voler uscire? ");
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if(!ShopFragment.isInside) {
                    finish();
                }else{
                    showToast("Esci dal negozio prima!");
                }
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.show();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK)
            shopFragment.enterSuccess();
        super.onActivityResult(requestCode, resultCode, data);
    }
}