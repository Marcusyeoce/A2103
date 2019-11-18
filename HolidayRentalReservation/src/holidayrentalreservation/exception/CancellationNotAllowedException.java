package holidayrentalreservation.exception;

public class CancellationNotAllowedException extends Exception {

    public CancellationNotAllowedException() {
    }

    public CancellationNotAllowedException(String msg) {
        super(msg);
    }
}
