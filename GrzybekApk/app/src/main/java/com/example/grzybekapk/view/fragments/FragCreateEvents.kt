package com.example.grzybekapk.view.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.grzybekapk.R
import com.example.grzybekapk.view.DataForEvents
import kotlinx.android.synthetic.main.fragment_create_event.*
import java.text.SimpleDateFormat
import java.util.*


class FragCreateEvents: Fragment(){

    private lateinit var datePickerButton: Button
    private lateinit var timePickerButton: Button
    private lateinit var confButton: Button
    private lateinit var descrEdTxt: EditText
    private lateinit var titleEdTxt: EditText
    private lateinit var dpd: DatePickerDialog
    private lateinit var tpd: TimePickerDialog
    private lateinit var date: Calendar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_create_event, container, false)
        (activity as AppCompatActivity).supportActionBar?.setTitle("Utwórz wydarzenie")
        datePickerButton = view.findViewById(R.id.date_pick_button) as Button
        timePickerButton = view.findViewById(R.id.timePicker) as Button
        confButton = view.findViewById(R.id.confirmButton) as Button
        descrEdTxt = view.findViewById(R.id.edit_description) as EditText
        titleEdTxt = view.findViewById(R.id.edit_title) as EditText


        /*
            Skrócic zapis listenera do kotlinowej wersji
            Nigdzie nie wykorzystujecie parametru View...
            A nawet jak trzeba to można się do niego odwołać jako 'it'
         */
        datePickerButton.setOnClickListener{
            if(!::date.isInitialized) {
                date = Calendar.getInstance()
            }
            var day: Int = date.get(Calendar.DAY_OF_MONTH)
            var month: Int = date.get(Calendar.MONTH)
            var year: Int = date.get(Calendar.YEAR)

            dpd = DatePickerDialog(activity, DatePickerDialog.OnDateSetListener { view, nYear, nMonth, nDay ->
                date.set(nYear, nMonth, nDay)
                datePickerButton.setText(SimpleDateFormat("dd-MM-yyyy").format(date.time))
            }, year, month, day)
            dpd.datePicker.minDate = System.currentTimeMillis()
            dpd.show()
        }


        timePickerButton.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?){
                if(!::date.isInitialized) {
                    date = Calendar.getInstance()
                }
                var hour = date.get(Calendar.HOUR_OF_DAY)
                var minute = date.get(Calendar.MINUTE)

                tpd = TimePickerDialog(activity, TimePickerDialog.OnTimeSetListener{view, nHour, nMinute ->
                    date.set(Calendar.HOUR_OF_DAY, nHour)
                    date.set(Calendar.MINUTE, nMinute)
                    timePickerButton.setText(SimpleDateFormat("HH:mm").format(date.time))
                }, hour, minute, true)
                tpd.show()
            }
        })

        confButton.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                var name: String = titleEdTxt.text.toString()
                var description = descrEdTxt.text.toString()

                //TODO Jakieś ograniczenia do nazwy wydarzenia, typu minimalna liczba znaków itp

                if(datePickerButton.text != resources.getString(R.string.default_date)
                    && timePickerButton.text != resources.getString(R.string.default_time)
                    && name != "") {

                   // FragCalendar.createEvent(date, DataForEvents(name, description, date, "Mateusz"))
                    Toast.makeText(activity, "Utworzono wydarzenie", Toast.LENGTH_LONG).show()

                } else {
                    Toast.makeText(activity, "Nie wybrano daty, godziny lub nazwy", Toast.LENGTH_LONG).show()
                }
            } //Tworzenie eventu, który pojawi się w kalendarzu
        })
        return view
    }
}