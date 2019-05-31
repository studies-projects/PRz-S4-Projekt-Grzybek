package com.example.grzybekapk.view.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.crashlytics.android.Crashlytics
import com.example.grzybekapk.R
import com.example.grzybekapk.view.DataForEvents
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_event_details.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class EventDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Get value from Counts in document
        fun setCounter(docRef: DocumentReference, field : TextView) {
            //var r = "69"
            docRef.get()
                .addOnSuccessListener { document ->
                        document.data!!["Counts"]?.let{
                            //r = it.toString()
                            field.text = it.toString()
                       }
                }
                .addOnFailureListener{
                    Log.d(Crashlytics.TAG, it.toString())
                }
            //return "213123"
        }


        setContentView(R.layout.activity_event_details)

        val local = Locale("pol")

        var event:DataForEvents =  intent.getParcelableExtra("event")

        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("Events").document(event.id)
        val user = FirebaseAuth.getInstance().currentUser
        val uid = user?.uid.toString()

        eventName.text = event.nameOfEvent
        eventOrganizer.text = event.organizer
        eventDescription.text = event.descriptionOfEvent
        eventDate.text =  SimpleDateFormat("EEE dd'.' MMM yyyy 'o' HH:mm", local).format(event.date.time)
        setCounter(docRef, eventCounter)

        event_button_going.setOnClickListener {
            val data = HashMap<String, Any>()
            data["timestamp"] = FieldValue.serverTimestamp()
            docRef
                .collection("Participants")
                .document(uid).set(data)

            docRef
                .update("Counts", FieldValue.increment(1))
                .addOnSuccessListener {
                    setCounter(docRef, eventCounter)
                }
            Toast.makeText(this@EventDetailsActivity, "CLICKED", Toast.LENGTH_SHORT).show()
        }
    }


}