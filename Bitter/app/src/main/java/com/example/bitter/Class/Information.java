package com.example.bitter.Class;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Information {
    private static User user;
    private static FirebaseDatabase firebaseDatabase;
    private static DatabaseReference rootNode;

    public Information(){
        user= new User();
        firebaseDatabase= FirebaseDatabase.getInstance("https://bitter-298116-default-rtdb.firebaseio.com/");
    }

    // metodo statico per riferirmi all'utente nelle altre activity
    public static User getUser(){
        return user;
    }

    // recupera l'istanza del server Firebase
    public static FirebaseDatabase getFirebaseDatabase(){
        return firebaseDatabase;
    }

    // recupera l'istanza del server Firebase e ne prende la referenziazione
    public static DatabaseReference getDatabaseReference(){
        return rootNode;
    }

    public static void pushInfo(String path, String child, String value){
        rootNode= firebaseDatabase.getReference(path);
        rootNode.child(child).setValue(value);
    }

    public static void removeInfo(String path, String child){
        rootNode= firebaseDatabase.getReference(path);
        rootNode.child(child).removeValue();
    }
}
