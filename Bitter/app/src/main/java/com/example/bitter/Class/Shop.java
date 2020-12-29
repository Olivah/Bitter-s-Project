package com.example.bitter.Class;

import java.util.ArrayList;

//classe negozio
public class Shop {

    //campi:
    //position array di coordinate che delimita l'area del negozio
    //people lista di persone all'interno del negozio
    //shopqueue coda del negozio
    //maxcap e currcap sono riferite alla capiacità massima e corrente di persone nel locale
    private ArrayList<Coordinates> position = new ArrayList<>();
    private ArrayList<User> people =new ArrayList<>();
    private Myqueue shopqueue;
    private int maxcap;
    private int currcap = 0;

    //costruttore
    public Shop(ArrayList<Coordinates> position, Myqueue shopqueue, ArrayList<User> people, int maxcap, int currcap) {
        this.shopqueue = shopqueue;
        this.position = position;
        this.people = people;
        this.currcap = currcap;
        this.maxcap = maxcap;
    }


    //aggiunge un utente alla coda richiamando il metodo di Myqeueue
    public synchronized void shopenqueue(User user) throws InterruptedException{
        shopqueue.add(user, people, maxcap, currcap);
    }

    //rimuove un utente dalla coda richiamando il metodo di Myqeueue
    public void shopdequeue(User user) {
        shopqueue.remove(user,people, currcap);
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

    public void setShopqueue(Myqueue shopqueue) {
        this.shopqueue = shopqueue;
    }
}
