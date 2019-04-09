package com.example.grzybekapk.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.grzybekapk.R

class FragEkranStartowy : Fragment(){


    companion object {
        fun newInstance(): FragEkranStartowy{
            return FragEkranStartowy()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_ekran_startowy, container, false)
    }
}