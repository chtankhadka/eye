package com.eye.screen.admin

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun AdminScreen(onEvent: (event: AdminEvent) -> Unit, state: AdminState) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    var showDialog by remember {
        mutableStateOf(false)
    }
    var isCallLog by remember {
        mutableStateOf(true)
    }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                showDialog = false
            }, confirmButton = {
                Button(onClick = {showDialog = false }) {
                    Text(text = "Okay")
                }
            },
            icon = { Icon(imageVector = Icons.Default.Warning, contentDescription = "warning")}

            )
    }
    ModalNavigationDrawer(
        modifier = Modifier.background(MaterialTheme.colorScheme.onBackground),
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier
                    .fillMaxWidth(0.20f)
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    content = {
                        items(state.users) {
                            Card(
                                modifier = Modifier
                                    .clickable {
                                        onEvent(AdminEvent.SetUser(it))
                                    }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PhoneAndroid,
                                    contentDescription = "phone"
                                )
                                Text(text = it)
                            }
                        }
                    })

            }

        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                    Column {
                        Image(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(shape = CircleShape),
                            imageVector = Icons.Default.PersonOutline, contentDescription = ""
                        )
                        Text(text = if (state.user.isEmpty()) "No User" else state.user)

                    }
                    Column(verticalArrangement = Arrangement.SpaceAround) {
                        Button(onClick = {
                            if (state.user.isBlank()){
                                showDialog = true
                            } else {
                                isCallLog = false
                                onEvent(AdminEvent.GetSMSLogs)
                            }

                        }) {
                            Text(text = "SMS Details")
                        }
                        Button(onClick = {
                            if (state.user.isBlank()){
                                showDialog = true
                            } else{
                                isCallLog = true
                                onEvent(AdminEvent.GetCallLogs)
                            }

                        }) {
                            Text(text = "Call Details")
                        }
                    }


                }
                if (isCallLog){
                    LazyColumn(
                        modifier = Modifier.padding(5.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        content = {
                        items(state.callLogs) {
                            for ((date, callList) in it) {
                                Spacer(modifier = Modifier.height(5.dp))
                                Card {
                                    Text(text = "Date:$date")
                                    Spacer(modifier = Modifier.height(5.dp))
                                    for (call in callList) {
                                        Card(
                                            modifier = Modifier.padding(5.dp),
                                            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer)) {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween) {
                                                Text(text = "Time: ${call.time}")
                                                Text(text = "Duration: ${call.duration}")
                                            }
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween) {
                                                Text(text = "Phone No: ${call.phoneNumber}")
                                                Text(text = "State: ${call.state}")
                                            }

                                        }
                                    }
                                }
                            }

                        }
                    })
                }else{
                    LazyColumn( modifier = Modifier.padding(5.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp), content = {
                        items(state.smsLogs) {
                            for ((date, smsList) in it) {
                                Card {
                                    Text(text = "Date:$date")
                                    Spacer(modifier = Modifier.height(5.dp))
                                    for (call in smsList) {

                                        Card(modifier = Modifier.padding(5.dp),
                                        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer)) {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween) {
                                                Text(text = "Time: ${call.time}")
                                            }
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween) {
                                                Text(text = "Phone No: ${call.phoneNumber}")
                                            }
                                            Text(text = "SMS: ${call.sms}")

                                        }
                                    }
                                }
                            }

                        }
                    })
                }

            }


        }
    )


}