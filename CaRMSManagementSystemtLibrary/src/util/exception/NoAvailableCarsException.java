package util.exception;

public class NoAvailableCarsException extends Exception {

    
    public NoAvailableCarsException() {
    }

    public NoAvailableCarsException(String msg) {
        super(msg);
    }
}
