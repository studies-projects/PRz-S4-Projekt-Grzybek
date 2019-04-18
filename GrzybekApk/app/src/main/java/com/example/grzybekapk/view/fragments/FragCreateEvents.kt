package com.example.grzybekapk.view.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.grzybekapk.R
import kotlinx.android.synthetic.main.fragment_create_event.*
import java.text.SimpleDateFormat
import java.util.*


class FragCreateEvents: Fragment() {

    private lateinit var datePickerButton: Button
    private lateinit var timePickerButton: Button
    private lateinit var calendar: Calendar
    private lateinit var dpd: DatePickerDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_create_event, container, false)
        (activity as AppCompatActivity).supportActionBar?.setTitle("Utwórz wydarzenie")
        datePickerButton = view!!.findViewById(R.id.date_pick_button) as Button
        timePickerButton = view.findViewById(R.id.timePicker) as Button

        datePickerButton.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                calendar = Calendar.getInstance()
                var day: Int = calendar.get(Calendar.DAY_OF_MONTH)
                var month: Int = calendar.get(Calendar.MONTH)
                var year: Int = calendar.get(Calendar.YEAR)

                dpd = DatePickerDialog(activity, DatePickerDialog.OnDateSetListener { view, nYear, nMonth, nDay ->
                    if(nDay <10 && nMonth < 9){
                        datePickerButton.setText("0" + nDay + "-0" + (nMonth+1) + "-" + nYear)
                    } else if(nDay < 10){
                        datePickerButton.setText("0" + nDay + "-" + (nMonth+1) + "-" + nYear)
                    } else if (nMonth < 9){
                        datePickerButton.setText("" + nDay + "-0" + (nMonth+1) + "-" + nYear)
                    } else {
                        datePickerButton.setText("" + nDay + "-" + (nMonth+1) + "-" + nYear)
                    }
                }, year, month, day)
                dpd.datePicker.minDate = System.currentTimeMillis()
                dpd.show()
            }
        })

        timePickerButton.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?){
                calendar = Calendar.getInstance()
                val timeSetListener = TimePickerDialog.OnTimeSetListener{timePicker, hour, minute ->
                    calendar.set(Calendar.HOUR_OF_DAY, hour)
                    calendar.set(Calendar.MINUTE, minute)
                    timePickerButton.setText(SimpleDateFormat("HH:mm").format(calendar.time))
                }
                TimePickerDialog(view.context, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()
            }
        })
        return view
    }
}