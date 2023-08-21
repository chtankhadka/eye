package com.eye.screen.user

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight

@Composable
fun UserScreen(onEvent: (event: UserEvent) -> Unit, state: UserState) {
    Column(modifier = Modifier.fillMaxSize()) {
        Button(onClick = {
            onEvent(UserEvent.SMS)
        }) {
            Text(text = "Send SMS")
        }
        state.sms.forEach { (date, list) ->
            Text(text = "Date: $date", fontWeight = FontWeight.Bold)
            list.forEach {
                Text(text = it.date)
                Text(text = it.time)
                Text(text = it.phoneNumber)
                Text(text = it.sms)
            }
        }
    }
}