package com.example.grzybekapk.view.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.grzybekapk.R
import com.example.grzybekapk.view.DataForEvents
import kotlinx.android.synthetic.main.activity_event_details.*
import java.text.SimpleDateFormat
import java.util.*

class EventDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_details)

        val local = Locale("pol")

        var event:DataForEvents =  intent.getParcelableExtra("event")

        eventName.text = event.nameOfEvent
        eventOrganizer.text = event.organizer
        eventDescription.text = event.descriptionOfEvent
        eventDate.text =  SimpleDateFormat("EEE dd'.' MMM yyyy 'o' HH:mm", local).format(event.date.time)
    }
}