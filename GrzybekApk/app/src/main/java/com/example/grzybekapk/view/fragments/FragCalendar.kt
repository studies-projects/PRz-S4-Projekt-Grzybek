package com.example.grzybekapk.view.fragments

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.crashlytics.android.Crashlytics.TAG

import com.example.grzybekapk.R
import com.example.grzybekapk.view.DataForEvents
import com.example.grzybekapk.view.activities.EventDetailsActivity
import com.github.sundeepk.compactcalendarview.CompactCalendarView
import com.github.sundeepk.compactcalendarview.domain.Event
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore

import java.text.SimpleDateFormat
import java.util.*

class FragCalendar : Fragment() {

    private val dateFormatForMonth = SimpleDateFormat("MMMM - yyyy", Locale.getDefault())
    private var compactCalendarView: CompactCalendarView? = null
    private var toolbar: ActionBar? = null
    private var showMonthYear: TextView? = null
    private var bookingsFromMap: List<Event>? = ArrayList()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_calendar, container, false)

        val mutableBookings = ArrayList<String>()

        val bookingsListView = view.findViewById<ListView>(R.id.bookings_listview)
        val showPreviousMonthBut = view.findViewById<ImageButton>(R.id.prev_button)
        val showNextMonthBut = view.findViewById<ImageButton>(R.id.next_button)
        showMonthYear = view.findViewById(R.id.month_view)
        val adapter = ArrayAdapter(context!!, android.R.layout.simple_list_item_1, mutableBookings)

        bookingsListView.adapter = adapter
        compactCalendarView = view.findViewById(R.id.compactcalendar_view)

        compactCalendarView!!.setUseThreeLetterAbbreviation(true)
        compactCalendarView!!.setFirstDayOfWeek(Calendar.MONDAY)
        compactCalendarView!!.setIsRtl(false)
        compactCalendarView!!.displayOtherMonthDays(true)

        toolbar = (activity as AppCompatActivity).supportActionBar
        toolbar!!.title = "Kalendarz"

        val firstDayOfMonth = Calendar.getInstance()
        firstDayOfMonth.time = compactCalendarView!!.firstDayOfCurrentMonth
        // o tu nie działa
        getEvents(firstDayOfMonth)

        showMonthYear!!.text = dateFormatForMonth.format(compactCalendarView!!.firstDayOfCurrentMonth)

        bookingsListView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val evClicked = bookingsFromMap!![position]
            val data = evClicked.data as DataForEvents?

            val i = Intent(context, EventDetailsActivity::class.java)
                .putExtra("name", data!!.nameOfEvent)
                .putExtra("description", data.descriptionOfEvent)
                .putExtra("organizer", data.organizer)
                .putExtra("date", data.dateOfEventTxt + " o godz. " + data.timeOfEventTxt)
            startActivity(i)
        }

        compactCalendarView!!.setListener(object : CompactCalendarView.CompactCalendarViewListener {
            override fun onDayClick(dateClicked: Date) {
                bookingsFromMap = compactCalendarView!!.getEvents(dateClicked)
                if (bookingsFromMap != null) {
                    mutableBookings.clear()
                    for (booking in bookingsFromMap!!) {                                         //pętla foreach
                        val dt = booking.data as DataForEvents?                       // pobranie danych naszego eventu
                        val message = dt!!.timeOfEventTxt + " " + dt.nameOfEvent
                        mutableBookings.add(message)
                    }
                    adapter.notifyDataSetChanged()
                } // Wyświetlenie wydarzeń w danym dniu
            }



            override fun onMonthScroll(firstDayOfNewMonth: Date) {
                showMonthYear!!.text = dateFormatForMonth.format(compactCalendarView!!.firstDayOfCurrentMonth)
                firstDayOfMonth.time = compactCalendarView!!.firstDayOfCurrentMonth

                getEvents(firstDayOfMonth)
            }
        })

        showPreviousMonthBut.setOnClickListener { compactCalendarView!!.scrollLeft() }
        showNextMonthBut.setOnClickListener { compactCalendarView!!.scrollRight() }

        compactCalendarView!!.setAnimationListener(object : CompactCalendarView.CompactCalendarAnimationListener {
            override fun onOpened() {}

            override fun onClosed() {}
        })
        return view
    }

    override fun onResume() {
        super.onResume()
        showMonthYear!!.text = dateFormatForMonth.format(compactCalendarView!!.firstDayOfCurrentMonth)
    }

    fun getEvents(firstDayOfMonth: Calendar){
        compactCalendarView!!.removeAllEvents()
        firstDayOfMonth.add(Calendar.MONTH,-1)
        var lastDayOfMonth = Calendar.getInstance()
        lastDayOfMonth.set(Calendar.YEAR,firstDayOfMonth.get(Calendar.YEAR))
        lastDayOfMonth.set(Calendar.MONTH,firstDayOfMonth.get(Calendar.MONTH)+3)
        lastDayOfMonth.set(Calendar.DAY_OF_MONTH,1)
        lastDayOfMonth.set(Calendar.HOUR,0)
        lastDayOfMonth.set(Calendar.MINUTE,0)
        lastDayOfMonth.set(Calendar.SECOND,0)
        lastDayOfMonth.set(Calendar.MILLISECOND,0)

        db.collection("Events")
            .whereGreaterThanOrEqualTo("DateStart",Timestamp(firstDayOfMonth.time))
            .whereLessThanOrEqualTo("DateStart",Timestamp(lastDayOfMonth.time))
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    var timestamp = document.data["DateStart"] as Timestamp
                    var date :Date = timestamp.toDate()
                    val cal = Calendar.getInstance()
                    cal.setTime(date)
                    var event = DataForEvents(document.data["Name"] as String, document.data["Desc"] as String,cal,document.data["Owner"] as String)

                    createEvent(event.date,event)

                    Log.d(TAG, "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }
        compactCalendarView!!.addEvents(events)
        events.clear()
    }

    companion object {
        private val events = ArrayList<Event>()

        //Dwie statyczne funkcje do dodawania wydarzeń w kalendarzu, bierzcie jak swoje
        fun createEvent(date: Calendar, `object`: DataForEvents) {
            events.add(Event(Color.rgb(255, 0, 0), date.timeInMillis, `object`))
        }

        //Ta się różni tym, że kolor tej kropki można ustawić według uznania
        fun createEvent(date: Calendar, `object`: DataForEvents, red: Int, green: Int, blue: Int) {
            events.add(Event(Color.rgb(red, green, blue), date.timeInMillis, `object`))
        }
    }
}