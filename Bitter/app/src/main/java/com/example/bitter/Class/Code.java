package com.example.bitter.Class;

import java.text.SimpleDateFormat;
import java.util.Date;

//classe codice
//con questa classe creiamo un codice per ogni utente utilizzando la data e l'ora attuale
//usando un formato della data che arriva fino ai millisecondi creiamo un codice univoco
//usiamo poi la funzione removespecial per eliminare tutti i caratteri non necessari all'interno del codice
public class Code {

    //campi
    //date è la data e ora attuale, formatter è il formato della data che abbiamo impostato e arriva fino ai millisecondi
    //code è la stringa effettiva del codice dell'utente
    private Date date;
    private SimpleDateFormat formatter;
    private String code;

    //costruttore
    public Code(){
        this.date = new Date();
        this.formatter = new SimpleDateFormat("HH:mm:ss:SSS dd-MM-yyyy ");
        this.code = formatter.format(date);
        this.code = removespecial(code);
    }

    //metodi
    //elimina i caratteri speciali non necessari
    public String removespecial(String data){

        data = data.replace(":","");
        data = data.replace("-","");
        data = data.replace(" ","");
        data = data.replace(".","");

        return data;
    }

    @Override
    public String toString(){
        return code;
    }
}
