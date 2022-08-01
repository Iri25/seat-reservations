package domain.validators;

import domain.Admin;

/**
 * 
 */
public class AdminValidation implements Validator<Admin> {

    /**
     * Default constructor
     */
    public AdminValidation() {
    }

    /**
     * @param entity
     */
    public void validate(Admin entity) throws ValidationException {
        Integer id = entity.getId();
        String username = entity.getUsername();
        String password = entity.getPassword();

        String errors = "";

        if(id < 0)
            errors += "Id not valid! ";

        if (username.equals(""))
            errors += "Username field is empty! ";
        if (username.length() < 4)
            errors += "Username should have at least four characters! ";

        if (password.equals(""))
            errors += "Password field is empty! ";
        if (password.length() < 4)
            errors += "Password should have at least four characters! ";

        if(errors.length() > 0)
            throw new ValidationException(errors);
    }

}