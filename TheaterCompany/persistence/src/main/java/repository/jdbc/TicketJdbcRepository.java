package repository.jdbc;

import domain.PayMethod;
import domain.Seat;
import domain.Show;
import domain.Ticket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import repository.TicketRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class TicketJdbcRepository implements TicketRepository {

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
    public TicketJdbcRepository(Properties properties) {

        this.jdbcUtils = new JdbcUtils(properties);
    }

    /**
     * Default constructor
     */
    public TicketJdbcRepository() {}

    /**
     *
     * @param id
     * @return
     */
    @Override
    public Ticket findOne(Integer id) {
        logger.traceEntry("Finding task with id {} ", id);
        Connection connection = jdbcUtils.getConnection();
        Ticket ticket = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement
                ("SELECT * FROM Tickets WHERE id = ?")) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    id = resultSet.getInt("id");
                    Show show = (Show) resultSet.getObject("show");
                    Seat seat = (Seat) resultSet.getObject("seat");
                    PayMethod payMethod = PayMethod.valueOf(resultSet.getString("payMethod"));

                    ticket = new Ticket(show, seat, payMethod);
                    ticket.setId(id);

                    return ticket;
                }
            }
        } catch (SQLException sqlException) {
            logger.error(sqlException);
            sqlException.printStackTrace();
        }
        logger.traceExit("No task found with id {}", id);
        return ticket;
    }

    /**
     *
     * @return
     */
    @Override
    public Iterable<Ticket> findAll() {
        logger.traceEntry();
        Connection connection = jdbcUtils.getConnection();

        List<Ticket> tickets = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Tickets")) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    Show show = (Show) resultSet.getObject("show");
                    Seat seat = (Seat) resultSet.getObject("seat");
                    PayMethod payMethod = PayMethod.valueOf(resultSet.getString("payMethod"));

                    Ticket ticket = new Ticket(show, seat, payMethod);
                    ticket.setId(id);

                    tickets.add(ticket);
                }
            }
        }
        catch (SQLException sqlException) {
            logger.error(sqlException);
            sqlException.printStackTrace();
        }
        logger.traceExit(tickets);
        return tickets;
    }

    /**
     *
     * @param entity
     *         entity must be not null
     */
    @Override
    public void save(Ticket entity) {
        logger.traceEntry("Saving task {}", entity);
        Connection connection = jdbcUtils.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement
                ("INSERT INTO Tickets VALUES (?, ?, ?, ?)")) {
            preparedStatement.setInt(1, entity.getId());
            preparedStatement.setString(2, String.valueOf(entity.getShow()));
            preparedStatement.setString(3, String.valueOf(entity.getSeat()));
            preparedStatement.setString(5, String.valueOf(entity.getPayMethod()));

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

    /**
     *
     * @param entity
     *          entity must not be null
     */
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
}

//package repository.jdbc;
//
//import domain.*;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.hibernate.Session;
//import org.hibernate.SessionFactory;
//import org.hibernate.Transaction;
//import org.hibernate.boot.MetadataSources;
//import org.hibernate.boot.registry.StandardServiceRegistry;
//import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
//import repository.TicketRepository;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Properties;
//
//public class TicketJdbcRepository implements TicketRepository {
//
//    /**
//     *
//     */
//    private JdbcUtils jdbcUtils;
//
//    /**
//     *
//     */
//    private static final Logger logger = LogManager.getLogger();
//
//    /**
//     *
//     */
//    static SessionFactory sessionFactory;
//
//
//    /**
//     *
//     * @param properties
//     */
//    public TicketJdbcRepository(Properties properties) {
//
//        this.jdbcUtils = new JdbcUtils(properties);
//        initialize();
//    }
//
//    /**
//     * Default constructor
//     */
//    public TicketJdbcRepository() {}
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
//    public Ticket findOne(Integer id) {
//        logger.traceEntry("Finding task with id {} ", id);
//        Connection connection = jdbcUtils.getConnection();
//
//        List<Ticket> result = null;
//        try (Session session = sessionFactory.openSession()) {
//            Transaction transaction = null;
//            try {
//                transaction = session.beginTransaction();
//                result = session.createQuery("FROM Tickets WHERE id = :id", Ticket.class)
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
//    public Iterable<Ticket> findAll() {
//        logger.traceEntry();
//        Connection connection = jdbcUtils.getConnection();
//
//        List<Ticket> tickets = new ArrayList<>();
//        try (Session session = sessionFactory.openSession()) {
//            Transaction transaction = null;
//            try {
//                transaction = session.beginTransaction();
//                tickets = session.createQuery("FROM Tickets", Ticket.class).list();
//                transaction.commit();
//            }
//            catch (RuntimeException exception) {
//                if (transaction != null) transaction.rollback();
//            }
//        }
//        return tickets;
//    }
//
//    /**
//     *
//     * @param entity
//     *         entity must be not null
//     */
//    @Override
//    public void save(Ticket entity) {
//        logger.traceEntry("Saving task {}", entity);
//        Connection connection = jdbcUtils.getConnection();
//
//        try (Session session = sessionFactory.openSession()) {
//            Transaction transaction = null;
//            try {
//                transaction = session.beginTransaction();
//                session.save(entity);
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
//    public void update(Ticket entity) {
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
