package TicketMachine;

import Ticket.Ticket;

public interface ServiceTicketMachine {
    void print(Ticket ticket);
    void refillPaper(int amount) throws IllegalPrinterArgumentException;
    void refillToner(int amount) throws IllegalPrinterArgumentException;
    void printSummary();
    int getNoPaperRefill();
    int getNoTonerRefill();

    int getMAX_REFILLS();
}
