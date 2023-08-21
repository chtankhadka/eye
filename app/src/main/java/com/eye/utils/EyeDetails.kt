package com.eye.utils

import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.Telephony
import androidx.annotation.RequiresApi
import com.eye.data.module.CallLog
import com.eye.data.module.SMS
import timber.log.Timber
import java.time.LocalDate

object EyeDetails {

    @RequiresApi(Build.VERSION_CODES.O)
    fun Context.readSMS(): List<SMS> {
        val smsUri = Uri.parse("content://sms")
        val list = mutableListOf<SMS>()
        val cursor = contentResolver.query(smsUri, null, null, null)
        cursor?.use {
            val dateIndex = it.getColumnIndexOrThrow(Telephony.Sms.DATE)
            val phoneIndex = it.getColumnIndexOrThrow(Telephony.Sms.ADDRESS)
            val bodyIndex = it.getColumnIndexOrThrow(Telephony.Sms.BODY)
            while (it.moveToNext()) {
                val dateTime = MyDate.convertIntToDate(it.getString(dateIndex)).split(" ")
                val smsDate = dateTime.first()
                if (smsDate == LocalDate.now().toString()) {
                    list.add(
                        SMS(
                            date = smsDate,
                            time = dateTime.last(),
                            phoneNumber = it.getString(phoneIndex),
                            sms = it.getString(bodyIndex)
                        )
                    )
                }

                Timber.tag("SMS")
                    .d("Date: " + dateIndex + ", Address: " + phoneIndex + ",Body: " + bodyIndex)
            }
        }
        return list
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun Context.readCallLog(): List<CallLog>{
        val phoneCallLogs = mutableListOf<CallLog>()
        val cursor = contentResolver.query(android.provider.CallLog.Calls.CONTENT_URI, null,null,null)
        cursor?.use {
            val dateIndex = cursor.getColumnIndexOrThrow(android.provider.CallLog.Calls.DATE)
            val stateIndex = cursor.getColumnIndexOrThrow(android.provider.CallLog.Calls.TYPE)
            val durationIndex = cursor.getColumnIndexOrThrow(android.provider.CallLog.Calls.DURATION)
            val phoneNumberIndex = cursor.getColumnIndexOrThrow(android.provider.CallLog.Calls.NUMBER)

            while (cursor.moveToNext()){

                val dateTime = MyDate.convertIntToDate(it.getString(dateIndex)).split(" ")
                val smsDate = dateTime.first()
                if (smsDate == LocalDate.now().toString()) {
                    phoneCallLogs.add(
                        CallLog(
                            date = smsDate,
                            time = dateTime.last(),
                            state = cursor.getString(stateIndex),
                            duration = cursor.getString(durationIndex),
                            phoneNumber = cursor.getString(phoneNumberIndex)
                        )
                    )
                }
            }
        }
        return phoneCallLogs
    }

}