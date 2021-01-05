package com.example.bitter.Class;

import android.os.SystemClock;

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

    private ArrayList<String> shops= new ArrayList<>();
    private int maxcapMall;
    private int currcapMall;
    private int maxcapShop;
    private int currcapShop;

    public Information(){

        user= new User();

        Query current= getInfo("Mall_List/Nave_de_Vero/Currcap");
        current.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    currcapMall=parseInt(dataSnapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Query max= getInfo("Mall_List/Nave_de_Vero/Maxcap");
        max.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    maxcapMall=parseInt(dataSnapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Query shopList= getInfo("Mall_List/Nave_de_Vero/Shop_List_Name");
        shopList.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    shops.clear();
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        shops.add(snapshot.getValue().toString());
                    }
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
    public boolean enqueueMall(String mall) {
        while(currcapMall >= maxcapMall) {SystemClock.sleep(100);}

        pushInfo("Mall_List/"+mall+"/Inside_User/List", user.getCode().toString(), user.getCode().toString());
        currcapMall++;
        pushInfo("Mall_List/Nave_de_Vero", "Currcap", ""+currcapMall);
        return true;
    }

    //elimina la prima persona dalla coda cioè quella in posizione 0 e la restituisce
    public boolean dequeueMall(String mall) {
        removeInfo("Mall_List/"+mall+"/Inside_User/List", user.getCode().toString());
        currcapMall--;
        pushInfo("Mall_List/"+mall+"/", "Currcap", ""+currcapMall);
        return true;
    }

    //mette un utente nella relativa coda
    public boolean enqueueShop(String mall, String shop) {
        Query current= getInfo("Mall_List/"+mall+"/Shop_List/"+shop+"/Currcap");
        current.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    currcapShop=parseInt(dataSnapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Query max= getInfo("Mall_List/"+mall+"/Shop_List/"+shop+"/Maxcap");
        max.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    maxcapShop=parseInt(dataSnapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        while(currcapShop >= maxcapShop) {SystemClock.sleep(100);}

        setUser(mall, shop);

        return true;
    }

    public void setUser(String mall, String shop){
        pushInfo("Mall_List/"+mall+"/Shop_List/"+shop+"/Inside_User/List", user.getCode().toString(), user.getCode().toString());
        currcapShop++;
        pushInfo("Mall_List/"+mall+"/Shop_List/"+shop, "Currcap", ""+currcapShop);
    }

    //elimina la prima persona dalla coda cioè quella in posizione 0 e la restituisce
    public boolean dequeueShop(String mall, String shop) {
        removeInfo("Mall_List/"+mall+"/Shop_List/"+shop+"/Inside_User/List", user.getCode().toString());
        currcapShop--;
        pushInfo("Mall_List/"+mall+"/Shop_List/"+shop, "Currcap", ""+currcapShop);
        return true;
    }
}
