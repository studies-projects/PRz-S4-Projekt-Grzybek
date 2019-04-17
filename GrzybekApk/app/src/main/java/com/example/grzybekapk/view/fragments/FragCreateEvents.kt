package com.example.grzybekapk.view.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.grzybekapk.R
import java.util.*


class FragCreateEvents: Fragment() {

    private lateinit var button: Button
    private lateinit var textView: TextView
    private lateinit var calendar: Calendar
    private lateinit var dpd: DatePickerDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_create_event, container, false)

        button = view!!.findViewById(R.id.date_pick_button) as Button

        button.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                calendar = Calendar.getInstance()
                var day: Int = calendar.get(Calendar.DAY_OF_MONTH)
                var month: Int = calendar.get(Calendar.MONTH)
                var year: Int = calendar.get(Calendar.YEAR)

                dpd = DatePickerDialog(activity, DatePickerDialog.OnDateSetListener { view, nYear, nMonth, nDay ->
                    if(nDay <10 && nMonth < 9){
                        button.setText("0" + nDay + "/0" + (nMonth+1) + "/" + nYear)
                    } else if(nDay < 10){
                        button.setText("0" + nDay + "/" + (nMonth+1) + "/" + nYear)
                    } else if (nMonth < 9){
                        button.setText("" + nDay + "/0" + (nMonth+1) + "/" + nYear)
                    } else {
                        button.setText("" + nDay + "/" + (nMonth+1) + "/" + nYear)
                    }
                }, year, month, day)
                dpd.show()
            }
        })

        return view
    }
}