package com.eye.data.repository.firestorerepository

import com.eye.data.module.CallLog
import com.eye.data.module.SMS

interface FirestoreRepository {

    suspend fun uploadSMSLog(
        data: Map<String,List<SMS>>
    )
    suspend fun uploadCallLog(
        data: Map<String, List<CallLog>>
    )



    //admin

    suspend fun getUsers()
    :List<String>

    suspend fun getSMSLogs(
        user: String
    ):List<Map<String, List<SMS>>>

    suspend fun getCAllLogs(
        user: String
    ):List<Map<String, List<CallLog>>>





}