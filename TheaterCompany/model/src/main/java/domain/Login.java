package domain;

/**
 * 
 */
public class Login extends Entity<Integer> {

    /**
     * Default constructor
     */
    public Login() {
        super();
    }

    /**
     * 
     */
    private String username;

    /**
     * 
     */
    private String password;

    /**
     * 
     */
    private UserStatus status;

    /**
     * @param username
     * @param password
     * @param status
     */
    public Login(String username, String password, UserStatus status) {
        this.username = username;
        this.password = password;
        this.status = status;
    }

    /**
     * @return
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return
     */
    public String getPassword() {
        return password;
    }

    /**
     * @return
     */
    public UserStatus getStatus() {
        return status;
    }

    /**
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @param status
     */
    public void setStatus(UserStatus status) {
        this.status = status;
    }

    /**
     * @return
     */
    @Override
    public String toString() {
        return "Login{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", status=" + status +
                '}';
    }
}