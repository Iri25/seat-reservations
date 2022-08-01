package domain;

/**
 * 
 */
public class Admin extends Login {

    /**
     * Default constructor
     */
    public Admin(String username, String password, UserStatus status) {
        super(username, password, status);
    }

    /**
     * @return
     */
    @Override
    public String toString() {
        return super.toString();
    }
}