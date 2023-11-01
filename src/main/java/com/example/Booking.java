package com.example;

import java.time.LocalDateTime;

public class Booking {
    private int bookingID;
    private int userID;
    private int goalID;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public Booking(int bookingID, int userID, int goalID, LocalDateTime startTime, LocalDateTime endTime) {
        this.bookingID = bookingID;
        this.userID = userID;
        this.goalID = goalID;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getBookingID() {
        return bookingID;
    }

    public void setBookingID(int bookingID) {
        this.bookingID = bookingID;
    }

    public int getUserID() {
        return userID;
    }

    public int getGoalID() {
        return goalID;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "Prenotazione{" + "ID Prenotazione=" + bookingID + ", ID Utente=" + userID + ", ID Obiettivo=" + goalID + ", Inizio=" + startTime + ", Fine=" + endTime + '}';
    }
}
