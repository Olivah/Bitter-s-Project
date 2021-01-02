package com.example.bitter;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragment extends Fragment implements OnMapReadyCallback{

    private GoogleMap mGoogleMap;           // Variabile locale privata per gestire le informazioni relative alla mappa
    private FragmentActivity context;

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

        // controllo di avere i permessi per la posizione dell'utente
        if(!(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)){
            ActivityCompat.requestPermissions(context,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MainActivity.PERMISSION_REQUEST_ACCESS_FINE_LOCATION);
        }

        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.getUiSettings().setCompassEnabled(true);
        mGoogleMap.getUiSettings().setAllGesturesEnabled(true);
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        mGoogleMap.getUiSettings().setMapToolbarEnabled(true);

        // centro la camera nel centro commerciale e ci metto un marker
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(MainActivity.MALL_BOUNDS.getCenter(), 17), 10, null);
        mGoogleMap.addMarker(new MarkerOptions()
                .position(MainActivity.MALL_BOUNDS.getCenter())
                .title("Nave de Vero"));

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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context= getActivity();
    }
}