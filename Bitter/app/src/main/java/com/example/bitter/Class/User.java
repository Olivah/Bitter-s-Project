package com.example.bitter.Class;

//classe utente
public class User {

    //campi
    //position è la posizione dell'utente
    //code è il suo codice
    private Coordinates position;
    private Code code;


    //costruttore
    public User(Coordinates position){
        this.code = new Code();
        this.position = position;
    }

    //metodo che inserisce l'utente nel centro commerciale richiamando il metodo di Mall
    public void mallenter(Mall mall) throws InterruptedException {
        mall.mallenqueue(this);
    }

    //metodo che rimuove l'utente nel centro commerciale richiamando il metodo di Mall
    public void mallexit(Mall mall){
        mall.malldequeue(this);
    }

    //metodo che inserisce l'utente nel negozio richiamando il metodo di Shop
    public void shopenter(Shop shop) throws InterruptedException {
        shop.shopenqueue(this);
    }

    //metodo che inserisce l'utente nel negozio richiamando il metodo di Shop
    public void shopexit(Shop shop){
        shop.shopdequeue(this);
    }


    //metodo per creare una segnalazione passandogli il relativo testo e le coordinate attuali nell'utente
    public Signal signalization (Coordinates cord, String text){
        Signal sign = new Signal(cord, text);
        return sign;
    }

    //getter
    public Coordinates getPosition() {
        return position;
    }
}
