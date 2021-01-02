package com.example.bitter.Class;

import java.util.ArrayList;

//classe negozio
public class Shop {

    //campi:
    //position array di coordinate che delimita l'area del negozio
    //people lista di persone all'interno del negozio
    //shopqueue coda del negozio
    //maxcap e currcap sono riferite alla capiacità massima e corrente di persone nel locale
    private ArrayList<Coordinates> position;
    private ArrayList<User> people;
    private Myqueue shopqueue;

    //costruttore
    public Shop(ArrayList<Coordinates> position, int maxcap) {
        this.position = position;
        this.people = new ArrayList<>();
        this.shopqueue = new Myqueue(maxcap);
    }


    //aggiunge un utente alla coda richiamando il metodo di Myqeueue
    public synchronized void shopenqueue(User user) throws InterruptedException{
        shopqueue.add(user, people);
    }

    //rimuove un utente dalla coda richiamando il metodo di Myqeueue
    public void shopdequeue(User user) {
        shopqueue.remove(user,people);
    }


    //getter
    public ArrayList<Coordinates> getPosition() {
        return position;
    }

    public Myqueue getShopqueue() {
        return shopqueue;
    }

    //setter
    public void setPosition(ArrayList<Coordinates> position) {
        this.position = position;
    }
}
