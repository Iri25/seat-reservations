package domain;

/**
 * 
 */
public class Ticket extends Entity<Integer> {

    /**
     * Default constructor
     */
    private Ticket() {
    }

    /**
     *
     */
    private Show show;

    /**
     *
     */
    public Seat seat;

    /**
     * 
     */
    private PayMethod payMethod;

    /**
     *
     * @param seat
     * @param show
     * @param payMethod
     */
    public Ticket(Show show, Seat seat, PayMethod payMethod){
        this.show = show;
        this.seat = seat;
        this.payMethod = payMethod;
    }

    /**
     * @return
     */
    public Show getShow() {
        return show;
    }

    /**
     * @return
     */
    public Seat getSeat() {
        return seat;
    }

    /**
     * @return
     */
    public PayMethod getPayMethod() {
        return payMethod;
    }

    /**
     * @param show
     */
    public void setShow(Show show) {
        this.show = show;
    }

    /**
     * @param seat
     */
    public void setSeat(Seat seat) {
        this.seat = seat;
    }


    /**
     * @param payMethod
     */
    public void setPayMethod(PayMethod payMethod) {
        this.payMethod = payMethod;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + getId() +
                ", show=" + show +
                ", seat=" + seat +
                ", payStatus=" + payMethod +
                '}';
    }
}