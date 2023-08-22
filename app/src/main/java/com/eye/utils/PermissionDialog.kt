package com.eye.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermissionDialog(
    permissionTextProvider: PermissionTextProvider,
    isPermissionDeclined: Boolean,
    onDismiss: () -> Unit,
    onOkClick: () -> Unit,
    onGoToAppSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = {

        }) {
        Column {
            Text(text = "Permission required")
            Text(text = permissionTextProvider.getDescription(isPermissionDeclined))
            Divider()
            Text(
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                text = if (isPermissionDeclined) {
                    "Grant permission"
                } else {
                    "OK"
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        if (isPermissionDeclined) {
                            onGoToAppSettingsClick()
                        } else {
                            onOkClick()
                        }
                    }
                    .padding(16.dp)
            )


        }
    }

}
interface PermissionTextProvider {
    fun getDescription(isPermanentlyDeclined : Boolean) : String
}

class PhoneCallPermissionTextProvider : PermissionTextProvider{
    override fun getDescription(isPermanentlyDeclined: Boolean): String {
        return if (isPermanentlyDeclined){
            "It seems you permanently declined phone call permission." +
                    "You can go to the app settings to grant it."
        }else{
            "This app needs access to your phoneCall"
        }
    }

}
class SMSPermissionTextProvider : PermissionTextProvider{
    override fun getDescription(isPermanentlyDeclined: Boolean): String {
        return if (isPermanentlyDeclined){
            "It seems you permanently declined SMS permission." +
                    "You can go to the app settings to grant it."
        }else{
            "This app needs access to your SMS"
        }
    }

}