package com.eye.screen.user

import com.eye.data.module.SMS

data class UserState(
    val sms: Map<String,List<SMS>> = emptyMap()
)
