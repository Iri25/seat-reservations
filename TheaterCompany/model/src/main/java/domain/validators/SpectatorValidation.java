package domain.validators;

import domain.Spectator;

import java.util.regex.Pattern;

/**
 * 
 */
public class SpectatorValidation implements Validator<Spectator> {

    /**
     * Default constructor
     */
    public SpectatorValidation() {
    }


    /**
     * @param entity
     */
    public void validate(Spectator entity) throws  ValidationException{
        Integer id = entity.getId();
        String username = entity.getUsername();
        String password = entity.getPassword();
        String firstName = entity.getFirstName();
        String lastName = entity.getLastName();
        String email = entity.getEmail();

        String errors = "";

        if(id < 0)
            errors += "Id not valid! ";

        if (username.equals(""))
            errors += "Username field is empty! ";
        if (username.length() < 4)
            errors += "Username should have at least four characters! ";

        if (password.equals(""))
            errors += "Password not valid! ";
        if (password.length() < 4)
            errors += "Password should have at least four characters! ";

        if (firstName.equals(""))
            errors += "First name not valid! ";
        if (firstName.length() < 3)
            errors += "First name should have at least three characters! ";
        char[] charsFirstName = firstName.toCharArray();
        for(char characterFirstName : charsFirstName){
            if(Character.isDigit(characterFirstName)){
                errors += "First name must not contain digits! ";
                break;
            }
        }

        if (lastName.equals(""))
            errors += "Last name field is empty! ";
        if (lastName.length() < 3)
            errors += "Last name should have at least three characters! ";
        char[] charsLastName = lastName.toCharArray();
        for(char characterLastName : charsLastName){
            if(Character.isDigit(characterLastName)){
                errors += "Last name must not contain digits! ";
                break;
            }
        }

        if (email.equals(""))
            errors += "E-mail field is empty! ";
        Pattern pattern1 = Pattern.compile("[a-z]$");
        Pattern pattern2 = Pattern.compile("[A-Z]$");
        Pattern pattern3 = Pattern.compile("[0-9]$");
        if (!(email.contains("@") || email.contains("_") || email.contains(".") || email.contains(pattern1.pattern())
                || email.contains(pattern2.pattern()) || email.contains(pattern3.pattern())))
            errors += "E-mail not valid! ";

        if(errors.length() > 0)
            throw new ValidationException(errors);
    }

}