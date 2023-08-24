import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class Ticket {
    private String origin_name;
    private String destination_name;
    private LocalDate departure_date;
    private LocalTime departure_time;
    private LocalDate arrival_date;
    private LocalTime arrival_time;
    private String carrier;
    private int price;

    public Ticket(String origin_name, String destination_name, LocalDate departure_date, LocalTime departure_time, LocalDate arrival_date, LocalTime arrival_time, String carrier, int price) {
        this.origin_name = origin_name;
        this.destination_name = destination_name;
        this.departure_date = departure_date;
        this.departure_time = departure_time;
        this.arrival_date = arrival_date;
        this.arrival_time = arrival_time;
        this.carrier = carrier;
        this.price = price;
    }

    public String getOrigin_name() {
        return origin_name;
    }

    public void setOrigin_name(String origin_name) {
        this.origin_name = origin_name;
    }

    public String getDestination_name() {
        return destination_name;
    }

    public void setDestination_name(String destination_name) {
        this.destination_name = destination_name;
    }

    public LocalDate getDeparture_date() {
        return departure_date;
    }

    public void setDeparture_date(LocalDate departure_date) {
        this.departure_date = departure_date;
    }

    public LocalTime getDeparture_time() {
        return departure_time;
    }

    public void setDeparture_time(LocalTime departure_time) {
        this.departure_time = departure_time;
    }

    public LocalDate getArrival_date() {
        return arrival_date;
    }

    public void setArrival_date(LocalDate arrival_date) {
        this.arrival_date = arrival_date;
    }

    public LocalTime getArrival_time() {
        return arrival_time;
    }

    public void setArrival_time(LocalTime arrival_time) {
        this.arrival_time = arrival_time;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return price == ticket.price && origin_name.equals(ticket.origin_name) && destination_name.equals(ticket.destination_name) && departure_date.equals(ticket.departure_date) && departure_time.equals(ticket.departure_time) && arrival_date.equals(ticket.arrival_date) && arrival_time.equals(ticket.arrival_time) && carrier.equals(ticket.carrier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(origin_name, destination_name, departure_date, departure_time, arrival_date, arrival_time, carrier, price);
    }
}
