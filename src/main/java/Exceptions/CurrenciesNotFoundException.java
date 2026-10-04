package Exceptions;

public class CurrenciesNotFoundException extends RuntimeException {
    public CurrenciesNotFoundException(String message) {
        super(message);
    }
}
