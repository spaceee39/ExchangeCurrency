package Exceptions;

public class CurrencyAlreadyExists extends RuntimeException {
    public CurrencyAlreadyExists(String message, Throwable cause) {
        super(message, cause);
    }
}
