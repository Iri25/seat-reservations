package domain;

import domain.validators.SpectatorValidation;

/**
 * 
 */
public class Spectator extends Login {

    /**
     * Default constructor
     */
    public Spectator() {
    }

    /**
     * 
     */
    private String firstName;

    /**
     * 
     */
    private String lastName;

    /**
     * 
     */
    private String email;

    /**
     * @param firstName
     * @param lastName
     * @param email
     */
    public Spectator(String username, String password, UserStatus status, String firstName, String lastName, String email) {
        super(username, password, status);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    /**
     * @return
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @return
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @return
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @param lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return
     */
    @Override
    public String toString() {
        return "Spectator{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}