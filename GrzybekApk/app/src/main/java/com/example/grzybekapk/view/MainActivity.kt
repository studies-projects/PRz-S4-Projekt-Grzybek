package com.example.grzybekapk.view

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toolbar
import com.example.grzybekapk.R

class MainActivity : AppCompatActivity() {
    lateinit var toolbar: Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onClickLogIn(v : View){
        val intent = Intent(this, EkranGlowny::class.java)
        startActivity(intent);
    }


}
