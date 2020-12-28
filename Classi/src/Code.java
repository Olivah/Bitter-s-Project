import java.util.Date;
import java.text.SimpleDateFormat;

public class Code {

    private Date date = new Date();
    private SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    private String code = formatter.format(date);

    public Code(String code){
        this.code = removespecial(code);
    }

    public String removespecial(String data){

        data = data.replace(":","");
        data = data.replace("-","");
        data = data.replace(" ","");

        return data;
    }
}
