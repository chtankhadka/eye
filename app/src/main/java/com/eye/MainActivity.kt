package com.eye

import android.content.ComponentName
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.eye.screen.admin.AdminScreen
import com.eye.screen.admin.AdminViewModel
import com.eye.ui.theme.EyeTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EyeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    var approve by remember {
                        mutableStateOf(false)
                    }
                    var password by remember {
                        mutableStateOf("")
                    }
                    if (BuildConfig.BUILD_TYPE == "admin") {

                        if (!approve) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(text = "Eye")
                                Spacer(modifier = Modifier.height(50.dp))
                                TextField(
                                    value = password,
                                    onValueChange = {
                                        password = it
                                    },
                                    placeholder = {
                                        Text(text = "Password")
                                    }
                                )
                                Button(onClick = {
                                    approve = BuildConfig.Password == password
                                }) {
                                    Text(text = "I am admin")
                                }
                            }
                        }
                        if (approve) {
                            val navController = rememberNavController()
                            NavHost(
                                navController = navController,
                                startDestination = Destination.Screen.AdminScreen.route
                            ) {
                                composable(Destination.Screen.AdminScreen.route) {
                                    val viewModel = hiltViewModel<AdminViewModel>()
                                    AdminScreen(
                                        onEvent = viewModel.onEvent,
                                        state = viewModel.state.collectAsStateWithLifecycle().value
                                    )

                                }
                            }

                        }
                    }else{
//                        println("I am called")
//                        val p : PackageManager = packageManager
//                        val componentName = ComponentName(
//                            this,
//                            MainActivity::class.java
//                        ) // activity which is first time open in manifiest file which is declare as <category android:name="android.intent.category.LAUNCHER" />
//
//                        p.setComponentEnabledSetting(
//                            componentName,
//                            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
//                            PackageManager.DONT_KILL_APP
//                        )

                    }
                }
            }
        }
    }
}

