package repository.jdbc;

import domain.Seat;
import domain.SeatStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import repository.SeatRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class SeatJdbcRepository implements SeatRepository {

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
    public SeatJdbcRepository(Properties properties) {

        this.jdbcUtils = new JdbcUtils(properties);
    }

    /**
     * Default constructor
     */
    public SeatJdbcRepository() {}

    /**
     *
     * @param id
     * @return
     */
    @Override
    public Seat findOne(Integer id) {
        logger.traceEntry("Finding task with id {} ", id);
        Connection connection = jdbcUtils.getConnection();
        Seat seat = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement
                ("SELECT * FROM Seats WHERE id = ?")) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    id = resultSet.getInt("id");
                    String lodge = resultSet.getString("lodge");
                    Integer row = resultSet.getInt("row");
                    Integer number = resultSet.getInt("number");
                    SeatStatus status = SeatStatus.valueOf(resultSet.getString("status"));

                    seat = new Seat(lodge, row, number, status);
                    seat.setId(id);

                    return seat;
                }
            }
        } catch (SQLException sqlException) {
            logger.error(sqlException);
            sqlException.printStackTrace();
        }
        logger.traceExit("No task found with id {}", id);
        return seat;
    }

    /**
     *
     * @return
     */
    @Override
    public Iterable<Seat> findAll() {
        logger.traceEntry();
        Connection connection = jdbcUtils.getConnection();

        List<Seat> spectators = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Seats")) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String lodge = resultSet.getString("lodge");
                    Integer row = resultSet.getInt("row");
                    Integer number = resultSet.getInt("number");
                    SeatStatus status = SeatStatus.valueOf(resultSet.getString("status"));

                    Seat seat = new Seat(lodge, row, number, status);
                    seat.setId(id);

                    spectators.add(seat);
                }
            }
        }
        catch (SQLException sqlException) {
            logger.error(sqlException);
            sqlException.printStackTrace();
        }
        logger.traceExit(spectators);
        return spectators;
    }

    /**
     *
     * @param entity
     *         entity must be not null
     */
    @Override
    public void save(Seat entity) {
        logger.traceEntry("Saving task {}", entity);
        Connection connection = jdbcUtils.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement
                ("INSERT INTO Seats VALUES (?, ?, ?, ?, ?)")) {
            preparedStatement.setInt(1, entity.getId());
            preparedStatement.setString(2, entity.getLodge());
            preparedStatement.setInt(3, entity.getRow());
            preparedStatement.setInt(4, entity.getNumber());
            preparedStatement.setString(5, String.valueOf(entity.getStatus()));

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

        try(PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Seats WHERE id = ?")) {
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
    public void update(Seat entity) {
        logger.traceEntry("Updating task {}", entity);
        Connection connection = jdbcUtils.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement
                ("UPDATE Seats SET lodge = ?, row = ?, number = ?, status = ? WHERE ID = ?")) {
            preparedStatement.setString(1, entity.getLodge());
            preparedStatement.setInt(2, entity.getRow());
            preparedStatement.setInt(3, entity.getNumber());
            preparedStatement.setString(4, String.valueOf(entity.getStatus()));
            preparedStatement.setInt(5, entity.getId());

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
