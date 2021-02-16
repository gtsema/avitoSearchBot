package exceptions;

public class DialogHandlerException extends Exception {
        public DialogHandlerException(String message) {
            super(message);
        }

        public  DialogHandlerException(Throwable cause) {
            super(cause);
        }

        public  DialogHandlerException(String message, Throwable cause) {
            super(message, cause);
        }
}
