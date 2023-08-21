package com.eye.screen.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eye.data.repository.firestorerepository.FirestoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository
) : ViewModel() {
    private val _state = MutableStateFlow(AdminState())
    val state: StateFlow<AdminState> = _state
    init {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    users = firestoreRepository.getUsers()
                )
            }
        }
    }


    val onEvent: (event: AdminEvent) -> Unit = { event ->
        viewModelScope.launch {
            when (event) {
                AdminEvent.GetCallLogs -> {
                    _state.update {
                        it.copy(
                            callLogs = firestoreRepository.getCAllLogs(state.value.user)
                        )
                    }
                }
                AdminEvent.GetSMSLogs -> {
                    _state.update {
                        it.copy(
                            smsLogs = firestoreRepository.getSMSLogs(state.value.user)
                        )
                    }
                }

                is AdminEvent.SetUser ->{
                    _state.update {
                        it.copy(
                            user = event.value
                        )
                    }
                }
            }
        }
    }


}