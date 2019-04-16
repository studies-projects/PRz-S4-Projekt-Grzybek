package com.example.grzybekapk.view.fragments

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v4.app.ListFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import com.example.grzybekapk.R
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_start_screen.*

class FragStartScreen : Fragment(){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater!!.inflate(R.layout.fragment_start_screen, container, false)
    }

    fun OknoDialogowe(v : View){

    }
}