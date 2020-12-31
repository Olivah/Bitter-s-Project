package com.example.bitter.Class;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Information {
    private User user;
    private DatabaseReference reference;

    public Information(){
        user= new User();
    }

    // metodo statico per riferirmi all'utente nelle altre activity
    public User getUser(){
        return user;
    }

    // recupera l'istanza del server Firebase
    public FirebaseDatabase getFirebaseDatabase(){
        return FirebaseDatabase.getInstance("https://bitter-298116-default-rtdb.firebaseio.com/");
    }

    // funzione che inserisce informazioni all'interno del real time database
    public void pushInfo(String path, String child, String value){
        reference= FirebaseDatabase.getInstance("https://bitter-298116-default-rtdb.firebaseio.com/").getReference(path);
        reference.child(child).setValue(value);
    }

    // funzione che prende informazioni all'interno del real time database
    public DatabaseReference getInfo(){
        return reference= FirebaseDatabase.getInstance("https://bitter-298116-default-rtdb.firebaseio.com/").getReference();
    }

    // funzione che rimuove informazioni all'interno del real time database
    public void removeInfo(String path, String child){
        reference= FirebaseDatabase.getInstance("https://bitter-298116-default-rtdb.firebaseio.com/").getReference(path);
        reference.child(child).removeValue();
    }
}
