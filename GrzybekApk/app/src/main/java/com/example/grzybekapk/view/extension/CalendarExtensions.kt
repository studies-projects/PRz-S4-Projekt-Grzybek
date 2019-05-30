package com.example.grzybekapk.view.extension

import com.google.firebase.Timestamp
import java.util.*

fun Calendar.asTimestamp() : Timestamp = Timestamp(this.time)