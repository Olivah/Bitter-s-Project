package com.example.bitter.Class;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static java.lang.Integer.parseInt;

public class Information {

    private User user;
    private DatabaseReference reference;

    private ArrayList<User> queue;
    private int maxcap;
    private int currcap;

    public Information(){

        user= new User();

        final Query current= getInfo("Mall_List/Nave_de_Vero/Currcap");
        current.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    currcap=parseInt(dataSnapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final Query max= getInfo("Mall_List/Nave_de_Vero/Maxcap");
        max.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    maxcap=parseInt(dataSnapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // metodo statico per riferirmi all'utente nelle altre activity
    public User getUser(){
        return user;
    }

    // funzione che inserisce informazioni all'interno del real time database
    public void pushInfo(String path, String child, String value){
        reference= FirebaseDatabase.getInstance("https://bitter-298116-default-rtdb.firebaseio.com/").getReference(path);
        reference.child(child).setValue(value);
    }

    // funzione che prende informazioni all'interno del real time database
    public DatabaseReference getInfo(String path){
        return reference= FirebaseDatabase.getInstance("https://bitter-298116-default-rtdb.firebaseio.com/").getReference(path);
    }

    // funzione che rimuove informazioni all'interno del real time database
    public void removeInfo(String path, String child){
        reference= FirebaseDatabase.getInstance("https://bitter-298116-default-rtdb.firebaseio.com/").getReference(path);
        reference.child(child).removeValue();
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
