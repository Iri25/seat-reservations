package domain.validators;

/**
 * 
 */
public interface Validator<T> {

    /**
     * @param entity
     */
    public void validate(T entity) throws ValidationException;

}