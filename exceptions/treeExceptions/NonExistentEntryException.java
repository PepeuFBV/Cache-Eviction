package exceptions.treeExceptions;

public class NonExistentEntryException extends Exception {
    public NonExistentEntryException(String message) {
        super(message);
    }
}
