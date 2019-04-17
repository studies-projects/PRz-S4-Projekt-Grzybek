package com.example.grzybekapk.view.fragments

<<<<<<< HEAD
import android.app.Activity
import android.content.Context
import android.content.Intent
=======
>>>>>>> apkaBranch
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
<<<<<<< HEAD
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import com.example.grzybekapk.R
import com.example.grzybekapk.view.activities.EventDetailsActivity
import com.example.grzybekapk.view.activities.MainScreen
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_my_events.*
import kotlinx.android.synthetic.main.fragment_start_screen.*
import java.util.*
=======
import com.example.grzybekapk.R
>>>>>>> apkaBranch

class FragStartScreen : Fragment(){
    override fun onAttach(context: Context?) {
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater!!.inflate(R.layout.fragment_start_screen, container, false)
    }
<<<<<<< HEAD

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val eventNameArray = ArrayList<String>() // Array for event names from database
        val organizerNameArray = ArrayList<String>() // Array for event organizers from database
        val eventDescriptionArray = ArrayList<String>() // Array for event description from database
        //val eventDateArray = ArrayList<Date>()  Array for event date from database ???
        val arrayAdapter = ArrayAdapter(activity,android.R.layout.simple_list_item_1,eventNameArray)
        eventsListView.adapter = arrayAdapter

        eventNameArray.add("Flanki")                    // hardcoded for testing, waiting for backend boys ( ͡° ͜ʖ ͡°)
        organizerNameArray.add("Mati")
        eventDescriptionArray.add("Final")
        eventNameArray.add("Grill")                    // hardcoded for testing, waiting for backend boys ( ͡° ͜ʖ ͡°)
        organizerNameArray.add("Barti")
        eventDescriptionArray.add("Grillowanie na grzybku")
        eventsListView.onItemClickListener = AdapterView.OnItemClickListener{adapterView,view,i,l->
            val intent = Intent(activity,EventDetailsActivity::class.java)
            intent.putExtra("name",eventNameArray[i])
            intent.putExtra("description",eventDescriptionArray[i])
            intent.putExtra("organizer",organizerNameArray[i])
            startActivity(intent)
        }
    }

    fun OknoDialogowe(v : View){

    }


=======
>>>>>>> apkaBranch
}