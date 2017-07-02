package hu.kits.userservice.domain;

@SuppressWarnings("serial")
public class AuthenticationException extends Exception {

    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException() {
        super("");
    }
    
}
