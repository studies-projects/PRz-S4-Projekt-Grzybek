package com.example.grzybekapk.view.activities

import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.grzybekapk.R
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.functions.FirebaseFunctions


class MainActivity : AppCompatActivity() {
    private lateinit var functions: FirebaseFunctions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    public override fun onStart() {
        super.onStart()
    }

    fun onClickLogIn(v : View){
        Log.d("CHECKPOINT", "Onclick RUN!")

        functions = FirebaseFunctions.getInstance()
        functions.useFunctionsEmulator("http://192.168.0.20:5000")

        /*var text:String = "test"
        val data = hashMapOf(
            "text" to text,
            "push" to true
        )*/


        functions
        //FirebaseFunctions.getInstance()
            .getHttpsCallable("test")
            .call(hashMapOf("manufacturer" to Build.MANUFACTURER))
            .continueWith { task ->
                if (task.isSuccessful) {
                    //Toast.makeText(activity, "Upppercase manufacturer is: ${task.result!!.data}", Toast.LENGTH_SHORT).show()
                    Log.d("RESPONSE", "Upppercase manufacturer is: ${task.result!!.data}")
                } else {
                    Log.d("FAILED", task.exception.toString())
                }
            }
        /*val intent = Intent(this, MainScreen::class.java)
        startActivity(intent)*/
        Log.d("CHECKPOINT", "Onclick DONE!")
    }

}
