package com.eye.screen.user

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eye.data.module.SMS
import com.eye.data.repository.firestorerepository.FirestoreRepository
import com.eye.utils.EyeDetails
import com.eye.utils.EyeDetails.readSMS
import com.google.firebase.firestore.FirebaseFirestore
import com.onesignal.OSNotificationReceivedEvent
import com.onesignal.OneSignal
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val firestore: FirestoreRepository,
    @ApplicationContext private val context: Context
    ): ViewModel(){
    private val _state = MutableStateFlow(UserState())
    val state: StateFlow<UserState> = _state



    @RequiresApi(Build.VERSION_CODES.O)
    val onEvent: (event: UserEvent) -> Unit = { event ->
//        viewModelScope.launch {
//        when (event) {
//            UserEvent.SMS -> {
//                val  smsReader = context.readSMS().groupBy {
//                    it.date
//                }
//                _state.update {
//                    it.copy(
//                        sms = smsReader
//                    )
//                }
//                firestore.uploadSMSLog(smsReader)
//            }
//        }
//    }
    }
}