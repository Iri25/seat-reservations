package repository;

import domain.Login;
import domain.UserStatus;

public interface LoginRepository extends Repository<Integer, Login> {

    /**
     *
     * @param username
     * @param password
     * @param status
     * @return
     */
     Login findEntity(String username, String password, UserStatus status);

}

