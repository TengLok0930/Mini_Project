package my.edu.tarc.tarumtfoodorderingsystem.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import my.edu.tarc.tarumtfoodorderingsystem.R

@Composable
fun AppBackButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val iconColor = MaterialTheme.colorScheme.onBackground

    IconButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled
    ) {
        Icon(
            painter = painterResource(R.drawable.arrow_back_24px),
            contentDescription = "Back",
            modifier = Modifier.size(24.dp),
            tint = if (enabled) {
                iconColor
            } else {
                iconColor.copy(alpha = 0.38f)
            }
        )
    }
}