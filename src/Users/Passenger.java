package Users;

import Ticket.Ticket;
import TicketMachine.ServiceTicketMachine;
import Utility.Gender;
import Utility.NIC;

public class Passenger implements Runnable {

    private final NIC nic;
    private final String name;
    private int age;
    private Gender gender;
    private String phoneNumber;
    private String email;
    private Ticket ticket;
    private final ServiceTicketMachine ticketMachine;


    public Passenger(NIC nic, String name, String phoneNumber, String email, ServiceTicketMachine ticketMachine) {
        this.nic = nic;
        this.name = name;
        age = nic.getAge();
        gender = nic.getGender();
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.ticketMachine = ticketMachine;
    }

    @Override
    public void run() {
        ticketMachine.print(ticket);
    }

    public NIC getNic() {
        return nic;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public Gender getGender() {
        return gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
