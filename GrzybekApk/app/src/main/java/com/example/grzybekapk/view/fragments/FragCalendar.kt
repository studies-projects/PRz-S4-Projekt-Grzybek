package com.example.grzybekapk.view.fragments

import android.app.usage.UsageEvents
import android.os.Bundle
import android.support.v4.view.ViewPager;
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.GridView
import com.example.grzybekapk.R
import com.github.sundeepk.compactcalendarview.CompactCalendarView
import java.util.*


class FragCalendar: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        (activity as AppCompatActivity).supportActionBar?.setTitle("Kalendarz")
        var view = inflater!!.inflate(R.layout.fragment_calendar, container, false)

        val compactCalendarView = view.findViewById<CompactCalendarView>(R.id.compactcalendar_view)
        compactCalendarView.setFirstDayOfWeek(Calendar.MONDAY)



        return view
    }


}