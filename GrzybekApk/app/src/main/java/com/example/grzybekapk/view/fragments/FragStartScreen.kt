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
import com.example.grzybekapk.view.DataForEvents
import com.example.grzybekapk.view.activities.EventDetailsActivity
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_start_screen.*
import java.text.SimpleDateFormat
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

        val eventsList = ArrayList<DataForEvents>() //Array for DataForEvents objects
        val eventsArray = ArrayList<String>()   // Array for event name and date for list view
        val arrayAdapter = ArrayAdapter(activity,android.R.layout.simple_list_item_1,eventsArray)
        eventsListView.adapter = arrayAdapter

        val local = Locale("pol") // localize date
        val dateFormat  = SimpleDateFormat("EEE dd'.' MMM yyyy 'o' HH:mm",local) // long date format
        val dateFormat2  = SimpleDateFormat("dd.MM HH:mm",local) // short date format

        // Access a Cloud Firestore instance from your Activity
        val db = FirebaseFirestore.getInstance()

        val today = Calendar.getInstance()
        today.add(Calendar.MINUTE,-120) //now - 2 hours
        var futureDate = Calendar.getInstance()
        futureDate.add(Calendar.DATE, 7) // now + 7 days

        db.collection("Events")
            .whereGreaterThanOrEqualTo("DateStart",Timestamp(today.time))//from now - 2 hours
            .whereLessThanOrEqualTo("DateStart",Timestamp(futureDate.time)) //to now + 7 days in the future
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {

                    val date : Timestamp = document.data["DateStart"] as Timestamp
                    val calendar = Calendar.getInstance()
                    calendar.time = date.toDate()
                    var nextEvent = DataForEvents(
                        document.data["Name"] as String,
                        document.data["Desc"] as String,
                        calendar as Calendar,
                        document.data["Owner"] as String
                    )
                    eventsArray.add(dateFormat2.format(date.toDate()) as String + "    " + document.data["Name"] as String)
                    eventsList.add(nextEvent)
                    arrayAdapter.notifyDataSetChanged()

                    Log.d(TAG, "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }

        eventsListView.onItemClickListener = AdapterView.OnItemClickListener{adapterView,view,i,l->
            val intent = Intent(activity,EventDetailsActivity::class.java)
            intent.putExtra("event",eventsList[i])
            startActivity(intent)
        }
    }
}