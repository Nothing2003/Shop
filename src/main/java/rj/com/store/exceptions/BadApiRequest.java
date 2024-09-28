package rj.com.store.exceptions;

public class BadApiRequest extends RuntimeException{
    public BadApiRequest(){
        super("Bad  Request");
    }
    public BadApiRequest(String massage){
        super(massage);
    }
}
