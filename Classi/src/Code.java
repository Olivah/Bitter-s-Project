import java.util.Date;
import java.text.SimpleDateFormat;

//classe codice
//con questa classe creiamo un codice per ogni utente utilizzando la data e l'ora attuale
//usando un formato della data che arriva fino ai millisecondi creiamo un codice univoco
//usiamo poi la funzione removespecial per eliminare tutti i caratteri non necessari all'interno del codice
public class Code {

    //campi
    //date è la data e ora attuale, formatter è il formato della data che abbiamo impostato e arriva fino ai millisecondi
    //code è la stringa effettiva del codice dell'utente
    private Date date = new Date();
    private SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss:SSS");
    private String code = formatter.format(date);

    //costruttore
    public Code(){
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
}
