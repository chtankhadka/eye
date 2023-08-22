package com.eye.data.local

import android.app.Application
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Preference constructor(
    val context : Context
) {

    @Inject
    constructor(
        application: Application
    ): this (
        application.applicationContext
    )
    companion object {
        private const val PREFERENCE_NAME = "PREFERENCE_NAME"
        private const val USER_NAME = "USER_NAME"
        private const val IS_CALL_LOG = "IS_CALL_LOG"
        private const val IS_SMS = "IS_SMS"
    }

    private val sharedPreference: SharedPreferences =
        context.getSharedPreferences(PREFERENCE_NAME,MODE_PRIVATE)

    var userNmae: String?
        get() = sharedPreference.getString(USER_NAME,null)
        set(value) {sharedPreference.edit().putString(USER_NAME,value).apply()}

    var isCallLog
        get() = sharedPreference.getBoolean(IS_CALL_LOG, false)
        set(value) {sharedPreference.edit().putBoolean(IS_CALL_LOG,value).apply()}

    var isSMS
        get() = sharedPreference.getBoolean(IS_SMS, false)
        set(value) {sharedPreference.edit().putBoolean(IS_SMS,value).apply()}
}