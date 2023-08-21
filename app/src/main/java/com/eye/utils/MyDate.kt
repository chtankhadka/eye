package com.eye.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object MyDate {

    fun convertIntToDate(timestamp: String): String{
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date = Date(timestamp.toLong())
        return dateFormat.format(date)
    }
}