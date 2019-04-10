package com.example.grzybekapk.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import com.example.grzybekapk.R

class FragEkranStartowy : Fragment(){


    companion object {
        fun newInstance(): FragEkranStartowy{
            return FragEkranStartowy()
        }
    }
    private lateinit var toolbar: Toolbar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        var rootView: View = inflater.inflate(R.layout.fragment_ekran_startowy, container, false)




        return rootView
    }




}