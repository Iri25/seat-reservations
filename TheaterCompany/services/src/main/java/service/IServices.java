package service;

import domain.*;

import java.util.List;

public interface IServices {

    Login login(Login user) throws Exception;

    void logout(Login user) throws Exception;

    void checkIdLogin();

    Iterable<Login> getAllUsers();

    void addUser(Login user);

    void checkIdSpectator();

    Iterable<Spectator> getAllSpectators();

    void addSpectator(Spectator spectator);

    Iterable<Show> getAllShows();

    Iterable<Seat> getAllSeats();

    Iterable<Ticket> getAllTickets();

    Iterable<Booking> getAllBookings();

    Integer checkIdTicket();

    Integer checkIdBooking();

    Integer checkIdShow();

    void buyTicketBooking(Ticket ticket, Spectator spectator);

    void addShow(Show show);

    void deleteShow(Show show);

    void updateShow(Show show);

    void updateSeatStatus(Seat seat);

    Spectator getSpectator(String username);
}
