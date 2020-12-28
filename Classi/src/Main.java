import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {

    public static String cavatuttodiocan(String data){

        data = data.replace(":","");
        data = data.replace("-","");
        data = data.replace(" ","");
        data = data.replace(".","");

        return data;
    }


    public static void main(String[] args ){

        Date date = new Date();

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
        String ciaone = formatter.format(date);


        ciaone = cavatuttodiocan(ciaone);
        System.out.println(ciaone);
    }
}
