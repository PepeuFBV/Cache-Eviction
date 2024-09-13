package exceptions;

public class NonExistentEntryException extends Exception {
    public NonExistentEntryException(String message) {
        super(message);
    }
}
