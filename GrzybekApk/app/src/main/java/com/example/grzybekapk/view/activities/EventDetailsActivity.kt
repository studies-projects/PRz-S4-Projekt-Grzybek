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
            docRef.get()
                .addOnSuccessListener { document ->
                        document.data!!["Counts"]?.let{
                            field.text = it.toString()
                       }
                }
                .addOnFailureListener{
                    Log.d(Crashlytics.TAG, it.toString())
                }
        }

        setContentView(R.layout.activity_event_details)

        val local = Locale("pol")

        var event:DataForEvents =  intent.getParcelableExtra("event")

        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("Events").document(event.id)
        val user = FirebaseAuth.getInstance().currentUser
        val uid = user?.uid.toString()
        var going = false //is user going for event

        eventName.text = event.nameOfEvent
        eventOrganizer.text = event.organizer
        eventDescription.text = event.descriptionOfEvent
        eventDate.text =  SimpleDateFormat("EEE dd'.' MMM yyyy 'o' HH:mm", local).format(event.date.time)
        setCounter(docRef, eventCounter) // setup default number for counter

        // check if user is going for event
        docRef.collection("Participants").document(uid).get()
            .addOnSuccessListener {doc ->
                if(doc.data != null)
                {
                    going = true
                    event_button_going.text = getString(R.string.eventGoingT) //Wypisz sie ...
                    Log.d("DOC", "EXTIST + $going + $doc")
                } else {
                    going = false
                    event_button_going.text = getString(R.string.eventGoingN) // Zapisz sie
                    Log.d("DOC", "NOTEXTIST + $going + $doc")
                }
            }


        event_button_going.setOnClickListener {
            if (going) {
                docRef
                    .collection("Participants")
                    .document(uid).delete()
                    .addOnSuccessListener {
                        going = false
                        event_button_going.text = getString(R.string.eventGoingN)

                        docRef
                            .update("Counts", FieldValue.increment(-1))
                            .addOnSuccessListener {
                                setCounter(docRef, eventCounter)
                            }
                    }
            } else {
                val data = HashMap<String, Any>()
                data["timestamp"] = FieldValue.serverTimestamp()
                docRef
                    .collection("Participants")
                    .document(uid).set(data)
                    .addOnSuccessListener {
                        going = true
                        event_button_going.text = getString(R.string.eventGoingT)

                        docRef
                            .update("Counts", FieldValue.increment(1))
                            .addOnSuccessListener {
                                setCounter(docRef, eventCounter)
                            }
                    }
            }
        }
    }


}