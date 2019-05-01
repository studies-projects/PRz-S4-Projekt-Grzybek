package com.example.grzybekapk.view


import java.text.SimpleDateFormat
import java.util.Calendar

class DataForEvents(
    val nameOfEvent: String,
    val descriptionOfEvent: String,
    val date: Calendar,
    val organizer: String
) {
    val dateOfEventTxt: String
    val timeOfEventTxt: String

    init {
        this.dateOfEventTxt = SimpleDateFormat("dd-MM-yyyy").format(date.time)
        this.timeOfEventTxt = SimpleDateFormat("HH:mm").format(date.time)
    }
}
