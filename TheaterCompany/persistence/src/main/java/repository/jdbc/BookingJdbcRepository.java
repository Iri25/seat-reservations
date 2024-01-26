package repository.jdbc;

import domain.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import repository.BookingRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class BookingJdbcRepository implements BookingRepository {

    private JdbcUtils jdbcUtils;
    private static final Logger logger = LogManager.getLogger();
    private final SpectatorJdbcRepository spectatorJdbcRepository;
    private final TicketJdbcRepository ticketJdbcRepository;

    public BookingJdbcRepository(Properties properties, SpectatorJdbcRepository spectatorJdbcRepository, TicketJdbcRepository ticketJdbcRepository) {

        this.jdbcUtils = new JdbcUtils(properties);
        this.spectatorJdbcRepository = spectatorJdbcRepository;
        this.ticketJdbcRepository = ticketJdbcRepository;
    }

    public BookingJdbcRepository(SpectatorJdbcRepository spectatorJdbcRepository, TicketJdbcRepository ticketJdbcRepository) {
        this.spectatorJdbcRepository = spectatorJdbcRepository;
        this.ticketJdbcRepository = ticketJdbcRepository;
    }

    @Override
    public Booking findOne(Integer id) {
        logger.traceEntry("Finding task with id {} ", id);
        Connection connection = jdbcUtils.getConnection();
        Booking booking = null;
        Savepoint savepoint = null;
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement preparedStatement = connection.prepareStatement
                    ("SELECT * FROM Bookings WHERE id = ?")) {
                preparedStatement.setInt(1, id);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        id = resultSet.getInt("id");
                        Spectator spectator = (Spectator) resultSet.getObject("spectator");
                        Ticket ticket = (Ticket) resultSet.getObject("ticket");

                        booking = new Booking(spectator, ticket);
                        booking.setId(id);

                        savepoint = connection.setSavepoint("findOne");
                        connection.commit();

                        return booking;
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
        return booking;
    }

    @Override
    public Iterable<Booking> findAll() {
        logger.traceEntry();
        Connection connection = jdbcUtils.getConnection();
        List<Booking> bookings = new ArrayList<>();
        Savepoint savepoint = null;
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Bookings")) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        int id = resultSet.getInt("id");
                        int idSpectator = resultSet.getInt("idSpectator");
                        int idTicket = resultSet.getInt("idTicket");

                        Spectator spectator = spectatorJdbcRepository.findOne(idSpectator);
                        Ticket ticket = ticketJdbcRepository.findOne(idTicket);
                        Booking booking = new Booking(spectator, ticket);
                        booking.setId(id);

                        savepoint = connection.setSavepoint("findAll");
                        connection.commit();

                        bookings.add(booking);
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
        logger.traceExit(bookings);
        return bookings;
    }

    @Override
    public void save(Booking entity) {
        logger.traceEntry("Saving task {}", entity);
        Connection connection = jdbcUtils.getConnection();
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement preparedStatement = connection.prepareStatement
                    ("INSERT INTO Bookings VALUES (?, ?, ?)")) {
                preparedStatement.setInt(1, entity.getId());
                preparedStatement.setInt(2, entity.getSpectator().getId());
                preparedStatement.setInt(3, entity.getTicket().getId());

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
    public void update(Booking entity) {

    }
}












/*
    @Override
    public void delete(Integer id) {
        logger.traceEntry("Deleting task with {}", id);
        Connection connection = jdbcUtils.getConnection();

        try(PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Bookings WHERE id = ?")) {
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
    public void update(Booking entity) {
        logger.traceEntry("Updating task {}", entity);
        Connection connection = jdbcUtils.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement
                ("UPDATE Bookings SET spectator = ?, ticket = ?WHERE ID = ?")) {
            preparedStatement.setString(1, String.valueOf(entity.getSpectator()));
            preparedStatement.setString(2, String.valueOf(entity.getTicket()));
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
