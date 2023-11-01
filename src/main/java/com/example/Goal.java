package com.example;

public class Goal {
    private int goalID;
    private String name;
    private String description;
    private String type;
    private int duration;
    private boolean available;
    private int coins;

    public Goal(int goalID, String name, String description, String type, int duration, boolean available, int coins) {
        this.goalID = goalID;
        this.name = name;
        this.description = description;
        this.type = type;
        this.duration = duration;
        this.available = available;
        this.coins = coins;
    }

    public int getGoalID() {
        return goalID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public String toString() {
        return "Obiettivo{" + "ID obiettivo=" + goalID + ", Nome Utente='" + name + '\'' + ", Descrizione='" + description + '\'' + ", Tipologia=" + type + ", Durata=" + duration + ", Coins=" + coins + ", Disponibilità=" + available + '}';
    }
}
