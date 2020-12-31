package com.example.bitter;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.SystemClock;
import android.view.View;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import static java.lang.Double.parseDouble;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    // variabili locali utili all'activity
    private final int PLAY_SERVICES_ERROR_CODE=9002;
    public static LatLngBounds ITALY_BOUNDS;
    public static LatLngBounds MALL_BOUNDS;
    private static Information info;
    private static Coordinates mall;
    private GoogleMap mGoogleMap;
    private Timer timer;

    public static Information getInformation(){
        return info;
    }

    public static Coordinates getMall(){
        return mall;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // controllo se l'utente ha l'API adeguata
        isServicesOk();

        // controllo se l'utente può connettersi ad internet, altrimenti lo avverto e chiudo l'app
        if(!isConnected()){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Non sei connesso ad una rete internet, connettiti e riprova");
            builder.show();

            timer= new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    finish();
                }
            }, 3000);
        }

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
        mGoogleMap.getUiSettings().setCompassEnabled(true);
        mGoogleMap.getUiSettings().setAllGesturesEnabled(true);
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        mGoogleMap.getUiSettings().setMapToolbarEnabled(true);

        double mallBottomBoundry= mall.getLat() -0.1;
        double mallLeftBoundry= mall.getLng() -0.1;
        double mallTopBoundry= mall.getLat() +0.1;
        double mallRightBoundry= mall.getLng() +0.1;

        // map window
        MALL_BOUNDS= new LatLngBounds(
                new LatLng(mallBottomBoundry,mallLeftBoundry),
                new LatLng(mallTopBoundry,mallRightBoundry)
        );

        double italyBottomBoundry= mall.getLat() -2.5;
        double italyLeftBoundry= mall.getLng() -2.5;
        double italyTopBoundry= mall.getLat() +2.5;
        double italyRightBoundry= mall.getLng() +2.5;

        // map window
        ITALY_BOUNDS= new LatLngBounds(
                new LatLng(italyBottomBoundry,italyLeftBoundry),
                new LatLng(italyTopBoundry,italyRightBoundry)
        );

        // centro la camera nel centro commerciale
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(MALL_BOUNDS.getCenter(), 17), 500, null);

        // limito la mappa navigabile intorno all'italia, non di zoom, ma di trascinata
        mGoogleMap.setLatLngBoundsForCameraTarget(ITALY_BOUNDS);
    }

    // funzione che si occupa di far entrare un utente nel centro commerciale
    public void enterHandler(View v){
        Intent i= new Intent(this, QueueActivity.class);
        startActivity(i);
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

    // funzione che si occupa di quando l'activity viene distrutta
    @Override
    public void onDestroy() {
        super.onDestroy();
        info.removeInfo("User_List", info.getUser().getCode().toString());
    }

    // funzione che si occupa di controllare se l'API del telefono è quella necessaria
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

    // funzione che si occupa di controllare se il telefono è connesso ad internet
    private boolean isConnected(){
        ConnectivityManager connectivityManager= (ConnectivityManager) this.getSystemService(this.CONNECTIVITY_SERVICE);

        NetworkInfo wifiConn= connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn= connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if((wifiConn!=null && wifiConn.isConnected()) || (mobileConn!=null && mobileConn.isConnected())){
            return true;
        }else{
            return false;
        }
    }
}