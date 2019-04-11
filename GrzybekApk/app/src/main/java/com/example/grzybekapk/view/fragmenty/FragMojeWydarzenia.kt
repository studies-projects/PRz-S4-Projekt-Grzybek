package com.example.grzybekapk.view.fragmenty

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.grzybekapk.R

class FragMojeWydarzenia: Fragment() {
//    companion object {
//        fun newInstance(): FragMojeWydarzenia{
//            return FragMojeWydarzenia()
//        }
//    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater!!.inflate(R.layout.fragment_moje_wydarzenia, container, false)
    }
}
