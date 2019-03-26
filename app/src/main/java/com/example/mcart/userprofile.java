package com.example.mcart;

import android.net.Uri;

public class userprofile {

    public String username;
    public String email;
    public String hostel;
    public String room;
    public String contact;


    public userprofile(String username, String email, String hostel, String room, String contact) {
        this.username = username;
        this.email = email;
        this.hostel = hostel;
        this.room = room;
        this.contact = contact;

    }

    public userprofile() {
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHostel() {
        return hostel;
    }

    public void setHostel(String hostel) {
        this.hostel = hostel;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
