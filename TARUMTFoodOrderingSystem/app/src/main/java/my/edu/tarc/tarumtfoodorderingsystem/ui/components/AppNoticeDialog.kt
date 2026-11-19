package my.edu.tarc.tarumtfoodorderingsystem.ui.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties

@Composable
fun AppNoticeDialog(
    title: String,
    message: String,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    confirmText: String = "OK"
) {
    val colors = MaterialTheme.colorScheme

    var closing by remember {
        mutableStateOf(false)
    }

    // Prevent repeated navigation while the destination is closing.
    fun closeNotice() {
        if (!closing) {
            closing = true
            onClose()
        }
    }

    AlertDialog(
        modifier = modifier,
        onDismissRequest = {
            closeNotice()
        },
        shape = RoundedCornerShape(20.dp),
        containerColor = colors.surface,
        titleContentColor = colors.onSurface,
        textContentColor = colors.onSurfaceVariant,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
        },
        text = {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    closeNotice()
                },
                enabled = !closing
            ) {
                Text(
                    text = confirmText,
                    fontWeight = FontWeight.SemiBold
                )
            }
        },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = false
        )
    )
}