package Ticket;

import Users.Passenger;
import Utility.Gender;
import Utility.NIC;

public class Ticket {
    private final NIC passengerNIC;
    private final String passengerName;
    private final int passengerAge;
    private final Gender passengerGender;
    private final Location from;
    private final Location to;
    private final double price;
    private final String seatNo;

    public Ticket(Passenger passenger, Location from, Location to, double price, String seatNo) {
        passengerNIC = passenger.getNic();
        passengerName = passenger.getName();
        passengerAge = passenger.getAge();
        passengerGender = passenger.getGender();
        this.from = from;
        this.to = to;
        this.price = price;
        this.seatNo = seatNo;
    }

    @Override
    public String toString() {
        return "\n--------------- TICKET ---------------" + "\n" +
                "Passenger NIC: " + passengerNIC.toString() + "\n" +
                "Passenger Name: " + passengerName + "\n" +
                "Passenger Age: " + passengerAge + "\n" +
                "Passenger Gender: " + passengerGender + "\n" +
                "From: " + from + "\n" +
                "To: " + to + "\n" +
                "Price: " + price + "\n" +
                "Seat Number: " + seatNo + "\n" +
                "--------------------------------------\n"
                ;

//        System.out.println("\n--------------- TICKET ---------------");
//        System.out.println("Passenger NIC: " + ticket.getPassengerNIC().toString());
//        System.out.println("Passenger Name: " + ticket.getPassengerName());
//        System.out.println("From: " + ticket.getFrom());
//        System.out.println("To: " + ticket.getTo());
//        System.out.println("Seat Number: " + ticket.getSeatNo());
//        System.out.println("Price: " + ticket.getPrice());
//        System.out.println("--------------------------------------\n");
    }

    public NIC getPassengerNIC() {
        return passengerNIC;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public Location getFrom() {
        return from;
    }

    public Location getTo() {
        return to;
    }

    public double getPrice() {
        return price;
    }

    public String getSeatNo() {
        return seatNo;
    }

}
