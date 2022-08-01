package repository.jdbc;

import domain.Show;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import repository.ShowRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ShowJdbcRepository implements ShowRepository {

    /**
     *
     */
    private JdbcUtils jdbcUtils;

    /**
     *
     */
    private static final Logger logger= LogManager.getLogger();

    /**
     *
     * @param properties
     */
    public ShowJdbcRepository(Properties properties) {

        this.jdbcUtils = new JdbcUtils(properties);
    }

    /**
     * Default constructor
     */
    public ShowJdbcRepository() {}

    /**
     *
     * @param id
     * @return
     */
    @Override
    public Show findOne(Integer id) {
        logger.traceEntry("Finding task with id {} ", id);
        Connection connection = jdbcUtils.getConnection();
        Show show = null;
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

                    return show;
                }
            }
        } catch (SQLException sqlException) {
            logger.error(sqlException);
            sqlException.printStackTrace();
        }
        logger.traceExit("No task found with id {}", id);
        return show;
    }

    /**
     *
     * @return
     */
    @Override
    public Iterable<Show> findAll() {
        logger.traceEntry();
        Connection connection = jdbcUtils.getConnection();

        List<Show> shows = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Shows")) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    LocalDateTime dateTime = LocalDateTime.parse(resultSet.getString("dateTime"));
                    Double price = resultSet.getDouble("price");

                    Show show = new Show(name, dateTime, price);
                    show.setId(id);

                    shows.add(show);
                }
            }
        }
        catch (SQLException sqlException) {
            logger.error(sqlException);
            sqlException.printStackTrace();
        }
        logger.traceExit(shows);
        return shows;
    }

    /**
     *
     * @param entity
     *         entity must be not null
     */
    @Override
    public void save(Show entity) {
        logger.traceEntry("Saving task {}", entity);
        Connection connection = jdbcUtils.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement
                ("INSERT INTO Shows VALUES (?, ?, ?, ?)")) {
            preparedStatement.setInt(1, entity.getId());
            preparedStatement.setString(2, entity.getName());
            preparedStatement.setString(3, String.valueOf(entity.getDateTime()));
            preparedStatement.setDouble(4, entity.getPrice());

            int result = preparedStatement.executeUpdate();
            logger.trace("Saved {} instances", result);
        }
        catch (SQLException sqlException) {
            logger.error(sqlException);
            sqlException.printStackTrace();
        }
        logger.traceExit();
    }

    /**
     *
     * @param id
     */
    @Override
    public void delete(Integer id) {
        logger.traceEntry("Deleting task with {}", id);
        Connection connection = jdbcUtils.getConnection();

        try(PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Shows WHERE id = ?")) {
            preparedStatement.setInt(1, id);
            int result = preparedStatement.executeUpdate();
        }
        catch (SQLException sqlException){
            logger.error(sqlException);
            sqlException.printStackTrace();
        }
        logger.traceExit();
    }

    /**
     *
     * @param entity
     *          entity must not be null
     */
    @Override
    public void update(Show entity) {
        logger.traceEntry("Updating task {}", entity);
        Connection connection = jdbcUtils.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement
                ("UPDATE Shows SET name = ?, dateTime = ?, price = ? WHERE ID = ?")) {
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, String.valueOf(entity.getDateTime()));
            preparedStatement.setDouble(3, entity.getPrice());
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
}
