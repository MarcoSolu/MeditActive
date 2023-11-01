package com.example;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ArrayList<User> users = Loader.loadUsers("utenti.csv");
        ArrayList<Goal> goals = Loader.loadGoals("obiettivi.csv");
        ArrayList<Booking> bookings = Loader.loadBookings("prenotazioni.csv");

        App app = new App(users, goals, bookings);

        Scanner scanner = new Scanner(System.in);

        boolean exit = false;
        while (!exit) {
            System.out.println("Select an operation:");
            System.out.println("1. Display all goals");
            System.out.println("2. Set an existing goal");
            System.out.println("3. Cancel a booking");
            System.out.println("4. Add a new user");
            System.out.println("5. Export available goals to a CSV file");
            System.out.println("0. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    app.displayAllGoals();
                    break;
                case 2:
                    app.setExistingGoal();
                    break;
                case 3:
                    app.cancelBooking();
                    break;
                case 4:
                    app.addNewUser();
                    break;
                case 5:
                    app.exportAvailableGoals("obiettivi_disponibili.csv");
                    break;
                case 0:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please select a valid operation.");
            }
        }

        scanner.close();
    }
}