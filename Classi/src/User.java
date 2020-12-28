public class User {

    private Coordinates position;
    private Code code;


    public User(Code code, Coordinates position){
        this.code = code;
        this.position = position;
    }

    public void mallenter(Mall x) throws InterruptedException {
        x.mallenqueue(this);
    }

    public void mallexit(Mall x){
        x.malldequeue(this);
    }
    public void shopenter(Shop x) throws InterruptedException {
        x.shopenqueue(this);
    }

    public void shopexit(Shop x){
        x.shopdequeue(this);
    }


    public Signal signalization (Coordinates cord, String text){
        Signal sign = new Signal(cord, text);
        return sign;
    }

    public Coordinates getPosition() {
        return position;
    }
}
