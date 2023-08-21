package com.eye.screen.admin

sealed interface AdminEvent{
    object GetSMSLogs : AdminEvent
    object GetCallLogs: AdminEvent
    class SetUser(val value: String) : AdminEvent
}