package com.example.waitinglist.Models;

public class Guest {
    private String guestName;
    private int partySize;

    public Guest(String guestName, int partySize) {
        this.guestName = guestName;
        this.partySize = partySize;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public int getPartySize() {
        return partySize;
    }

    public void setPartySize(int partySize) {
        this.partySize = partySize;
    }
}
