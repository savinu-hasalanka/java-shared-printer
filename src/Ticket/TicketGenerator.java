package Ticket;

import Users.Passenger;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class TicketGenerator {
    private static final Random random = new Random();
    private static int count = 0;
    private static final double MIN_TICKET_PRICE = 500.00;
    private static final double MAX_TICKET_PRICE = 2500.00;

    public static Ticket generateRandomTicket(Passenger passenger) {
        Location[] locations = selectRandomLocations();
        Location from = locations[0];
        Location to = locations[1];
        String seatNumber = generateSeatNumber(from, to);
        double price = generatePrice();


        return new Ticket(passenger, from, to, price, seatNumber);
    }

    private static Location[] selectRandomLocations() {
        Set<Location> locations = new HashSet<>(2);

        while (locations.size() < 2) {
            int pick = random.nextInt(Location.values().length);
            locations.add(Location.values()[pick]);
        }

        Location[] locationsArray = new Location[2];

        int index = 0;
        for(Location location:locations){
            locationsArray[index++] = location;
        }

        return  locationsArray;
    }

    private static String generateSeatNumber(Location from, Location to) {
        String seatCode = from.toString().substring(0, 3).toUpperCase() + to.toString().substring(0, 3).toUpperCase();
        return String.format("%s%03d", seatCode, ++count);
    }

    private static double generatePrice() {
        return Math.round((MIN_TICKET_PRICE + (MAX_TICKET_PRICE - MIN_TICKET_PRICE) * random.nextDouble()));

    }
}
