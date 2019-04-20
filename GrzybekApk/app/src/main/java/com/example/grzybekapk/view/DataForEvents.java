package com.example.grzybekapk.view;


import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DataForEvents {
    private String nameOfEvent;
    private String descriptionOfEvent;
    private String dateOfEventTxt;
    private String timeOfEventTxt;
    private String organizer;
    private Calendar date;

    public DataForEvents(String nameOfEvent, String descriptionOfEvent, Calendar date, String organizer){
        this.nameOfEvent = nameOfEvent;
        this.date = date;
        this.descriptionOfEvent = descriptionOfEvent;
        this.dateOfEventTxt = new SimpleDateFormat("dd-MM-yyyy").format(date.getTime());
        this.timeOfEventTxt = new SimpleDateFormat("HH:mm").format(date.getTime());
        this.organizer = organizer;
    }

    public String getNameOfEvent() {
        return nameOfEvent;
    }

    public String getDescriptionOfEvent() {
        return descriptionOfEvent;
    }

    public String getDateOfEventTxt() {
        return dateOfEventTxt;
    }

    public String getOrganizer() {
        return organizer;
    }

    public String getTimeOfEventTxt() {
        return timeOfEventTxt;
    }

    public Calendar getDate(){return date;}
}
