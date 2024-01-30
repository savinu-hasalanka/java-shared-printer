package Users;

import TicketMachine.ServiceTicketMachine;

import java.util.Random;

public abstract class Technician {
    private final String name;
    private final ServiceTicketMachine ticketMachine;

    public Technician(String name, ServiceTicketMachine ticketMachine) {
        this.name = name;
        this.ticketMachine = ticketMachine;
    }

    public String getName() {
        return name;
    }

    public ServiceTicketMachine getTicketMachine() {
        return ticketMachine;
    }
}
