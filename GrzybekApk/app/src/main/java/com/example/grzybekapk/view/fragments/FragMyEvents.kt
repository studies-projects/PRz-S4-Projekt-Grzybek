package com.example.grzybekapk.view.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.crashlytics.android.Crashlytics
import com.example.grzybekapk.R
import com.example.grzybekapk.view.activities.EventDetailsActivity
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_my_events.*
import java.text.SimpleDateFormat
import java.util.*

class FragMyEvents: Fragment() {
    override fun onAttach(context: Context?) {
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        (activity as AppCompatActivity).supportActionBar?.setTitle("Moje wydarzenia")
        return inflater!!.inflate(R.layout.fragment_my_events, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val eventNameArray = ArrayList<String>() // Array for event names from database
        val organizerNameArray = ArrayList<String>() // Array for event organizers from database
        val eventDescriptionArray = ArrayList<String>() // Array for event description from database
        val eventDateArray = ArrayList<String>()   // Array for event date from database
        val eventsArray = ArrayList<String>()   // Array for event name and date for list view
        val arrayAdapter = ArrayAdapter(activity,android.R.layout.simple_list_item_1,eventsArray)
        myeventsListView.adapter = arrayAdapter

        val local = Locale("pol") // localize date
        val dateFormat  = SimpleDateFormat("EEE dd'.' MMM yyyy 'o' HH:mm",local) // long date format
        val dateFormat2  = SimpleDateFormat("dd.MM.YYYY HH:mm",local) // short date format

        // Access a Cloud Firestore instance from your Activity
        val db = FirebaseFirestore.getInstance()

        db.collection("Events")
            .whereEqualTo("Owner", FirebaseAuth.getInstance().currentUser!!.uid)
            .orderBy("DateStart")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    eventNameArray.add(document.data["Name"] as String)
                    organizerNameArray.add(document.data["Owner"] as String)
                    eventDescriptionArray.add(document.data["Desc"] as String)
                    val date : Timestamp = document.data["DateStart"] as Timestamp // save timestamp
                    eventDateArray.add( dateFormat.format(date.toDate()) as String) // timestamp to date to string
                    eventsArray.add(dateFormat2.format(date.toDate()) as String + "    " + document.data["Name"] as String)
                    arrayAdapter.notifyDataSetChanged()

                    Log.d(Crashlytics.TAG, "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w(Crashlytics.TAG, "Error getting documents: ", exception)
            }

        myeventsListView.onItemClickListener = AdapterView.OnItemClickListener{adapterView,view,i,l->
            val intent = Intent(activity,EventDetailsActivity::class.java)
            intent.putExtra("name",eventNameArray[i])
            intent.putExtra("description",eventDescriptionArray[i])
            intent.putExtra("organizer",organizerNameArray[i])
            intent.putExtra("date",eventDateArray[i])
            startActivity(intent)
        }
    }
}
