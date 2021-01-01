package com.example.bitter.Class;

import java.util.ArrayList;

//classe centro commerciale
public class Mall {
    //campi
    //position array di coordinate che delimita l'area del centro commerciale
    //shops lista di negozio all'interno del centri commerciale
    //people lista di persone all'interno del negozio
    //mallqueue coda del negozio
    //maxcap e currcap sono riferite alla capiacità massima e corrente di persone nel centro commerciale

    private ArrayList<Coordinates> position;
    private ArrayList<Shop> shops;
    private ArrayList<User> people;
    private Myqueue mallqueue;

    //costruttore
    public Mall(ArrayList<Coordinates> position, ArrayList<Shop> shops, int maxcap){
        this.people= new ArrayList<>();
        this.position = position;
        this.shops = shops;
        this.mallqueue = new Myqueue(maxcap);
    }

    public ArrayList<Shop> getShops(){
        return shops;
    }

    public ArrayList<User> getPeople(){
        return people;
    }

    //aggiunge un utente alla coda richiamando il metodo di Myqeueue
    public void mallenqueue(User user) throws InterruptedException {
        mallqueue.add(user, people);
    }

    //rimuove un utente dalla coda richiamando il metodo di Myqeueue
    public void malldequeue(User user){
        mallqueue.remove(user,people);
    }




}
