package Exceptions;

public class CurrencySaveException extends RuntimeException {
    public CurrencySaveException(String message, Throwable cause) {
        super(message, cause);
    }
}
