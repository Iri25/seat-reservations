package repository.jdbc;

import domain.Spectator;
import domain.UserStatus;
import domain.validators.SpectatorValidation;
import repository.SpectatorRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class SpectatorJdbcRepository extends SpectatorValidation implements SpectatorRepository {

    private JdbcUtils jdbcUtils;
    private static final Logger logger = LogManager.getLogger();


    public SpectatorJdbcRepository(Properties properties) {
        this.jdbcUtils = new JdbcUtils(properties);
    }

    public SpectatorJdbcRepository() {
    }


    @Override
    public Spectator findOne(Integer id) {
        logger.traceEntry("Finding task with id {} ", id);
        Connection connection = jdbcUtils.getConnection();
        Spectator spectator = null;
        Savepoint savepoint = null;
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement preparedStatement = connection.prepareStatement
                    ("SELECT * FROM Spectators WHERE id = ?")) {
                preparedStatement.setInt(1, id);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        id = resultSet.getInt("id");
                        String username = resultSet.getString("username");
                        String password = resultSet.getString("password");
                        UserStatus status = UserStatus.valueOf(resultSet.getString("status"));
                        String firstName = resultSet.getString("firstName");
                        String lastName = resultSet.getString("lastName");
                        String email = resultSet.getString("email");

                        spectator = new Spectator(username, password, status, firstName, lastName, email);
                        spectator.setId(id);

                        savepoint = connection.setSavepoint("findOne");
                        connection.commit();

                        return spectator;
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
        return spectator;
    }

    public Spectator findOne(String username) {
        logger.traceEntry("Finding task with username {} ", username);
        Connection connection = jdbcUtils.getConnection();
        Spectator spectator = null;
        Savepoint savepoint = null;
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement preparedStatement = connection.prepareStatement
                    ("SELECT * FROM Spectators WHERE username = ?")) {
                preparedStatement.setString(1, username);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        Integer id = resultSet.getInt("id");
                        username = resultSet.getString("username");
                        String password = resultSet.getString("password");
                        UserStatus status = UserStatus.valueOf(resultSet.getString("status"));
                        String firstName = resultSet.getString("firstName");
                        String lastName = resultSet.getString("lastName");
                        String email = resultSet.getString("email");

                        spectator = new Spectator(username, password, status, firstName, lastName, email);
                        spectator.setId(id);

                        savepoint = connection.setSavepoint("findOne");
                        connection.commit();

                        return spectator;
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

        logger.traceExit("No task found with username {}", username);
        return spectator;
    }

    @Override
    public Iterable<Spectator> findAll() {
        logger.traceEntry();
        Connection connection = jdbcUtils.getConnection();
        List<Spectator> spectators = new ArrayList<>();
        Savepoint savepoint = null;
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Spectators")) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        int id = resultSet.getInt("id");
                        String username = resultSet.getString("username");
                        String password = resultSet.getString("password");
                        UserStatus status = UserStatus.valueOf(resultSet.getString("status"));
                        String firstName = resultSet.getString("firstName");
                        String lastName = resultSet.getString("lastName");
                        String email = resultSet.getString("email");

                        Spectator spectator = new Spectator(username, password, status, firstName, lastName, email);
                        spectator.setId(id);

                        savepoint = connection.setSavepoint("findAll");
                        connection.commit();

                        spectators.add(spectator);
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

        logger.traceExit(spectators);
        return spectators;
    }

    @Override
    public void save(Spectator entity) {
        logger.traceEntry("Saving task {}", entity);
        Connection connection = jdbcUtils.getConnection();
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement preparedStatement = connection.prepareStatement
                    ("INSERT INTO Spectators VALUES (?, ?, ?, ?, ?, ?, ?)")) {
                preparedStatement.setInt(1, entity.getId());
                preparedStatement.setString(2, entity.getUsername());
                preparedStatement.setString(3, entity.getPassword());
                preparedStatement.setString(4, String.valueOf(entity.getStatus()));
                preparedStatement.setString(5, entity.getFirstName());
                preparedStatement.setString(6, entity.getLastName());
                preparedStatement.setString(7, entity.getEmail());

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
    public void update(Spectator entity) {

    }

}











/*
    @Override
    public void delete(Integer id) {
        logger.traceEntry("Deleting task with {}", id);
        Connection connection = jdbcUtils.getConnection();

        try(PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Spectators WHERE id = ?")) {
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
    public void update(Spectator entity) {
        logger.traceEntry("Updating task {}", entity);
        Connection connection = jdbcUtils.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement
                ("UPDATE Spectators SET username = ?, password = ?, status = ?, firstName = ?, lastName = ?, email = ?  WHERE ID = ?")) {
            preparedStatement.setString(1, entity.getUsername());
            preparedStatement.setString(2, entity.getPassword());
            preparedStatement.setString(3, String.valueOf(entity.getStatus()));
            preparedStatement.setString(4, entity.getFirstName());
            preparedStatement.setString(5, entity.getLastName());
            preparedStatement.setString(6, entity.getEmail());
            preparedStatement.setInt(7, entity.getId());

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
