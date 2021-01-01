package com.example.bitter;

import android.Manifest;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import com.example.bitter.Class.Coordinates;
import com.example.bitter.Class.Information;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import static java.lang.Double.parseDouble;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    // variabili locali utili all'activity
    private static final int REQUEST_ENABLE_BT=0;
    private static final int REQUEST_DISCOVER_BT=1;
    private final int PLAY_SERVICES_ERROR_CODE=9002;
    public final static int PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 9003;

    public static LatLngBounds ITALY_BOUNDS;
    public static LatLngBounds MALL_BOUNDS;
    public static Information info;
    public static Timer timer;
    public static BluetoothAdapter mAdapter;
    private static Coordinates mall;
    private GoogleMap mGoogleMap;

    // thread che controlla se i sensori sono attivi
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            isPermissionOk();
            handler.postDelayed(this, 8500);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // controllo se l'utente ha l'API adeguata
        isServicesOk();

        // creo un'istanza di variabili globali utili nelle varie activity
        info= new Information();

        // prendo il codice univoco appena generato per l'utente e lo inserisco nella lista degli utenti attivi
        info.pushInfo("User_List",info.getUser().getCode().toString(),info.getUser().getCode().toString());
        ((TextView)findViewById(R.id.user_code)).setText(((TextView)findViewById(R.id.user_code)).getText().toString().replace("?", info.getUser().getCode().toString()));

        // imposto le coordinate da visualizzare nella mappa
        settingCoordinates();

        // carico la mappa nel layout della main activity
        SupportMapFragment supportMapFragment=(SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_fragment);
        supportMapFragment.getMapAsync(this);
    }

    // funzione che si occupa di quando l'activity viene distrutta
    @Override
    public void onDestroy() {
        super.onDestroy();
        info.removeInfo("User_List", info.getUser().getCode().toString());
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

    // funzione che si occupa di inizializzare le coordinate da visualizzare nella mappa
    public void settingCoordinates(){
        mall=new Coordinates();

        // mi prendo la latitudine da firebase
        Query latQuery= info.getInfo();
        latQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    String temp=dataSnapshot.child("Mall_List").child("Nave_de_Vero").child("Coordinates").child("Lat").getValue().toString();
                    double x=parseDouble(temp);
                    mall.setLat(x);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // mi prendo la longitudine da firebase
        Query lngQuery= info.getInfo();
        lngQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    String temp=dataSnapshot.child("Mall_List").child("Nave_de_Vero").child("Coordinates").child("Lng").getValue().toString();
                    double x=parseDouble(temp);
                    mall.setLng(x);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // aspetto 800 ms perchè è il tempo che impiega il listener a caricare nella variabile la longitudine e la latitudine
        // FORSE PERCHE' HA BASE NEGLI STATI UNITI? LA SCELTA ERA TRA BELGIO IN BETA O US VERSIONE NORMALE
        SystemClock.sleep(800);
    }

    // funzione di inizializzazione della mappa
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap=googleMap;

        // controllo di avere i permessi per la posizione dell'utente
        if(!(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MainActivity.PERMISSION_REQUEST_ACCESS_FINE_LOCATION);
        }

        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.getUiSettings().setCompassEnabled(true);
        mGoogleMap.getUiSettings().setAllGesturesEnabled(true);
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        mGoogleMap.getUiSettings().setMapToolbarEnabled(true);

        double mallBottomBoundry= mall.getLat() -0.1;
        double mallLeftBoundry= mall.getLng() -0.1;
        double mallTopBoundry= mall.getLat() +0.1;
        double mallRightBoundry= mall.getLng() +0.1;

        // assegno al bound del centro commerciale dei limiti
        MALL_BOUNDS= new LatLngBounds(
                new LatLng(mallBottomBoundry,mallLeftBoundry),
                new LatLng(mallTopBoundry,mallRightBoundry)
        );

        double italyBottomBoundry= mall.getLat() -2.5;
        double italyLeftBoundry= mall.getLng() -2.5;
        double italyTopBoundry= mall.getLat() +2.5;
        double italyRightBoundry= mall.getLng() +2.5;

        // assegno al bound dell'italia dei limiti
        ITALY_BOUNDS= new LatLngBounds(
                new LatLng(italyBottomBoundry,italyLeftBoundry),
                new LatLng(italyTopBoundry,italyRightBoundry)
        );

        // centro la camera nel centro commerciale e ci metto un marker
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(MALL_BOUNDS.getCenter(), 17), 500, null);
        mGoogleMap.addMarker(new MarkerOptions()
                .position(MALL_BOUNDS.getCenter())
                .title("Nave de Vero"));
        // limito la mappa navigabile intorno all'italia, non di zoom, ma di trascinata
        mGoogleMap.setLatLngBoundsForCameraTarget(ITALY_BOUNDS);
    }

    // funzione che si occupa di far entrare un utente nel centro commerciale
    public void enterHandler(View v){
        Intent i= new Intent(this, QueueActivity.class);
        startActivity(i);
    }

    // funzione che si occupa di controllare se i servizi necessari  all'applicazione
    // sono attivi, come l'API, la connessione internet, il gps e il bluetooth
    private boolean isServicesOk(){
        GoogleApiAvailability googleApi= GoogleApiAvailability.getInstance();
        int result= googleApi.isGooglePlayServicesAvailable(this);
        if(result == ConnectionResult.SUCCESS){
            return true;
        }else if(googleApi.isUserResolvableError(result)){
            Dialog dialog=googleApi.getErrorDialog(this, result, PLAY_SERVICES_ERROR_CODE);
            dialog.show();
        }
        return false;
    }

    // funzione che controlla se i permessi sono attivi
    private boolean isPermissionOk(){
        if(isConnected() && isGPSEnabled() && isBluetoothEnabled()){
            return true;
        }else{
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle("Sensori richiesti")
                    .setMessage("L'applicazione necessita dei seguenti sensori:\n\n-Internet\n-Bluetooth\n-GPS\n\nControlla di averli attivi!")
                    .setCancelable(false)
                    .show();

            timer= new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    finish();
                }
            }, 8000);
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
        mAdapter= BluetoothAdapter.getDefaultAdapter();
        if(mAdapter.isEnabled()) {
            return true;
        }
        return false;
    }
}