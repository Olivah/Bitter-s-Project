//Classe segnalazione
public class Signal {

    //campi coordinate e testo da unire alla segnalazione
    private Coordinates cord;
    private String text;

    //costruttore
    public Signal (Coordinates cord, String text){
        this.cord = cord;
        this.text = text;

    }

    //getter
    public Coordinates getCord() {              //da togliere?
        return cord;
    }

    public String getText() {
        return text;
    }


    //setter
    public void setCord(Coordinates cord) {
        this.cord = cord;
    }

    public void setText(String text) {
        this.text = text;
    }
}
