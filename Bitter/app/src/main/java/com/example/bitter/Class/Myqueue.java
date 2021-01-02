package com.example.bitter.Class;

import java.util.ArrayList;

//Classe per gestire le code del centro commerciale e dei relativi negozi al suo interno
public class Myqueue{

    //campo people lista di persone all'interno del centro commerciale/negozio
    private ArrayList<User> queue;
    private int maxcap;
    private int currcap;

    //costruttore
    public Myqueue (int maxcap){
        this.currcap=0;
        this.maxcap=maxcap;
        this.queue = new ArrayList<>(this.maxcap);
    }

    //mette un utente nella relativa coda
    public synchronized void enqueue(User user){
        queue.add(user);
    }

    //elimina la prima persona dalla coda cioè quella in posizione 0 e la restituisce
    public synchronized User dequeue(){
        return queue.remove(0);
    }

    //elimina l'utente passato attraverso i parametro dalla coda
    public synchronized void dequeue(User user){
        queue.remove(user);
    }

    //metodo add: aggiunge la prima persona della coda nella lista del negozio/centro commerciale
    public synchronized void add(User user, ArrayList<User> people) throws InterruptedException {

        //mette l'utente in coda
        enqueue(user);

        //attende che la capacità corrente sia minore di quella massima
        while(currcap>=maxcap)
            wait();

        //toglie la prima persona dalla coda di attesa e la aggiunge al negozio aumntando il numero di persone di 1
        people.add(dequeue());
        currcap++;
    }

    //rimuove una persona dal negozio
    public synchronized void remove(User user, ArrayList<User> people){

        //rimuove l'utente uscito dalla struttura diminuendo il numero di persone all'interno di 1 e lo notifica alla coda
        people.remove(user);
        currcap--;
        notify();
    }

    public int getMaxcap(){
        return maxcap;
    }

    public int getCurrcap(){
        return currcap;
    }
}


