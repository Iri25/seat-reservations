package repository.jdbc;

import domain.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import repository.TicketRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class TicketJdbcRepository implements TicketRepository {

    private JdbcUtils jdbcUtils;
    private static final Logger logger = LogManager.getLogger();

    private final ShowJdbcRepository showJdbcRepository;
    private final SeatJdbcRepository seatJdbcRepository;

    public TicketJdbcRepository(Properties properties, ShowJdbcRepository showJdbcRepository, SeatJdbcRepository seatJdbcRepository) {

        this.jdbcUtils = new JdbcUtils(properties);
        this.showJdbcRepository = showJdbcRepository;
        this.seatJdbcRepository = seatJdbcRepository;
    }

    public TicketJdbcRepository(ShowJdbcRepository showJdbcRepository, SeatJdbcRepository seatJdbcRepository) {
        this.showJdbcRepository = showJdbcRepository;
        this.seatJdbcRepository = seatJdbcRepository;
    }

    @Override
    public Ticket findOne(Integer id) {
        logger.traceEntry("Finding task with id {} ", id);
        Connection connection = jdbcUtils.getConnection();
        Ticket ticket = null;
        Savepoint savepoint = null;
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement preparedStatement = connection.prepareStatement
                    ("SELECT * FROM Tickets WHERE id = ?")) {
                preparedStatement.setInt(1, id);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        id = resultSet.getInt("id");
                        Integer idShow = resultSet.getInt("idShow");
                        Integer idSeat = resultSet.getInt("idSeat");
                        PayMethod payMethod = PayMethod.valueOf(resultSet.getString("payMethod"));

                        Show show = showJdbcRepository.findOne(idShow);
                        Seat seat = seatJdbcRepository.findOne(idSeat);
                        ticket = new Ticket(show, seat, payMethod);
                        ticket.setId(id);

                        savepoint = connection.setSavepoint("findOne");
                        connection.commit();

                        return ticket;
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
        return ticket;
    }

    @Override
    public Iterable<Ticket> findAll() {
        logger.traceEntry();
        Connection connection = jdbcUtils.getConnection();
        List<Ticket> tickets = new ArrayList<>();
        Savepoint savepoint = null;
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Tickets")) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        int id = resultSet.getInt("id");
                        int idShow = resultSet.getInt("idShow");
                        int idSeat = resultSet.getInt("idSeat");
                        PayMethod payMethod = PayMethod.valueOf(resultSet.getString("payMethod"));

                        Show show = showJdbcRepository.findOne(idShow);
                        Seat seat = seatJdbcRepository.findOne(idSeat);
                        Ticket ticket = new Ticket(show, seat, payMethod);
                        ticket.setId(id);

                        savepoint = connection.setSavepoint("findAll");
                        connection.commit();

                        tickets.add(ticket);
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
        logger.traceExit(tickets);
        return tickets;
    }

    @Override
    public void save(Ticket entity) {
        logger.traceEntry("Saving task {}", entity);
        Connection connection = jdbcUtils.getConnection();
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement preparedStatement = connection.prepareStatement
                    ("INSERT INTO Tickets VALUES (?, ?, ?, ?)")) {
                preparedStatement.setInt(1, entity.getId());
                preparedStatement.setInt(2, entity.getShow().getId());
                preparedStatement.setInt(3, entity.getSeat().getId());
                preparedStatement.setString(4, String.valueOf(entity.getPayMethod()));

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
    public void update(Ticket entity) {

    }
}

















    /*
    @Override
    public void delete(Integer id) {
        logger.traceEntry("Deleting task with {}", id);
        Connection connection = jdbcUtils.getConnection();

        try(PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Tickets WHERE id = ?")) {
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
    public void update(Ticket entity) {
        logger.traceEntry("Updating task {}", entity);
        Connection connection = jdbcUtils.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement
                ("UPDATE Tickets SET show = ?, seat = ?, payMethod = ? WHERE ID = ?")) {
            preparedStatement.setString(1, String.valueOf(entity.getShow()));
            preparedStatement.setString(2, String.valueOf(entity.getSeat()));
            preparedStatement.setString(3, String.valueOf(entity.getPayMethod()));
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