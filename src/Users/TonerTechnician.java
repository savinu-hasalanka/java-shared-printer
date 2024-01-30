package Users;

import TicketMachine.IllegalPrinterArgumentException;
import TicketMachine.ServiceTicketMachine;
import java.util.Random;


public class TonerTechnician extends Technician implements Runnable {
    public TonerTechnician(String name, ServiceTicketMachine ticketMachine) {
        super(name, ticketMachine);
    }

    @Override
    public void run() {
        Random random = new Random();
        int randomTimePeriod;
        while (getTicketMachine().getNoTonerRefill() < 3) {
            try {
                super.getTicketMachine().refillToner(10);
                randomTimePeriod = random.nextInt(5000 - 1000) + 1000;
                Thread.sleep(randomTimePeriod);
            } catch (InterruptedException | IllegalPrinterArgumentException e) {
                throw new RuntimeException(e);
            }
        }
    }
}