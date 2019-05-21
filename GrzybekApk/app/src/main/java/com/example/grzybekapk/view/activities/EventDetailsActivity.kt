package com.example.grzybekapk.view.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.grzybekapk.R
import kotlinx.android.synthetic.main.activity_event_details.*

class EventDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_details)
        /*
            Robić tak jak niżej.
            Można z tytułu:
                build.gradle:
                apply plugin: 'kotlin-android-extensions'
         */
        eventName.text = intent.getStringExtra("name")
        eventOrganizer.text = intent.getStringExtra("organizer")
        eventDescription.text = intent.getStringExtra("description")
        eventDate.text = intent.getStringExtra("date")
    }
}