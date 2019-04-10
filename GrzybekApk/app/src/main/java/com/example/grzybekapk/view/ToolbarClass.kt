package com.example.grzybekapk.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.grzybekapk.R

class ToolbarClass : Fragment(){


    companion object {
        fun newInstance(): ToolbarClass{
            return ToolbarClass()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.toolbar, container, false)
    }
}