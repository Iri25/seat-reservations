package repository.jdbc;

import domain.Show;
import domain.validators.SpectatorValidation;
import repository.ShowRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ShowJdbcRepository extends SpectatorValidation implements ShowRepository {

    private JdbcUtils jdbcUtils;
    private static final Logger logger= LogManager.getLogger();

    public ShowJdbcRepository(Properties properties) { this.jdbcUtils = new JdbcUtils(properties); }
    public ShowJdbcRepository() {}

    @Override
    public Show findOne(Integer id) {
        logger.traceEntry("Finding task with id {} ", id);
        Connection connection = jdbcUtils.getConnection();
        Show show = null;
        Savepoint savepoint = null;
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement preparedStatement = connection.prepareStatement
                    ("SELECT * FROM Shows WHERE id = ?")) {
                preparedStatement.setInt(1, id);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        id = resultSet.getInt("id");
                        String name = resultSet.getString("name");
                        LocalDateTime dateTime = LocalDateTime.parse(resultSet.getString("dateTime"));
                        Double price = resultSet.getDouble("price");

                        show = new Show(name, dateTime, price);
                        show.setId(id);

                        savepoint = connection.setSavepoint("findOne");
                        connection.commit();
                        return show;
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
        return show;
    }

    @Override
    public Iterable<Show> findAll() {
        logger.traceEntry();
        Connection connection = jdbcUtils.getConnection();
        List<Show> shows = new ArrayList<>();
        Savepoint savepoint = null;
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Shows")) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        int id = resultSet.getInt("id");
                        String name = resultSet.getString("name");
                        LocalDateTime dateTime = LocalDateTime.parse(resultSet.getString("dateTime"));
                        Double price = resultSet.getDouble("price");

                        Show show = new Show(name, dateTime, price);
                        show.setId(id);

                        savepoint = connection.setSavepoint("findAll");
                        connection.commit();

                        shows.add(show);
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

        logger.traceExit(shows);
        return shows;
    }

    @Override
    public void save(Show entity) {
        logger.traceEntry("Saving task {}", entity);
        Connection connection = jdbcUtils.getConnection();
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement preparedStatement = connection.prepareStatement
                ("INSERT INTO Shows VALUES (?, ?, ?, ?)")) {
                preparedStatement.setInt(1, entity.getId());
                preparedStatement.setString(2, entity.getName());
                preparedStatement.setString(3, String.valueOf(entity.getDateTime()));
                preparedStatement.setDouble(4, entity.getPrice());

                int result = preparedStatement.executeUpdate();

                if (result == 1) {
                    connection.commit();
                } else {
                    connection.rollback();
                }
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
    public void delete(Integer id) {
        logger.traceEntry("Deleting task with {}", id);
        Connection connection = jdbcUtils.getConnection();
        try {
            connection.setAutoCommit(false);
            try(PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Shows WHERE id = ?")) {
                preparedStatement.setInt(1, id);

                int result = preparedStatement.executeUpdate();

                if (result == 1) {
                    connection.commit();
                } else {
                    connection.rollback();
                }
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
    public void update(Show entity) {
        logger.traceEntry("Updating task {}", entity);
        Connection connection = jdbcUtils.getConnection();
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement preparedStatement = connection.prepareStatement
                    ("UPDATE Shows SET name = ?, dateTime = ?, price = ? WHERE ID = ?")) {
                preparedStatement.setString(1, entity.getName());
                preparedStatement.setString(2, String.valueOf(entity.getDateTime()));
                preparedStatement.setDouble(3, entity.getPrice());
                preparedStatement.setInt(4, entity.getId());

                int result = preparedStatement.executeUpdate();

                if (result == 1) {
                    connection.commit();
                } else {
                    connection.rollback();
                }

                logger.trace("Updated {} instances", result);
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
}
