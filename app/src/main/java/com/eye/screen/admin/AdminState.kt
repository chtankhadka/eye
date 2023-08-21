package com.eye.screen.admin

import com.eye.data.module.CallLog
import com.eye.data.module.SMS

data class AdminState(
    val smsLogs :List<Map<String,List<SMS>>> = emptyList(),
    val callLogs :List<Map<String,List<CallLog>>> = emptyList(),
    val users : List<String> = emptyList(),
    val user: String = ""
)
