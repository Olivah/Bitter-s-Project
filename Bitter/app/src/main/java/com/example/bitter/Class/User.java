package com.example.bitter.Class;

//classe utente
public class User {

    //campi
    //position è la posizione dell'utente
    //code è il suo codice
    private Code code;


    //costruttore
    public User(){
        this.code = new Code();
    }

    public Code getCode(){
        return code;
    }
}
