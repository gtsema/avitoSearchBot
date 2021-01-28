package exceptions;

public class PropertyException extends Exception {
    public PropertyException(String message) {
        super(message);
    }

    public  PropertyException(Throwable cause) {
        super(cause);
    }

    public  PropertyException(String message, Throwable cause) {
        super(message, cause);
    }
}
