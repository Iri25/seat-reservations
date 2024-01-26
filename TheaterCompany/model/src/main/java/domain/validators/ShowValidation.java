package domain.validators;

import domain.Show;

import java.time.LocalDate;
import java.time.LocalTime;

public class ShowValidation implements Validator<Show> {

    /**
     * Default constructor
     */
    public ShowValidation() {

    }
    /**
     * @param entity
     */
    public void validate(Show entity) throws ValidationException {
        Integer id = entity.getId();
        String name = entity.getName();
        // LocalDate date = entity.getDate();
        LocalTime time = entity.getTime();
        Double price = entity.getPrice();

        String errors = "";

        if(id < 0)
            errors += "Id not valid! ";

        if (name.isEmpty())
            errors += "Name not valid! ";
        if (name.length() < 3)
            errors += "Name should have at least three characters! ";
        char[] charsFirstName = name.toCharArray();
        for(char characterFirstName : charsFirstName){
            if(Character.isDigit(characterFirstName)){
                errors += "Name must not contain digits! ";
                break;
            }
        }

        if (time.toString().isEmpty())
            errors += "Hour not valid! ";

        if (price < 0)
            errors += "Price not valid! ";
    }
}
