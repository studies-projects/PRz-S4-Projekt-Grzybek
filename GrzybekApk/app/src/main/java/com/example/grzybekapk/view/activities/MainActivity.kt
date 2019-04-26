package com.example.grzybekapk.view.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.grzybekapk.R
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())
        setContentView(R.layout.activity_main)
    }

    fun onClickLogIn(v : View){
        val intent = Intent(this, MainScreen::class.java)
        startActivity(intent)
    }

}
