package com.eye

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.widget.Toast
import androidx.work.Constraints

import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.eye.service.EyeWorker
import com.eye.service.ScreenOnReceiver
import com.google.firebase.FirebaseApp
import com.onesignal.OSNotificationReceivedEvent
import com.onesignal.OneSignal
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit


@HiltAndroidApp
class EyeApp : Application(), OneSignal.OSRemoteNotificationReceivedHandler{
    private val screenStateReceiver = ScreenOnReceiver()
    private var isScreenStateReceiverRegistered = false


    override fun onCreate() {
        super.onCreate()
        initFirebase()
        if (BuildConfig.BUILD_TYPE != "admin"){
            initOneSignal()
            setupWorker()
            registerScreenStateReceiver()
        }



    }

    @SuppressLint("InvalidPeriodicWorkRequestInterval")
    private fun setupWorker(){
        val constraint = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        val workRequest = PeriodicWorkRequest.Builder(EyeWorker::class.java,1,TimeUnit.MINUTES)
            .setConstraints(constraint)
            .build()
        WorkManager.getInstance(this).enqueue(workRequest)
    }

    private fun registerScreenStateReceiver() {
        if (!isScreenStateReceiverRegistered) {
            val intentFilter = IntentFilter()
            intentFilter.addAction(Intent.ACTION_SCREEN_ON)
            registerReceiver(screenStateReceiver, intentFilter)
            isScreenStateReceiverRegistered = true
        }
    }

    fun unregisterScreenStateReceiver() {
        if (isScreenStateReceiverRegistered) {
            unregisterReceiver(screenStateReceiver)
            isScreenStateReceiverRegistered = false
        }
    }

    private fun initFirebase() {
        FirebaseApp.initializeApp(this)
    }
    private fun initOneSignal(){
        //enable verbose oneSignal logging to debug issues if needed.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)

        //OneSignal Initialixation
        OneSignal.initWithContext(this)
        OneSignal.setAppId(BuildConfig.OneSignalId)
        OneSignal.promptForPushNotifications()



    }

    override fun remoteNotificationReceived(context: Context?, notificationReceivedEvent: OSNotificationReceivedEvent?) {
        val notification = notificationReceivedEvent?.notification
        val data = notification?.body
        Toast.makeText(this,data.toString(),Toast.LENGTH_LONG).show()

    }
}