package com.example.grzybekapk.view.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.grzybekapk.R
import com.example.grzybekapk.view.DataForEvents
import com.example.grzybekapk.view.activities.EventDetailsActivity
import com.example.grzybekapk.view.activities.MainScreen
import java.text.SimpleDateFormat
import java.util.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.Timestamp

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



        val db = FirebaseFirestore.getInstance()

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


                if(datePickerButton.text != resources.getString(R.string.default_date)
                    && timePickerButton.text != resources.getString(R.string.default_time)
                    && name != "") {
                    confButton.isEnabled = false

                  //  var dateStr: String = "$day/$month/$year $hour:$minute:00"
                 //   var dateEpoch: Long = java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").parse(dateStr).getTime() / 1000
                    val items = HashMap<String, Any>()
                    items.put("DateStart", Timestamp(date.time))
                    items.put("Desc", description)
                    items.put("Name", name)
                    items.put("Owner", FirebaseAuth.getInstance().currentUser!!.uid)

                    db.collection("Events").document().set(items).addOnSuccessListener {
                        Toast.makeText(activity, "Utworzono wydarzenie", Toast.LENGTH_LONG).show()
                        var fragment: Fragment? = FragStartScreen()
                        val man = activity?.supportFragmentManager
                        val trans = man?.beginTransaction()

                        if(fragment!=null) {
                            trans?.replace(R.id.frameLay, fragment)
                            trans?.commit()
                        }

                        (activity as MainScreen).switchFr(view)

                        val intent = Intent(activity, EventDetailsActivity::class.java)
                            .putExtra("event", DataForEvents("", name, description, date, FirebaseAuth.getInstance().currentUser!!.uid))
                        startActivity(intent)
                    }
                } else {
                    Toast.makeText(activity, "Nie wybrano daty, godziny lub nazwy", Toast.LENGTH_LONG).show()
                }
            }
        })
        return view
    }
}