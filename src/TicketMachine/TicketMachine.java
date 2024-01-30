package TicketMachine;

import Ticket.Ticket;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class TicketMachine implements ServiceTicketMachine {
    private volatile int paper;
    private volatile int toner;

    private final int MAX_PAPER_LEVEL = 100;
    private final int MAX_TONER_LEVEL = 60;
    private final int MIN_PAPER_LEVEL = 10;
    private final int MIN_TONER_LEVEL = 10;

    private final int MAX_REFILLS = 3;

    private volatile int totalPrintouts = 0;
    private volatile int noPaperRefill = 0;
    private volatile int noTonerRefill = 0;

    private final ReentrantLock paperRefillLock = new ReentrantLock();
    private final ReentrantLock tonerRefillLock = new ReentrantLock();

    private final Condition hasPaper = paperRefillLock.newCondition();
    private final Condition hasToner = tonerRefillLock.newCondition();


    public TicketMachine(int paper, int toner) throws IllegalPrinterArgumentException {
        if (paper < 0 || toner < 0)
            throw new IllegalPrinterArgumentException("Paper or toner can not be negative");

        this.paper = Math.min(paper, MAX_PAPER_LEVEL);
        this.toner = Math.min(toner, MAX_TONER_LEVEL);
    }

    @Override
    public void print(Ticket ticket) {
        // checking if both locks are available
        // before acquiring
        while (tonerRefillLock.isLocked() || paperRefillLock.isLocked()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        paperRefillLock.lock();
        tonerRefillLock.lock();

        try {
            // fetch current thread's name
            String threadName = Thread.currentThread().getName();

            // check for resources
            while (!hasSufficientResources()) {
                System.out.println("\nInsufficient Resources!");
                System.out.println("Paper: " + paper);
                System.out.println("Ink: " + toner);
                System.out.println("Passenger " + threadName + " waiting for refill");

                try {
                    // return when number of refills reach max and no available stocks
                    // from said resource
                    if (paper < 1 && getNoPaperRefill() >= MAX_REFILLS) {
                        System.out.println("Passenger " + threadName + " couldn't print. Paper Tech OUT of service\n");
                        tonerRefillLock.unlock();
                        paperRefillLock.unlock();
                        return;
                    }

                    // check if the technician available before calling await
                    if (getNoPaperRefill() < MAX_REFILLS) {
                        hasPaper.await();
                    }

                    // return when number of refills reach max and no available stocks
                    // from said resource
                    if (toner < 5 && getNoTonerRefill() >= MAX_REFILLS) {
                        System.out.println("Passenger " + threadName + " couldn't print. Toner Tech OUT of Service\n");
                        tonerRefillLock.unlock();
                        paperRefillLock.unlock();
                        return;
                    }

                    // check if the technician available before calling await
                    if (getNoTonerRefill() < MAX_REFILLS) {
                        hasToner.await();
                    }

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            // display the ticket
            System.out.println(ticket);

            // consume resources
            paper -= 1;
            toner -= 5;

            // increment the number of prints
            totalPrintouts++;

        } finally {
            // release the locks
            if (paperRefillLock.isHeldByCurrentThread()) {
                paperRefillLock.unlock();
            }
            if (tonerRefillLock.isHeldByCurrentThread()) {
                tonerRefillLock.unlock();
            }
        }
    }


    @Override
    public void refillPaper(int amount) throws IllegalPrinterArgumentException {
        // check for illegal arguments
        if (amount <= 0)
            throw new IllegalPrinterArgumentException("Paper amount cannot be negative");

        // acquire the lock
        paperRefillLock.lock();

        // return if enough stocks are available already
        if (paper > MIN_PAPER_LEVEL) {
            hasPaper.signalAll();
            paperRefillLock.unlock();
            return;
        }

        // increment stocks
        paper = Math.min(paper+amount, MAX_PAPER_LEVEL);
        noPaperRefill ++;

        // notify user
        System.out.println("Paper Refilled");

        // signal other threads and release the lock
        hasPaper.signalAll();
        paperRefillLock.unlock();
    }

    @Override
    public  void refillToner(int amount) throws IllegalPrinterArgumentException {
        // check for illegal arguments
        if (amount <= 0)
            throw new IllegalPrinterArgumentException("Refill amount cannot be negative");

        // acquire the lock
        tonerRefillLock.lock();

        // return if enough stocks are available already
        if (toner > MIN_TONER_LEVEL) {
            hasToner.signal();
            tonerRefillLock.unlock();
            return;
        }

        // increment stocks
        toner = Math.min(toner+amount, MAX_TONER_LEVEL);
        noTonerRefill ++;

        // notify user
        System.out.println("Toner Refilled");

        // signal other threads and release the lock
        hasToner.signal();
        tonerRefillLock.unlock();
    }

    @Override
    public void printSummary() {
        System.out.println("------------ SUMMARY ------------");
        System.out.println(totalPrintouts + " tickets were printed in total.");
        System.out.println("Paper was replaced " + noPaperRefill + " time(s).");
        System.out.println("Toner was replaced " + noTonerRefill + " time(s).");
        System.out.println("---------------------------------");
    }

    private synchronized boolean hasSufficientResources() {
        return paper >= 1 && toner >= 5;
    }

    public int getNoPaperRefill() {
        return noPaperRefill;
    }

    public int getNoTonerRefill() {
        return noTonerRefill;
    }

    public int getMAX_REFILLS() {
        return MAX_REFILLS;
    }
}
