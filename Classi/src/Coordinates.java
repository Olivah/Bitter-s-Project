//Classe coordinate
public class Coordinates {

    //campi latitudine e longitudine
    private float Long;
    private float Lat;

    //costruttore
    public Coordinates(float Long, float Lat){
        this.Long = Long;
        this.Lat = Lat;

    }

    //getter
    public float getLong() {
        return Long;
    }

    public float getLat() {
        return Lat;
    }

    //setter
    public void setLong(int Long) {
        this.Long = Long;
    }

    public void setLat(int Lat ){
        this.Lat = Lat;
    }


}
