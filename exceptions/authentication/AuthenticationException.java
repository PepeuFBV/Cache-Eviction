package exceptions.authentication;

public class AuthenticationException extends Exception {
    public AuthenticationException(String message) {
        super(message);
    }
}