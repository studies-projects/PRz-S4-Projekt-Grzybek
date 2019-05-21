package com.example.grzybekapk.view


import java.text.SimpleDateFormat
import java.util.*

/*
    Zmienić na data-class.
    Nic nie robi, tylko daje info developerom co ta klasa robi (przenosi dane).
    No dobra... troche zmienia konstruktor
 */
data class DataForEvents(
    val nameOfEvent: String,
    val descriptionOfEvent: String,
    val date: Calendar,
    val organizer: String
) {
    /*
         Wywalić pola (które zapisujecie w BD) na rzecz funkcji wyliczanych na podstawie pól.
         Nie dojdzie do anomali w postaci modyfikacji pola date, a pozostawieniu starych pól godzin i dnia..
     */
    fun getDate() = SimpleDateFormat("dd-MM-yyyy", Locale.US).format(date.time)
    fun getHour() = SimpleDateFormat("HH:mm", Locale.US).format(date.time)
}
