package repository.jdbc;

import domain.Login;
import domain.UserStatus;
import domain.validators.LoginValidation;
import repository.LoginRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class LoginJdbcRepository extends LoginValidation implements LoginRepository {

    private JdbcUtils jdbcUtils;
    private static final Logger logger = LogManager.getLogger();

    public LoginJdbcRepository(Properties properties) { this.jdbcUtils = new JdbcUtils(properties); }
    public LoginJdbcRepository() {
    }

    @Override
    public Login findEntity(String username, String password, UserStatus status) {
        logger.traceEntry("Finding task with username, password and status {}, {}, {} ",
                username, password, status);
        Connection connection = jdbcUtils.getConnection();
        Savepoint savepoint = null;
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement preparedStatement = connection.prepareStatement
                    ("SELECT * FROM Users WHERE username = ? and password = ? and status = ?")) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                preparedStatement.setString(3, String.valueOf(status));
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        username = resultSet.getString("username");
                        password = resultSet.getString("password");
                        status = UserStatus.valueOf(resultSet.getString("status"));
                        Integer id = resultSet.getInt("id");

                        Login user = new Login(username, password, status);
                        user.setId(id);

                        savepoint = connection.setSavepoint("findEntity");
                        connection.commit();

                        return user;
                    }
                }
            }
        } catch (SQLException sqlException) {
            logger.error(sqlException);

            try {
                if (savepoint != null) {
                    connection.rollback(savepoint);
                } else {
                    connection.rollback();
                }
            } catch (SQLException rollbackException) {
                logger.error("Rollback failed: " + rollbackException.getMessage());
                rollbackException.printStackTrace();
            }

            sqlException.printStackTrace();
        } finally {
            try {
                connection.setAutoCommit(true);
                connection.close();
            } catch (SQLException closeException) {
                logger.error("Failed to close connection: " + closeException.getMessage());
                closeException.printStackTrace();
            }
        }

        logger.traceExit("No task found with username {}, ", username);
        return null;
    }

    @Override
    public Login findOne(Integer id) {
        logger.traceEntry("Finding task with id {} ", id);
        Connection connection = jdbcUtils.getConnection();
        Login user = null;
        Savepoint savepoint = null;
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement preparedStatement = connection.prepareStatement
                    ("SELECT * FROM Users WHERE id = ?")) {
                preparedStatement.setInt(1, id);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        id = resultSet.getInt("id");
                        String username = resultSet.getString("username");
                        String password = resultSet.getString("password");
                        UserStatus status = UserStatus.valueOf(resultSet.getString("status"));

                        user = new Login(username, password, status);
                        user.setId(id);

                        savepoint = connection.setSavepoint("findOne");
                        connection.commit();

                        return user;
                    }
                }
            }
        } catch (SQLException sqlException) {
            logger.error(sqlException);

            try {
                if (savepoint != null) {
                    connection.rollback(savepoint);
                } else {
                    connection.rollback();
                }
            } catch (SQLException rollbackException) {
                logger.error("Rollback failed: " + rollbackException.getMessage());
                rollbackException.printStackTrace();
            }

            sqlException.printStackTrace();
        } finally {
            try {
                connection.setAutoCommit(true);
                connection.close();
            } catch (SQLException closeException) {
                logger.error("Failed to close connection: " + closeException.getMessage());
                closeException.printStackTrace();
            }
        }

        logger.traceExit("No task found with id {}", id);
        return user;
    }

    @Override
    public Iterable<Login> findAll() {
        logger.traceEntry();
        Connection connection = jdbcUtils.getConnection();
        List<Login> users = new ArrayList<>();
        Savepoint savepoint = null;
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Users")) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        int id = resultSet.getInt("id");
                        String username = resultSet.getString("username");
                        String password = resultSet.getString("password");
                        UserStatus status = UserStatus.valueOf(resultSet.getString("status"));

                        Login user = new Login(username, password, status);
                        user.setId(id);

                        savepoint = connection.setSavepoint("findAll");
                        connection.commit();

                        users.add(user);
                    }
                }
            }
        } catch (SQLException sqlException) {
            logger.error(sqlException);

            try {
                if (savepoint != null) {
                    connection.rollback(savepoint);
                } else {
                    connection.rollback();
                }
            } catch (SQLException rollbackException) {
                logger.error("Rollback failed: " + rollbackException.getMessage());
                rollbackException.printStackTrace();
            }

            sqlException.printStackTrace();
        } finally {
            try {
                connection.setAutoCommit(true);
                connection.close();
            } catch (SQLException closeException) {
                logger.error("Failed to close connection: " + closeException.getMessage());
                closeException.printStackTrace();
            }
        }

        logger.traceExit(users);
        return users;
    }

    @Override
    public void save(Login entity) {
        logger.traceEntry("Saving task {}", entity);
        Connection connection = jdbcUtils.getConnection();
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement preparedStatement = connection.prepareStatement
                    ("INSERT INTO Users VALUES (?, ?, ?, ?)")) {
                preparedStatement.setInt(1, entity.getId());
                preparedStatement.setString(2, entity.getUsername());
                preparedStatement.setString(3, entity.getPassword());
                preparedStatement.setString(4, String.valueOf(entity.getStatus()));
                int result = preparedStatement.executeUpdate();

                if (result == 1) {
                    connection.commit();
                } else {
                    connection.rollback();
                }

                logger.trace("Saved {} instances", result);
            }
        } catch (SQLException sqlException) {
            logger.error(sqlException);
            sqlException.printStackTrace();

            try {
                connection.rollback();
            } catch (SQLException rollbackException) {
                logger.error("Rollback failed: " + rollbackException.getMessage());
                rollbackException.printStackTrace();
            }
        } finally {
            try {
                connection.setAutoCommit(true);
                connection.close();
            } catch (SQLException closeException) {
                logger.error("Failed to close connection: " + closeException.getMessage());
                closeException.printStackTrace();
            }
        }
        logger.traceExit();
    }

    @Override
    public void delete(Integer integer) {

    }

    @Override
    public void update(Login entity) {

    }
}












    /*
    @Override
    public void delete(Integer id) {
        logger.traceEntry("Deleting task with {}", id);
        Connection connection = jdbcUtils.getConnection();

        try(PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Users WHERE id = ?")) {
            preparedStatement.setInt(1, id);
            int result = preparedStatement.executeUpdate();
        }
        catch (SQLException sqlException){
            logger.error(sqlException);
            sqlException.printStackTrace();
        }
        logger.traceExit();
    }

    @Override
    public void update(Login entity) {
        logger.traceEntry("Updating task {}", entity);
        Connection connection = jdbcUtils.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement
                ("UPDATE Users SET username = ?, password = ?, status = ?  WHERE ID = ?")) {
            preparedStatement.setString(1, entity.getUsername());
            preparedStatement.setString(2, entity.getPassword());
            preparedStatement.setString(3, String.valueOf(entity.getStatus()));
            preparedStatement.setInt(4, entity.getId());
            int result = preparedStatement.executeUpdate();
            logger.trace("Updated {} instances", result);
        }
        catch (SQLException sqlException) {
            logger.error(sqlException);
            sqlException.printStackTrace();
        }
        logger.traceExit();
    }
*/