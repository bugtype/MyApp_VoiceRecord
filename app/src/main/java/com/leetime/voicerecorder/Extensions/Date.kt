package com.leetime.voicerecorder.Extensions

import java.text.SimpleDateFormat
import java.util.*

fun Date.toSimpleString() : String {
    val format = SimpleDateFormat("yyyy.MM.dd_hh:mm:sss")
    return format.format(this)
}