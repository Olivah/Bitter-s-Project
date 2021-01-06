package com.example.bitter.Class;

import android.os.SystemClock;
import android.util.Log;

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

    private ArrayList<String> queueMall= new ArrayList<>();
    private ArrayList<String> queueShop= new ArrayList<>();
    private int size;
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

        Query userList= getInfo("Mall_List/Nave_de_Vero/Queue_User");
        userList.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    queueMall.clear();
                    size=0;
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        queueMall.add(size, snapshot.getValue().toString());
                        size++;
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
    public boolean enqueueMall(String mall, Thread myself) {
        // metto l'utente in coda
        pushInfo("Mall_List/"+mall+"/Queue_User/List",user.getCode().toString(), user.getCode().toString());

        SystemClock.sleep(1000);

        while(currcapMall >= maxcapMall && queueMall.get(0)!=user.getCode().toString()) {SystemClock.sleep(100);}

        // tolgo l'utente dalla coda
        removeInfo("Mall_List/"+mall+"/Queue_User/List/", user.getCode().toString());

        // se avevo interrotto il thread annullo l'inserimento nel centro commerciale
        if(myself.isInterrupted()){
            return false;
        }

        currcapMall++;
        pushInfo("Mall_List/Nave_de_Vero", "Currcap", ""+currcapMall);
        pushInfo("Mall_List/"+mall+"/Inside_User/List", user.getCode().toString(), user.getCode().toString());
        return true;
    }

    //elimina la prima persona dalla coda cioè quella in posizione 0 e la restituisce
    public boolean dequeueMall(String mall) {
        currcapMall--;
        pushInfo("Mall_List/"+mall+"/", "Currcap", ""+currcapMall);
        removeInfo("Mall_List/"+mall+"/Inside_User/List", user.getCode().toString());
        return true;
    }

    //mette un utente nella relativa coda
    public boolean enqueueShop(String mall, String shop, Thread myself) {
        // metto l'utente in coda
        pushInfo("Mall_List/"+mall+"/Shop_List/"+shop+"/Queue_User/List",user.getCode().toString(), user.getCode().toString());

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

        Query userList= getInfo("Mall_List/"+mall+"/Shop_List/"+shop+"/Queue_User/List");
        userList.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    queueShop.clear();
                    size=0;
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        queueShop.add(size, snapshot.getValue().toString());
                        size++;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        SystemClock.sleep(1000);

        while(currcapShop >= maxcapShop && queueShop.get(0)!=user.getCode().toString()){SystemClock.sleep(100);}

        // tolgo l'utente dalla coda
        removeInfo("Mall_List/"+mall+"/Shop_List/"+shop+"/Queue_User/List", user.getCode().toString());

        // se avevo interrotto il thread annullo l'inserimento nel centro commerciale
        if(myself.isInterrupted()){
            return false;
        }

        currcapShop++;
        pushInfo("Mall_List/"+mall+"/Shop_List/"+shop, "Currcap", ""+currcapShop);
        pushInfo("Mall_List/"+mall+"/Shop_List/"+shop+"/Inside_User/List", user.getCode().toString(), user.getCode().toString());

        return true;
    }

    //elimina la prima persona dalla coda cioè quella in posizione 0 e la restituisce
    public boolean dequeueShop(String mall, String shop) {
        currcapShop--;
        pushInfo("Mall_List/"+mall+"/Shop_List/"+shop, "Currcap", ""+currcapShop);
        removeInfo("Mall_List/"+mall+"/Shop_List/"+shop+"/Inside_User/List", user.getCode().toString());
        return true;
    }
}
