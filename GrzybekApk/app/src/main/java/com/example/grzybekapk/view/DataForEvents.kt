package com.example.grzybekapk.view


import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.text.SimpleDateFormat
import java.util.*

@Parcelize
data class DataForEvents(
    val nameOfEvent: String,
    val descriptionOfEvent: String,
    val date: Calendar,
    val organizer: String
) : Parcelable {
    fun getDate() = SimpleDateFormat("dd-MMM-yyyy", Locale.US).format(date.time)
    fun getHour() = SimpleDateFormat("HH:mm", Locale.US).format(date.time)
}
