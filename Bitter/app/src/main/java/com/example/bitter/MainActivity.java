package com.example.bitter;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static android.widget.Toast.*;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private final int PLAY_SERVICES_ERROR_CODE=9002;
    private GoogleMap mGoogleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isServicesOk();

        SupportMapFragment supportMapFragment=(SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_fragment);
        supportMapFragment.getMapAsync(this);
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
        Intent i= new Intent(this, TabsActivity.class);
        startActivity(i);
    }
}