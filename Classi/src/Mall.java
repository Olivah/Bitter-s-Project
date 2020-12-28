import java.util.ArrayList;

//classe centro commerciale
public class Mall {
    //campi
    //position array di coordinate che delimita l'area del centro commerciale
    //shops lista di negozio all'interno del centri commerciale
    //people lista di persone all'interno del negozio
    //mallqueue coda del negozio
    //maxcap e currcap sono riferite alla capiacità massima e corrente di persone nel centro commerciale

    private ArrayList<Coordinates> position = new ArrayList<>();
    private ArrayList<Shop> shops = new ArrayList<>();
    private ArrayList<User> people =new ArrayList<>();
    private Myqueue mallqueue;
    private final int  mallmaxcap = 10000;
    private int mallcurrcap;

    //cstruttore
    public Mall(ArrayList<Coordinates> position,ArrayList<Shop> shops, Myqueue mallqueue, ArrayList<User> people, int mallcurrcap){
        this.position = position;
        this.shops = shops;
        this.mallqueue = mallqueue;
        this.mallcurrcap = mallcurrcap;
    }

    //aggiunge un utente alla coda richiamando il metodo di Myqeueue
    public void mallenqueue(User user) throws InterruptedException {
        mallqueue.add(user, people, mallmaxcap, mallcurrcap);
    }

    //rimuove un utente dalla coda richiamando il metodo di Myqeueue
    public void malldequeue(User user){
        mallqueue.remove(user,people,mallcurrcap);
    }




}
