package com.eye.screen.user

sealed interface UserEvent {
    object SMS: UserEvent
}