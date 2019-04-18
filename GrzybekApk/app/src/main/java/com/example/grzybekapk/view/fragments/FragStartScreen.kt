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
import com.crashlytics.android.Crashlytics.TAG
import com.example.grzybekapk.R
import com.example.grzybekapk.view.activities.EventDetailsActivity
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_start_screen.*
import java.util.*

class FragStartScreen : Fragment(){
    override fun onAttach(context: Context?) {
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        (activity as AppCompatActivity).supportActionBar?.setTitle("Najbliższe wydarzenia")
        return inflater!!.inflate(R.layout.fragment_start_screen, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val eventNameArray = ArrayList<String>() // Array for event names from database
        val organizerNameArray = ArrayList<String>() // Array for event organizers from database
        val eventDescriptionArray = ArrayList<String>() // Array for event description from database
        val eventDateArray = ArrayList<String>()   // Array for event date from database ???
        val arrayAdapter = ArrayAdapter(activity,android.R.layout.simple_list_item_1,eventNameArray)
        eventsListView.adapter = arrayAdapter

        var date = Calendar.getInstance()
        date.add(Calendar.DATE, 7)

        // Access a Cloud Firestore instance from your Activity
        val db = FirebaseFirestore.getInstance()

        db.collection("Events")
            .whereGreaterThanOrEqualTo("DateStart",Timestamp.now()) //from today
            .whereLessThanOrEqualTo("DateStart",Timestamp(date.time)) //to 7 days in the future
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    eventNameArray.add(document.data["Name"] as String)
                    organizerNameArray.add(document.data["Owner"] as String)
                    eventDescriptionArray.add(document.data["Desc"] as String)
                    arrayAdapter.notifyDataSetChanged()
                    Log.d(TAG, "${document.id} => ${document.data["Name"] }, ${eventNameArray}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }

        eventsListView.onItemClickListener = AdapterView.OnItemClickListener{adapterView,view,i,l->
            val intent = Intent(activity,EventDetailsActivity::class.java)
            intent.putExtra("name",eventNameArray[i])
            intent.putExtra("description",eventDescriptionArray[i])
            intent.putExtra("organizer",organizerNameArray[i])
            //intent.putExtra("date",eventDateArray[i])                  // to dodałem
            startActivity(intent)
        }
    }
}