package domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * 
 */
public class Show extends Entity<Integer> {

    /**
     * Default constructor
     */
    public Show() {
    }

    /**
     * 
     */
    private String name;

    /**
     * 
     */
    private LocalDateTime dateTime;

    /**
     *
     */
    private Double price;

    /**
     *
     * @param name
     * @param dateTime
     */
    public Show(String name, LocalDateTime dateTime, Double price) {
        this.name = name;
        this.dateTime = dateTime;
        this.price = price;
    }


    /**
     * @return
     */
    public String getName() {

        return name;
    }

    /**
     * @return
     */
    public LocalDateTime getDateTime() {

        return dateTime;
    }

    /**
     * @return
     */
    public Double getPrice() {

        return price;
    }

    /**
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @param dateTime
     */
    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    /**
     *
     * @param price
     */
    public void setPrice(Double price) {
        this.price = price;
    }

    /**
     * @return
     */
    public LocalDate getDate() {

        return dateTime.toLocalDate();
    }

    /**
     * @return
     */
    public LocalTime getTime() {

        return dateTime.toLocalTime();
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return "Show{" +
                "id=" + getId() +
                ", name='" + name + '\'' +
                ", dateTime=" + dateTime +
                ", price=" + price +
                '}';
    }

    /**
     * @return
     */
    public String getIdString() {

        return super.getId().toString();
    }

    /**
     * @return
     */
    public String getNameString() {

        return "" + name;
    }

    /**
     * @return
     */
    public String getDateString() {

        return "" + dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    /**
     * @return
     */
    public String getTimeString() {

        return "" + dateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    /**
     *
     * @return
     */
    public String getPriceString(){
        return "" + price;
    }
}