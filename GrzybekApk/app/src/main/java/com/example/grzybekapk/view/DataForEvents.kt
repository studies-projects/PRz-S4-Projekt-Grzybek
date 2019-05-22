package com.example.grzybekapk.view


import java.text.SimpleDateFormat
import java.util.*

data class DataForEvents(
    val nameOfEvent: String,
    val descriptionOfEvent: String,
    val date: Calendar,
    val organizer: String
){
    fun getDate() = SimpleDateFormat("dd-MM-yyyy", Locale.US).format(date.time)
    fun getHour() = SimpleDateFormat("HH:mm", Locale.US).format(date.time)
}
