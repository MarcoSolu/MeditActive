package com.example;

import java.time.LocalDate;

public class User {
    private int userID;
    private String firstName;
    private String lastName;
    LocalDate birthDate;
    String address;
    String documentID;
    private int coins;

    public User(int userID, String firstName, String lastName, LocalDate birthDate, String address, String documentID, int coins){
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.address = address;
        this.documentID = documentID;
        this.coins = coins;
    }

    public int getUserID() {
        return userID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDocumentID() {
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    @Override
    public String toString() {
        return "Utente{" + "ID Utente=" + userID + ", Nome='" + firstName + '\'' + ", Cognome='" + lastName + '\'' +  ", Data di nascita= " + birthDate + ", Indirizzo= " + address + ", ID Documento= " + documentID + ", Coins=" + coins + '}';
    }
}
