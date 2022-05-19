package edu.kau.fcit.cpit252.transactions;

import java.util.Scanner;

public class Balance extends Transaction{
    private double balance;

    public Balance(double balance) {
        this.balance = balance;
    }

   
    public boolean showDialog() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Balance Inquiry? (y|n) ");
        String answer = scanner.next();
        if (answer.contains("y")) {
            return true;
        }
        return false;
    }


    public boolean perform(User u) {
        simulateNetworkLatency();
        System.out.println("Balance: SAR " + this.balance);
        return true;
    }

   protected void simulateNetworkLatency() {
        try {
            System.out.println();
            for (int i = 0; i < 10; i++) {
                System.out.print(".");
                Thread.sleep(500);
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    public void closeDialog() {
        System.out.println("Closing the balance inquiry dialog.");
    }
}
