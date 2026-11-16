package my.edu.tarc.tarumtfoodorderingsystem.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle

@Composable
fun AppFieldLabel(
    title: String,
    modifier: Modifier = Modifier,
    required: Boolean = false
) {
    val requiredColor = MaterialTheme.colorScheme.error

    Text(
        text = buildAnnotatedString {
            append(title)

            if (required) {
                withStyle(
                    style = SpanStyle(
                        color = requiredColor,
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append(" *")
                }
            }
        },
        modifier = modifier,
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}