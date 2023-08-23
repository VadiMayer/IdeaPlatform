import java.io.FileNotFoundException;
import java.io.FileReader;
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
            String carrier = (String) ticket.get("carrier");
            String departureTime = (String) ticket.get("departure_time");
            String arrivalTime = (String) ticket.get("arrival_time");
            long departureTimeStamp = getTimeStamp(departureTime);
            long arrivalTimeStamp = getTimeStamp(arrivalTime);
            long duration = arrivalTimeStamp - departureTimeStamp;
            minDurations.put(carrier, duration);
            minDurations.get(carrier).
//            if(carrier.equals("TK")) {
//                System.out.println("Минимальное время полета TK: " + duration);
//            } else if(carrier.equals("S7")) {
//                System.out.println("Минимальное время полета S7: " + duration);
//            }
//            flightDurations.add(duration);
            Integer price = ((Long)ticket.get("price")).intValue();
            prices.add(price);
            double averagePrice = prices.stream().mapToInt(v -> v).average().orElse(0.0);
            int medianPrice = median(prices);
            double diff = averagePrice - medianPrice;
            System.out.println("Разница между средней и медианой цены: " + diff);
        }
    }
    public static long getTimeStamp(String time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime localTime = LocalTime.parse(time, formatter);
        return localTime.toSecondOfDay() * 1000L;
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
