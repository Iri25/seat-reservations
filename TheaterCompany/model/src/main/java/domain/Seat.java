package domain;

/**
 * 
 */
public class Seat extends Entity<Integer>{

    /**
     * Default constructor
     */
    public Seat() {
    }

    /**
     * 
     */
    private String lodge;

    /**
     * 
     */
    private Integer row;

    /**
     * 
     */
    private Integer number;

    /**
     * 
     */
    private SeatStatus status;

    /**
     *
     * @param lodge
     * @param row
     * @param number
     * @param status
     */
    public Seat(String lodge, Integer row, Integer number, SeatStatus status){
        this.lodge = lodge;
        this.row = row;
        this.number = number;
        this.status = status;
    }

    /**
     * @return
     */
    public String getLodge() {
        return lodge;
    }

    /**
     * @return
     */
    public Integer getRow() {
        return row;
    }

    /**
     * @return
     */
    public Integer getNumber() {
        return number;
    }

    /**
     * @return
     */
    public SeatStatus getStatus() {
        return status;
    }

    /**
     * @param lodge
     */
    public void setLodge(String lodge) {
        this.lodge = lodge;
    }

    /**
     * @param row
     */
    public void setRow(Integer row) {
        this.row = row;
    }
    /**
     * @param number
     */
    public void setNumber(Integer number) {
        this.number = number;
    }

    /**
     * @param status
     */
    public void setSeatStatus(SeatStatus status) {
        this.status = status;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return "Seat{" +
                "id=" + getId() +
                ", lodge='" + lodge + '\'' +
                ", row=" + row +
                ", number=" + number +
                ", status=" + status +
                '}';
    }

    /**
     * @return
     */
    public String getLodgeString() {

        return "" + lodge;
    }

    /**
     * @return
     */
    public String getRowString() {

        return "" + row;
    }

    /**
     * @return
     */
    public String getNumberString() {

        return "" + number;
    }

}