package com.example.bitter.Class;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static java.lang.Integer.parseInt;

//Classe per gestire le code del centro commerciale e dei relativi negozi al suo interno
public class Myqueue {

    //campo people lista di persone all'interno del centro commerciale/negozio
    private ArrayList<User> queue;
    private int maxcap;
    private int currcap;
    private Information database;

    //costruttore
    public Myqueue(Information info) {
        database = info;

        final Query current= database.getInfo("Mall_List/Nave_de_Vero/Currcap");
        current.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    //currcap=parseInt(dataSnapshot.getValue().toString());
                    currcap=parseInt(dataSnapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final Query max= database.getInfo("Mall_List/Nave_de_Vero/Maxcap");
        max.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    //currcap=parseInt(dataSnapshot.getValue().toString());
                    maxcap=parseInt(dataSnapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //mette un utente nella relativa coda
    public void enqueue() {
        if(currcap<maxcap){

        }
    }

    //elimina la prima persona dalla coda cioè quella in posizione 0 e la restituisce
    public void dequeue() {

    }
}


