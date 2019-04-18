package com.example.grzybekapk.view.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.grzybekapk.R
import kotlinx.android.synthetic.main.activity_event_details.*

class EventDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_details)

        val eventName = intent.getStringExtra("name")
        val eventOrganizer = intent.getStringExtra("organizer")
        val eventDescription = intent.getStringExtra("description")
        //val eventDate = intent.getStringExtra("date")    // to dodałem

        findViewById<TextView>(R.id.eventName).text = eventName
        findViewById<TextView>(R.id.eventOrganizer).text = eventOrganizer
        findViewById<TextView>(R.id.eventDescription).text = eventDescription
        //findViewById<TextView>(R.id.eventDate).text = eventDate   // to dodałem
    }
}