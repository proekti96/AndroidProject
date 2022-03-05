package com.example.cultureevents.models;

public class Event {
    String name;
    String description;
    String location;
    String date;
    String time;
    String image;
    String creationDate;
    Boolean neededTicket;

    public Event() {
    }


    public Event(String name, String description, String location, String date, String time, String creationDate, String image, Boolean neededTicket) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.date = date;
        this.time=time;
        this.image=image;
        this.creationDate=creationDate;
        this.neededTicket = neededTicket;
    }

    public String getImage() {
        return image;
    }

    public String getTime() {
        return time;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public String getDate() {
        return date;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public Boolean getNeededTicket() {
        return neededTicket;
    }
}
