package domain;

/**
 * 
 */
public class Booking extends Entity<Integer> {

    /**
     * Default constructor
     */
    public Booking() {
    }

    /**
     * 
     */
    private Spectator spectator;

    /**
     * 
     */
    private Ticket ticket;


    /**
     *
     * @param spectator
     * @param ticket
     */
    public Booking(Spectator spectator, Ticket ticket) {
        this.spectator = spectator;
        this.ticket = ticket;
    }

    /**
     *
     * @return
     */
    public Spectator getSpectator() {
        return spectator;
    }

    /**
     *
     * @return
     */
    public Ticket getTicket() {
        return ticket;
    }

    /**
     *
     * @param spectator
     */
    public void setSpectator(Spectator spectator) {
        this.spectator = spectator;
    }

    /**
     *
     * @param ticket
     */
    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", spectator=" + spectator +
                ", ticket=" + ticket +
                '}';
    }

    /**
     *
     * @return
     */
    public String getNameShowString() {

        return ticket.getShow().getNameString();
    }

    /**
     *
     * @return
     */
    public String getDateShowString() {

        return ticket.getShow().getDateString();
    }

    /**
     *
     * @return
     */
    public String getTimeShowString() {

        return ticket.getShow().getTimeString();
    }

    /**
     *
     * @return
     */
    public String getPriceShowString() {

        return ticket.getShow().getPriceString();
    }

    /**
     *
     * @return
     */
    public String getLodgeSeatString() {

        return ticket.getSeat().getLodgeString();
    }

    /**
     *
     * @return
     */
    public String getRowSeatString() {

        return ticket.getSeat().getRowString();
    }

    /**
     *
     * @return
     */
    public String getNumberSeatString() {

        return ticket.getSeat().getNumberString();
    }



}