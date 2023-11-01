package com.example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {
    private List<User> users;
    private List<Goal> goals;
    private List<Booking> bookings;

    public App(List<User> users, List<Goal> goals, List<Booking> bookings) {
        this.users = users;
        this.goals = goals;
        this.bookings = bookings;
    }

    public void displayAllGoals() {
        for (Goal goal : goals) {
            System.out.println(goal);
        }
    }

    public void setExistingGoal() {
        Scanner scanner = new Scanner(System.in);
    
        System.out.print("Enter the Goal ID you want to set: ");
        int goalIDToSet;
        if (scanner.hasNextInt()) {
            goalIDToSet = scanner.nextInt();
            scanner.nextLine();
        } else {
            System.out.println("Invalid Goal ID. Please enter a valid integer.");
            return;
        }
    
        Goal goalToSet = null;
        for (Goal goal : goals) {
            if (goal.getGoalID() == goalIDToSet && goal.isAvailable()) {
                goalToSet = goal;
                break;
            }
        }
    
        if (goalToSet == null) {
            System.out.println("Goal not found or not available. Please enter a valid Goal ID.");
            return;
        }
    
        System.out.println("Goal found:");
        System.out.println(goalToSet);
    
        System.out.print("Enter the User ID: ");
        int userID;
        if (scanner.hasNextInt()) {
            userID = scanner.nextInt();
            scanner.nextLine();
        } else {
            System.out.println("Invalid User ID. Please enter a valid integer.");
            return;
        }
    
        System.out.print("Enter the start date (dd/MM/yyyy): ");
        String startDateInput = scanner.nextLine();
    
        LocalDate startDate = null;
        try {
            startDate = LocalDate.parse(startDateInput, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Please use dd/MM/yyyy.");
            return;
        }
    
        LocalDateTime startDateTime = startDate.atStartOfDay();
    
        System.out.print("Enter the end date (dd/MM/yyyy): ");
        String endDateInput = scanner.nextLine();
    
        LocalDate endDate = null;
        try {
            endDate = LocalDate.parse(endDateInput, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Please use dd/MM/yyyy.");
            return;
        }
    
        LocalDateTime endDateTime = endDate.atStartOfDay();
    
        if (hasConflictingBooking(userID, goalIDToSet, startDateTime, endDateTime)) {
            System.out.println("User already has a conflicting booking for the same goal.");
            return;
        }
    
        int newBookingID = generateNewBookingID();
    
        Booking newBooking = new Booking(newBookingID, userID, goalIDToSet, startDateTime, endDateTime);
        bookings.add(newBooking);
    
        goalToSet.setAvailable(false);
    
        updatePrenotazioniFile(newBooking);

        goalToSet.setAvailable(false);

        updateDisponibilitaObiettivi(goalIDToSet, false); 
        System.out.println("Goal updated to unavailable.");
    
        System.out.println("Booking created successfully.");
    }
    
    private boolean hasConflictingBooking(int userID, int goalID, LocalDateTime startDate, LocalDateTime endDate) {
        for (Booking booking : bookings) {
            if (booking.getUserID() == userID && booking.getGoalID() == goalID) {
                LocalDateTime existingStart = booking.getStartTime();
                LocalDateTime existingEnd = booking.getEndTime();
                if (startDate.isBefore(existingEnd) && endDate.isAfter(existingStart)) {
                    return true;
                }
            }
        }
        return false;
    }    

    private int generateNewBookingID() {
        int maxBookingID = 0;

    try {
        String fileName = "src/main/resources/prenotazioni.csv";
        File file = Paths.get(fileName).toFile();
        Scanner fileScanner = new Scanner(file);

        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine();
            String[] parts = line.split(";");
            if (parts.length > 0) {
                try {
                    int bookingID = Integer.parseInt(parts[0]);
                    if (bookingID > maxBookingID) {
                        maxBookingID = bookingID;
                    }
                } catch (NumberFormatException e) {
                    
                }
            }
        }
        fileScanner.close();
    } catch (IOException e) {
        e.printStackTrace();
        System.out.println("An error occurred while reading the prenotazioni.csv file.");
    }

    return maxBookingID + 1;
    }

    private void updatePrenotazioniFile(Booking newBooking) {
        try {
            String filePath = "src/main/resources/prenotazioni.csv";
            FileWriter writer = new FileWriter(filePath, true);
    
            String line = String.format(
                "%d;%d;%d;%s;%s%n",
                newBooking.getBookingID(),
                newBooking.getGoalID(),
                newBooking.getUserID(),
                newBooking.getStartTime().toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                newBooking.getEndTime().toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            );
    
            writer.write(line);
    
            writer.close();
    
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("An error occurred while updating the prenotazioni.csv file.");
        }
    }

    private void updateDisponibilitaObiettivi(int goalID, boolean isAvailable) {
    
        try {
            String filePath = "src/main/resources/obiettivi.csv";
            File file = Paths.get(filePath).toFile();
            List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
    
            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i);
                String[] parts = line.split(";");
                
                if (parts.length >= 6) {
                    int currentGoalID = Integer.parseInt(parts[0]);
                    
                    if (currentGoalID == goalID) {
                        parts[5] = isAvailable ? "SI" : "NO";
                        lines.set(i, String.join(";", parts));
                    }
                }
            }
    
            Files.write(file.toPath(), lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("An error occurred while updating the obiettivi.csv file.");
        }
    }

    public void cancelBooking() {
        Scanner scanner = new Scanner(System.in);
    
        System.out.println("Cancel a Booking");
        System.out.print("Enter the Booking ID you want to cancel: ");
    
        if (scanner.hasNextInt()) {
            int bookingIDToCancel = scanner.nextInt();
            scanner.nextLine();
    
            List<String> lines = new ArrayList<>();
            boolean bookingFound = false;
            Goal associatedGoal = null; 
    
            try {
                String filePath = "src/main/resources/prenotazioni.csv";
                File file = Paths.get(filePath).toFile();
                Scanner fileScanner = new Scanner(file);
    
                while (fileScanner.hasNextLine()) {
                    String line = fileScanner.nextLine();
                    String[] parts = line.split(";");
                    if (parts.length >= 2) {
                        try {
                            int bookingID = Integer.parseInt(parts[0]);
                            int goalID = Integer.parseInt(parts[1]);
                            if (bookingID == bookingIDToCancel) {
                                bookingFound = true;
                                associatedGoal = findGoalByID(goalID);
                            } else {
                                lines.add(line);
                            }
                        } catch (NumberFormatException e) {
                            System.err.println("Skipping line with invalid integer values: " + line);
                        }
                    } else {
                        System.err.println("Skipping line with insufficient data: " + line);
                    }
                }
                fileScanner.close();
    
                if (!bookingFound) {
                    System.out.println("Booking not found. Please enter a valid Booking ID.");
                    return;
                }
    
                System.out.print("Are you sure you want to cancel this booking? (Y/N): ");
                String confirmation = scanner.nextLine();
                if (confirmation.equalsIgnoreCase("Y")) {
                    FileWriter writer = new FileWriter(filePath, false);
    
                    for (String line : lines) {
                        writer.write(line + "\n");
                    }
                    writer.close();
    
                    System.out.println("Booking canceled successfully.");
    
                    if (associatedGoal != null) {
                        associatedGoal.setAvailable(true);
                        updateDisponibilitaObiettivi(associatedGoal.getGoalID(), true); 
                        System.out.println("Goal updated to available.");
                    }
                } else {
                    System.out.println("Booking cancellation canceled.");
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("An error occurred while canceling the booking.");
            }
        } else {
            System.out.println("Invalid Booking ID. Please enter a valid integer.");
        }
    }

    public Goal findGoalByID(int goalID) {
        for (Goal goal : goals) {
            if (goal.getGoalID() == goalID) {
                return goal; 
            }
        }
        return null; 
    }

        public void addNewUser() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Add a New User");

        int newUserID = generateNewUserID();
        
        String firstName, lastName, inputBirthDate, address, documentID;
        LocalDate birthDate = null;
        int coins = 0;

        while (true) {
            System.out.print("Enter the first name: ");
            firstName = scanner.nextLine();
            if (!firstName.isEmpty()) {
                break;
            }
            System.out.println("First name cannot be empty. Please try again.");
        }

        while (true) {
            System.out.print("Enter the last name: ");
            lastName = scanner.nextLine();
            if (!lastName.isEmpty()) {
                break;
            }
            System.out.println("Last name cannot be empty. Please try again.");
        }

        while (true) {
            System.out.print("Enter the birthday date (dd-MM-yyyy): ");
            inputBirthDate = scanner.nextLine();
            if (!inputBirthDate.isEmpty()) {
                try {
                    birthDate = LocalDate.parse(inputBirthDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                    break;
                } catch (DateTimeParseException e) {
                    System.out.println("Invalid date format. Please use dd-MM-yyyy.");
                }
            } else {
                System.out.println("Birthday date cannot be empty. Please try again.");
            }
        }

        while (true) {
            System.out.print("Enter the address: ");
            address = scanner.nextLine();
            if (!address.isEmpty()) {
                break;
            }
            System.out.println("Address cannot be empty. Please try again.");
        }

        while (true) {
            System.out.print("Enter the document ID: ");
            documentID = scanner.nextLine();
            if (!documentID.isEmpty()) {
                break;
            }
            System.out.println("Document ID cannot be empty. Please try again.");
        }

        while (true) {
            System.out.print("Enter the initial number of coins: ");
            String coinsInput = scanner.nextLine();
            if (!coinsInput.isEmpty()) {
                try {
                    coins = Integer.parseInt(coinsInput);
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid coins amount. Please enter a valid number.");
                }
            } else {
                System.out.println("Coins amount cannot be empty. Please try again.");
            }
        }

        User newUser = new User(newUserID, firstName, lastName, birthDate, address, documentID, coins);
        users.add(newUser);

        try {
            String filePath = "src/main/resources/utenti.csv";
            FileWriter writer = new FileWriter(filePath, true);

            writer.write(
                newUser.getUserID() + ";" +
                newUser.getFirstName() + ";" +
                newUser.getLastName() + ";" +
                newUser.getBirthDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + ";" +
                newUser.getAddress() + ";" +
                newUser.getDocumentID() + ";" +
                newUser.getCoins() + "\n"
            );

            writer.close();

            System.out.println("User added successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("An error occurred while adding the user.");
        }
    }
    

    private int generateNewUserID() {
        int maxUserID = 0;
        for (User user : users) {
            if (user.getUserID() > maxUserID) {
                maxUserID = user.getUserID();
            }
        }
        
        return maxUserID + 1;
    }

    public void exportAvailableGoals(String filename) {
        try {
            FileWriter writer = new FileWriter(filename);
    
            writer.write("Goal ID,Name,Description,Type,Duration,Coins,Available\n");
    
            for (Goal goal : goals) {
                if (goal.isAvailable()) {
                    writer.write(
                        goal.getGoalID() + "," +
                        goal.getName() + "," +
                        goal.getDescription() + "," +
                        goal.getType() + "," +
                        goal.getDuration() + "," +
                        goal.getCoins() + "," +
                        "SI\n"
                    );
                }
            }
    
            writer.close();
    
            System.out.println("Available goals exported to " + filename + " successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("An error occurred while exporting goals.");
        }
    }    
}