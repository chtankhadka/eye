package com.eye

import android.Manifest
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.eye.data.local.Preference
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val preference: Preference
) : ViewModel() {


    val visiblePermissionDialogQueue = mutableStateListOf<String>()


    fun dismissDialog(){
        visiblePermissionDialogQueue.removeFirst()
    }
    fun onPermissionResult(
        permission: String,
        isGranted: Boolean
    ){
        when (permission){
            Manifest.permission.READ_SMS -> {

                preference.isCallLog = isGranted
            }
            Manifest.permission.READ_SMS -> {
                preference.isSMS = isGranted
            }
        }
        if (!isGranted){
            visiblePermissionDialogQueue.add(permission)
        }
    }

}