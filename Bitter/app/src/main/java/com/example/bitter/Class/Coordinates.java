package com.example.bitter.Class;

//Classe coordinate
public class Coordinates {

    //campi latitudine e longitudine
    private double lng;
    private double lat;

    public Coordinates(){
        this.lng=0;
        this.lat=0;
    }

    //costruttore
    public Coordinates(double lng, double lat){
        this.lng = lng;
        this.lat = lat;
    }

    //getter
    public double getLng() {
        return lng;
    }

    public double getLat() {
        return lat;
    }

    //setter
    public void setLng(double lng) {
        this.lng = lng;
    }

    public void setLat(double lat ){
        this.lat = lat;
    }
}
