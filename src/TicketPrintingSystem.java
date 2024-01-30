import Data.PassengerData;
import Ticket.Ticket;
import Ticket.TicketGenerator;
import TicketMachine.ServiceTicketMachine;
import TicketMachine.TicketMachine;
import TicketMachine.IllegalPrinterArgumentException;
import Users.PaperTechnician;
import Users.Passenger;
import Users.TonerTechnician;
import Utility.NIC;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TicketPrintingSystem {
    private static final List<Map<String, String>> passengers = PassengerData.PASSENGERS;
    private static final ServiceTicketMachine sharedTicketMachine;

    static {
        try {
            sharedTicketMachine = new TicketMachine(5, 10);
        } catch (IllegalPrinterArgumentException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        // creates a thread group for Passengers
        ThreadGroup passengerThreadsGroup = new ThreadGroup("Passengers");
        ArrayList<Thread> passengerThreadsArray = generatePassengerThreads(passengerThreadsGroup);

        // creates two threads for paper and toner technicians
        Thread paperTechnicianThread = new Thread(new PaperTechnician("Paper", sharedTicketMachine));
        Thread tonerTechnicianThread = new Thread(new TonerTechnician("Toner", sharedTicketMachine));

        // set technician threads to Daemon
        // technician threads must stop when other threads finish
        paperTechnicianThread.setDaemon(true);
        tonerTechnicianThread.setDaemon(true);

        // start technician threads
        paperTechnicianThread.start();
        tonerTechnicianThread.start();

        // start passenger threads
        for (Thread thread: passengerThreadsArray) {
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println("PASSENGERS ARE DONE");
        System.out.println("\n---All threads except main-thread finished their jobs---\n");

        sharedTicketMachine.printSummary();
    }

    private static ArrayList<Thread> generatePassengerThreads(ThreadGroup passengerThreadsGroup) {
        ArrayList<Thread> passengerThreadsArray = new ArrayList<>();
        for (Map<String, String> passenger: passengers) {

            String id = passenger.get("id");
            String name = passenger.get("name");
            String phoneNumber = passenger.get("phoneNumber");
            String email = passenger.get("email");

            Passenger newPassenger = new Passenger(new NIC(id), name, phoneNumber, email, sharedTicketMachine);
            Ticket ticket = TicketGenerator.generateRandomTicket(newPassenger);
            newPassenger.setTicket(ticket);
            passengerThreadsArray.add(new Thread(passengerThreadsGroup, newPassenger, name));
        }
        return passengerThreadsArray;
    }
}