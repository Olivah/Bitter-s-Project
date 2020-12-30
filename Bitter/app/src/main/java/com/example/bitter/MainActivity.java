package com.example.bitter;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.bitter.Class.Information;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    // variabili locali utili all'activity
    private final int PLAY_SERVICES_ERROR_CODE=9002;
    private GoogleMap mGoogleMap;
    private static Information globalVariables;

    public static Information getInformation(){
        return globalVariables;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isServicesOk();

        SupportMapFragment supportMapFragment=(SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_fragment);
        supportMapFragment.getMapAsync(this);

        globalVariables= new Information();
        Information.pushInfo("User_Code", globalVariables.getUser().getCode().toString(), globalVariables.getUser().getCode().toString());
        ((TextView)findViewById(R.id.user_code)).setText(((TextView)findViewById(R.id.user_code)).getText().toString().replace("?", globalVariables.getUser().getCode().toString()));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap=googleMap;
        mGoogleMap.getUiSettings().setCompassEnabled(true);
        mGoogleMap.getUiSettings().setAllGesturesEnabled(true);
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        mGoogleMap.getUiSettings().setMapToolbarEnabled(true);

        LatLng latLng = new LatLng(45.458881659857745, 12.213175861536298);
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17), 500, null);
    }

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

    public void enterHandler(View v){
        Intent i= new Intent(this, QueueActivity.class);
        startActivity(i);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        Information.removeInfo("User_Code", globalVariables.getUser().getCode().toString());
    }
}