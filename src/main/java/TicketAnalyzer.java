import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        List<Integer> prices = new ArrayList<>();
        for(Object ticketObj : tickets) {
            JSONObject ticket = (JSONObject) ticketObj;
            if (!(ticket.get("origin_name").equals("Владивосток")) | !(ticket.get("destination_name").equals("Тель-Авив"))) {
                continue;
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy");
            DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("H:mm");
            ticketsInJava.add(new Ticket((String)ticket.get("origin_name"),
                    (String)ticket.get("destination_name"), LocalDate.parse((String)ticket.get("departure_date"), formatter),
                    LocalTime.parse((String)ticket.get("departure_time"),formatter2), LocalDate.parse((String)ticket.get("arrival_date"),formatter),
                    LocalTime.parse((String)ticket.get("arrival_time"),formatter2), (String) ticket.get("carrier"),
                    ((Long)ticket.get("price")).intValue()));
        }
        for (Ticket ticket:ticketsInJava) {
            LocalDateTime departureTimeStamp = getTimeStamp(ticket.getDeparture_date(), ticket.getDeparture_time());
            LocalDateTime arrivalTimeStamp = getTimeStamp(ticket.getArrival_date(), ticket.getArrival_time());
            Duration duration = Duration.between(departureTimeStamp, arrivalTimeStamp);

            if (minDurations.containsKey(ticket.getCarrier())) {
                if (minDurations.get(ticket.getCarrier()) > duration.toMinutes()) {
                    minDurations.put(ticket.getCarrier(), duration.toMinutes());
                }
            } else
                minDurations.put(ticket.getCarrier(), duration.toMinutes());
            Integer price = ticket.getPrice();
            prices.add(price);
        }
        for (Map.Entry<String,Long> entry: minDurations.entrySet()) {
            System.out.println("Минимальное время полета " + entry.getKey() + " : "  + entry.getValue());
        }
        double averagePrice = prices.stream().mapToInt(v -> v).average().orElse(0.0);
        int medianPrice = median(prices);
        double diff = averagePrice - medianPrice;
        System.out.println("Разница между средней и медианой цены: " + diff);
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
