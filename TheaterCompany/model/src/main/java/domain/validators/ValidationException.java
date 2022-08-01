package domain.validators;

/**
 * 
 */
public class ValidationException extends RuntimeException{

    /**
     * Default constructor
     */
    public ValidationException() {
    }

    /**
     * @param message
     */
    public ValidationException(String message) {
        super(message);
    }

}