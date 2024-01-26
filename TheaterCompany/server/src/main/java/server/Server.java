package server;

import domain.*;
import repository.jdbc.*;
import service.IServices;

public class Server implements IServices {

    private final LoginJdbcRepository userRepository;
    private final SpectatorJdbcRepository spectatorRepository;
    private final ShowJdbcRepository showRepository;
    private final SeatJdbcRepository seatRepository;
    private final TicketJdbcRepository ticketRepository;
    private final BookingJdbcRepository bookingRepository;

    private Integer freeIdLogin;
    private Integer freeIdSpectator;
    private Integer freeIdTicket;
    private Integer freeIdBooking;
    private Integer freeIdShow;

    public Server(LoginJdbcRepository userRepository, SpectatorJdbcRepository spectatorRepository,
                  ShowJdbcRepository showRepository, SeatJdbcRepository seatRepository,
                  TicketJdbcRepository ticketRepository, BookingJdbcRepository bookingRepository) {
        this.userRepository = userRepository;
        this.spectatorRepository = spectatorRepository;
        this.showRepository = showRepository;
        this.seatRepository = seatRepository;
        this.ticketRepository = ticketRepository;
        this.bookingRepository = bookingRepository;

        freeIdLogin = 0;
        freeIdSpectator = 0;
        freeIdTicket = 0;
        freeIdBooking = 0;
        freeIdShow = 0;

    }

    @Override
    public synchronized Login login(Login user) throws Exception {

        Login userLogin = userRepository.findEntity(user.getUsername(), user.getPassword(), user.getStatus());
        if(userLogin != null)
            return userLogin;
        else
            throw new Exception("Authentication failed.");
    }

    @Override
    public synchronized void logout(Login user) {

        Login userLogin = userRepository.findOne(user.getId());
    }

    @Override
    public void checkIdLogin() {
        freeIdLogin = 0;
        int number = 0;
        int size = 0;
        boolean ok = true;
        for(Login login: getAllUsers())
            size++;
        for(Login login: getAllUsers()) {
            freeIdLogin++;
            number++;
            if (!freeIdLogin.equals(login.getId())) {
                ok = false;
                break;
            }
        }
        if(number == size && ok)
            freeIdLogin++;

    }

    @Override
    public Iterable<Login> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void addUser(Login login) {
        Login user = new Login(login.getUsername(), login.getPassword(), login.getStatus());
        checkIdLogin();
        user.setId(freeIdLogin);
        userRepository.save(user);
    }

    @Override
    public void checkIdSpectator() {
        freeIdSpectator = 0;
        int number = 0;
        int size = 0;
        boolean ok = true;
        for(Spectator spectator: getAllSpectators())
            size++;
        for(Spectator spectator: getAllSpectators()) {
            freeIdSpectator++;
            number++;
            if (!freeIdSpectator.equals(spectator.getId())) {
                ok = false;
                break;
            }
        }
        if(number == size && ok)
            freeIdSpectator++;
    }

    @Override
    public Iterable<Spectator> getAllSpectators() {
        return spectatorRepository.findAll();
    }

    @Override
    public void addSpectator(Spectator spectator) {
        Spectator user = new Spectator(spectator.getUsername(), spectator.getPassword(), spectator.getStatus(),
                spectator.getFirstName(), spectator.getLastName(), spectator.getEmail());
        checkIdSpectator();
        user.setId(freeIdLogin);
        spectatorRepository.save(user);
    }

    @Override
    public Iterable<Show> getAllShows() {
        return showRepository.findAll();
    }

    @Override
    public Iterable<Seat> getAllSeats() {
        return seatRepository.findAll();
    }

    @Override
    public Iterable<Ticket> getAllTickets() { return ticketRepository.findAll(); }

    @Override
    public Iterable<Booking> getAllBookings() { return bookingRepository.findAll(); }

    @Override
    public Integer checkIdTicket() {
        freeIdTicket = 0;
        int number = 0;
        int size = 0;
        boolean ok = true;
        for(Ticket ticket: getAllTickets())
            size++;
        for(Ticket ticket: getAllTickets()) {
            freeIdTicket++;
            number++;
            if (!freeIdTicket.equals(ticket.getId())) {
                ok = false;
                break;
            }
        }
        if(number == size && ok)
            freeIdTicket++;
        return freeIdTicket;
    }

    @Override
    public Integer checkIdBooking() {
        freeIdBooking = 0;
        int number = 0;
        int size = 0;
        boolean ok = true;
        for(Booking booking: getAllBookings())
            size++;
        for(Booking booking: getAllBookings()) {
            freeIdBooking++;
            number++;
            if (!freeIdBooking.equals(booking.getId())) {
                ok = false;
                break;
            }
        }
        if(number == size && ok)
            freeIdBooking++;
        return freeIdBooking;
    }
    @Override
    public void buyTicketBooking(Ticket ticket, Spectator spectator) {
        checkIdTicket();
        ticket.setId(freeIdTicket);
        ticketRepository.save(ticket);

        Booking booking = new Booking(spectator, ticket);
        checkIdBooking();
        booking.setId(freeIdBooking);
        bookingRepository.save(booking);
    }

    @Override
    public Integer checkIdShow() {
        freeIdShow = 0;
        int number = 0;
        int size = 0;
        boolean ok = true;
        for(Show show: getAllShows())
            size++;
        for(Show show: getAllShows()) {
            freeIdShow++;
            number++;
            if (!freeIdShow.equals(show.getId())) {
                ok = false;
                break;
            }
        }
        if(number == size && ok)
            freeIdShow++;
        return freeIdShow;
    }
    @Override
    public void addShow(Show show) {
        checkIdShow();
        show.setId(freeIdShow);
        showRepository.save(show);
    }

    @Override
    public void deleteShow(Show show) {
        showRepository.delete(show.getId());
    }

    @Override
    public void updateShow(Show show) {
        showRepository.update(show);
    }

    @Override
    public void updateSeatStatus(Seat seat) {
        seatRepository.update(seat);
    }

    @Override
    public Spectator getSpectator(String username) { return spectatorRepository.findOne(username); }
}
