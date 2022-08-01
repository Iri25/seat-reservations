package repository.jdbc;

import domain.Booking;
import domain.Spectator;
import domain.Ticket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import repository.BookingRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class BookingJdbcRepository implements BookingRepository {

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
    public BookingJdbcRepository(Properties properties) {

        this.jdbcUtils = new JdbcUtils(properties);
    }

    /**
     * Default constructor
     */
    public BookingJdbcRepository() {}

    /**
     *
     * @param id
     * @return
     */
    @Override
    public Booking findOne(Integer id) {
        logger.traceEntry("Finding task with id {} ", id);
        Connection connection = jdbcUtils.getConnection();
        Booking booking = null;
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

                    return booking;
                }
            }
        } catch (SQLException sqlException) {
            logger.error(sqlException);
            sqlException.printStackTrace();
        }
        logger.traceExit("No task found with id {}", id);
        return booking;
    }

    /**
     *
     * @return
     */
    @Override
    public Iterable<Booking> findAll() {
        logger.traceEntry();
        Connection connection = jdbcUtils.getConnection();

        List<Booking> bookings = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Tickets")) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    Spectator spectator = (Spectator) resultSet.getObject("spectator");
                    Ticket ticket = (Ticket) resultSet.getObject("ticket");

                    Booking booking = new Booking(spectator, ticket);
                    booking.setId(id);

                    bookings.add(booking);
                }
            }
        }
        catch (SQLException sqlException) {
            logger.error(sqlException);
            sqlException.printStackTrace();
        }
        logger.traceExit(bookings);
        return bookings;
    }

    /**
     *
     * @param entity
     *         entity must be not null
     */
    @Override
    public void save(Booking entity) {
        logger.traceEntry("Saving task {}", entity);
        Connection connection = jdbcUtils.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement
                ("INSERT INTO Bookings VALUES (?, ?, ?)")) {
            preparedStatement.setInt(1, entity.getId());
            preparedStatement.setString(2, String.valueOf(entity.getSpectator()));
            preparedStatement.setString(3, String.valueOf(entity.getTicket()));

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

    /**
     *
     * @param entity
     *          entity must not be null
     */
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
}

//package repository.jdbc;
//
//import domain.Booking;
//import domain.Ticket;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.hibernate.Session;
//import org.hibernate.SessionFactory;
//import org.hibernate.Transaction;
//import org.hibernate.boot.MetadataSources;
//import org.hibernate.boot.registry.StandardServiceRegistry;
//import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
//import repository.BookingRepository;
//import repository.jdbc.JdbcUtils;
//
//import java.sql.Connection;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Properties;
//
//public class BookingJdbcRepository implements BookingRepository {
//
//    /**
//     *
//     */
//    private JdbcUtils jdbcUtils;
//
//    /**
//     *
//     */
//    private static final Logger logger= LogManager.getLogger();
//
//    /**
//     *
//     */
//    static SessionFactory sessionFactory;
//
//    public BookingJdbcRepository(Properties properties) {
//
//        this.jdbcUtils = new JdbcUtils(properties);
//        initialize();
//    }
//
//    /**
//     *
//     */
//    static void initialize() {
//        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
//        try {
//            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
//        }
//        catch (Exception e) {
//            StandardServiceRegistryBuilder.destroy(registry);
//        }
//    }
//
//    /**
//     *
//     */
//    static void close() {
//        if (sessionFactory != null) sessionFactory.close();
//    }
//
//    /**
//     *
//     * @param id
//     * @return
//     */
//    @Override
//    public Booking findOne(Integer id) {
//        logger.traceEntry("Finding task with id {} ", id);
//        Connection connection = jdbcUtils.getConnection();
//
//        List<Booking> result = null;
//        try (Session session = sessionFactory.openSession()) {
//            Transaction transaction = null;
//            try {
//                transaction = session.beginTransaction();
//                result = session.createQuery("FROM Bookings WHERE id = :id", Booking.class)
//                        .setParameter("id", id).list();
//                transaction.commit();
//            }
//            catch (RuntimeException exception) {
//                if (transaction != null) transaction.rollback();
//            }
//        }
//        logger.traceExit("No task found with id {}", id);
//        return result.get(0);
//    }
//
//    /**
//     *
//     * @return
//     */
//    @Override
//    public Iterable<Booking> findAll() {
//        logger.traceEntry();
//        Connection connection = jdbcUtils.getConnection();
//
//        List<Booking> bookings = new ArrayList<>();
//        try (Session session = sessionFactory.openSession()) {
//            Transaction transaction = null;
//            try {
//                transaction = session.beginTransaction();
//                bookings = session.createQuery("FROM Bookings ", Booking.class).list();
//                transaction.commit();
//            }
//            catch (RuntimeException exception) {
//                if (transaction != null) transaction.rollback();
//            }
//        }
//        return bookings;
//    }
//
//    /**
//     *
//     * @param entity
//     *         entity must be not null
//     */
//    @Override
//    public void save(Booking entity) {
//        logger.traceEntry("Saving task {}", entity);
//        Connection connection = jdbcUtils.getConnection();
//
//        try (Session session = sessionFactory.openSession()) {
//            Transaction transaction = null;
//            try {
//                transaction = session.beginTransaction();
//                int id;
//                try {
//                    id = session.createQuery("SELECT MAX(id) FROM Bookings", Integer.class).getSingleResult() + 1;
//                }
//                catch (Exception exception) {
//                    id = 1;
//                }
//                entity.setId(id);
//                session.save(entity);
//                transaction.commit();
//            }
//            catch (Exception exception) {
//                if (transaction != null) transaction.rollback();
//            }
//        }
//        logger.traceExit();
//    }
//
//    /**
//     *
//     * @param id
//     */
//    @Override
//    public void delete(Integer id) {
//        logger.traceEntry("Deleting task with {}", id);
//        Connection connection = jdbcUtils.getConnection();
//
//        try (Session session = sessionFactory.openSession()) {
//            Transaction transaction = null;
//            try {
//                transaction = session.beginTransaction();
//                session.delete(id);
//                transaction.commit();
//            }
//            catch (RuntimeException exception) {
//                if (transaction != null) transaction.rollback();
//            }
//        }
//        logger.traceExit();
//    }
//
//    /**
//     *
//     * @param entity
//     *          entity must not be null
//     */
//    @Override
//    public void update(Booking entity) {
//        logger.traceEntry("Updating task {}", entity);
//        Connection connection = jdbcUtils.getConnection();
//
//        try (Session session = sessionFactory.openSession()) {
//            Transaction transaction = null;
//            try {
//                transaction = session.beginTransaction();
//                session.update(entity);
//                transaction.commit();
//            }
//            catch (RuntimeException exception) {
//                if (transaction != null) transaction.rollback();
//            }
//        }
//        logger.traceExit();
//    }
//}
//
