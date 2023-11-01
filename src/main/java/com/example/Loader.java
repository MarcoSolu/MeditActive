package com.example;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class Loader {
    public static ArrayList<User> loadUsers(String filename) {
        ArrayList<User> users = new ArrayList<>();

        try (InputStream inputStream = Loader.class.getClassLoader().getResourceAsStream(filename);
             Scanner scanner = new Scanner(inputStream)) {

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(";");
                if (parts.length == 7) {
                    String userIDStr = parts[0];
                    String coinsStr = parts[6];
                    try {
                        int userID = Integer.parseInt(userIDStr);
                        int coins = Integer.parseInt(coinsStr);

                        String firstName = parts[1];
                        String lastName = parts[2];

                        String dateStr = parts[3];
                        LocalDate birthDate = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

                        String address = parts[4];
                        String documentID = parts[5];

                        User user = new User(userID, firstName, lastName, birthDate, address, documentID, coins);
                        users.add(user);
                    } catch (NumberFormatException | DateTimeParseException e) {
                        System.err.println("Invalid userID, coins, or birthDate: " + userIDStr + ", " + coinsStr + ", " + parts[3]);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return users;
    }

    public static ArrayList<Goal> loadGoals(String filename) {
        ArrayList<Goal> goals = new ArrayList();

        try (InputStream inputStream = Loader.class.getClassLoader().getResourceAsStream(filename);
             Scanner scanner = new Scanner(inputStream)) {

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(";");
                if (parts.length == 7) {
                    String goalIDStr = parts[0];
                    try {
                        int goalID = Integer.parseInt(goalIDStr);
                        String name = parts[1];
                        String description = parts[2];
                        String type = parts[3];
                        int duration = Integer.parseInt(parts[4]);
                        boolean available = parts[5].equalsIgnoreCase("SI");
                        int coins = Integer.parseInt(parts[6]);
                        Goal goal = new Goal(goalID, name, description, type, duration, available, coins);
                        goals.add(goal);
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid goalID: " + goalIDStr);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return goals;
    }

    public static ArrayList<Booking> loadBookings(String filename) {
        ArrayList<Booking> bookings = new ArrayList<>();

        try (InputStream inputStream = Loader.class.getClassLoader().getResourceAsStream(filename);
             Scanner scanner = new Scanner(inputStream)) {

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(";");
                if (parts.length == 5) {
                    String bookingIDStr = parts[0];
                    String goalIDStr = parts[1];
                    String userIDStr = parts[2];
                    String startTimeStr = parts[3];
                    String endTimeStr = parts[4];
                    try {
                        int bookingID = Integer.parseInt(bookingIDStr);
                        int goalID = Integer.parseInt(goalIDStr);
                        int userID = Integer.parseInt(userIDStr);

                        LocalDateTime startTime = LocalDateTime.parse(startTimeStr, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
                        LocalDateTime endTime = LocalDateTime.parse(endTimeStr, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

                        Booking booking = new Booking(bookingID, userID, goalID, startTime, endTime);
                        bookings.add(booking);
                    } catch (NumberFormatException | DateTimeParseException e) {
                        System.err.println("Invalid bookingID, goalID, userID, startTime, or endTime: " +
                                bookingIDStr + ", " + goalIDStr + ", " + userIDStr + ", " + startTimeStr + ", " + endTimeStr);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bookings;
    }
}

