package com.example.bitter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bitter.Class.Coordinates;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class MapFragment extends Fragment implements OnMapReadyCallback{

    private GoogleMap mGoogleMap;           // Variabile locale privata per gestire le informazioni relative alla mappa
    private Coordinates mall;

    // Metodo invocato quando viene creato l'oggetto
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("success","onCreateView");
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    // Metodo invocato quando la mappa è pronta per essere modificata
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap=googleMap;
        mGoogleMap.getUiSettings().setCompassEnabled(true);
        mGoogleMap.getUiSettings().setAllGesturesEnabled(true);
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        mGoogleMap.getUiSettings().setMapToolbarEnabled(true);

        // centro la camera nel centro commerciale
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(MainActivity.MALL_BOUNDS.getCenter(), 17), 500, null);

        // limito la mappa navigabile intorno all'italia, non di zoom, ma di trascinata
        mGoogleMap.setLatLngBoundsForCameraTarget(MainActivity.ITALY_BOUNDS);
    }

    // Metodo invocato quando viene creata la view relativa all'oggetto
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().
                findFragmentById(R.id.fragment_map);
        mapFragment.getMapAsync(this);
    }
}