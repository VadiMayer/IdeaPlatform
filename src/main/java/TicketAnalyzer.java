import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class TicketAnalyzer {
    public static void main(String[] args) throws Exception {
        JSONParser parser = new JSONParser();
        JSONObject ticketsJson = null;
        HashMap<String, Long> minDurations = new HashMap<>();
        List<Ticket> ticketsInJava = new ArrayList<>();
        try (FileReader reader = new FileReader("src/main/resources/tickets.json")) {
            reader.read();
            ticketsJson = (JSONObject) parser.parse(reader);
        } catch (FileNotFoundException e) {
        }
        JSONArray tickets = (JSONArray) ticketsJson.get("tickets");
    //  List<Long> flightDurations = new ArrayList<>();
        List<Integer> prices = new ArrayList<>();
        for(Object ticketObj : tickets) {
            JSONObject ticket = (JSONObject) ticketObj;
            if (!(ticket.get("origin_name").equals("Владивосток"))) {
                continue;
            }
            ticketsInJava.add(new Ticket((String)ticket.get("origin_name"),
                    (String)ticket.get("destination_name"), (LocalDate) ticket.get("departure_date"),
                    (LocalTime) ticket.get("departure_time"), (LocalDate) ticket.get("arrival_date"),
                    (LocalTime) ticket.get("arrival_time"), (String) ticket.get("carrier"),
                    (int)ticket.get("price")));
        }
        for (Ticket ticket:ticketsInJava) {
            LocalDateTime departureTimeStamp = getTimeStamp(ticket.getDeparture_date(), ticket.getDeparture_time());
            LocalDateTime arrivalTimeStamp = getTimeStamp(ticket.getArrival_date(), ticket.getArrival_time());
            long duration = arrivalTimeStamp.toEpochSecond() - departureTimeStamp;
            if (minDurations.containsKey(ticket.getCarrier())) {
                if (minDurations.get(ticket.getCarrier()) > duration) {
                    minDurations.put(ticket.getCarrier(), duration);
                }
            } else
                minDurations.put(ticket.getCarrier(), duration);
//            if(carrier.equals("TK")) {
//                System.out.println("Минимальное время полета TK: " + duration);
//            } else if(carrier.equals("S7")) {
//                System.out.println("Минимальное время полета S7: " + duration);
//            }
//            flightDurations.add(duration);
            Integer price = ticket.getPrice();
            prices.add(price);
            double averagePrice = prices.stream().mapToInt(v -> v).average().orElse(0.0);
            int medianPrice = median(prices);
            double diff = averagePrice - medianPrice;
            System.out.println("Разница между средней и медианой цены: " + diff);
        }
    }
    public static LocalDateTime getTimeStamp(LocalDate date, LocalTime time) {
        return LocalDateTime.of(date, time);
    }
    public static int median(List<Integer> list) {
        list.sort(Integer::compareTo);
        int middle = list.size() / 2;
        if (list.size() % 2 == 0) {
            return (list.get(middle) + list.get(middle - 1)) / 2;
        } else {
            return list.get(middle);
        }
    }
}
