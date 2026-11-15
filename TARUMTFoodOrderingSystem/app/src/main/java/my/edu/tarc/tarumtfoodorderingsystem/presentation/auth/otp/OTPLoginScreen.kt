package my.edu.tarc.tarumtfoodorderingsystem.presentation.auth.otp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import my.edu.tarc.tarumtfoodorderingsystem.R
import my.edu.tarc.tarumtfoodorderingsystem.ui.components.AppBackButton

@Composable
fun OTPLoginScreen(
    uiState: OTPLoginUiState,
    onAction: (OTPLoginAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    val focusManager = LocalFocusManager.current

    val interactionsEnabled = !uiState.isVerificationSuccessful

    val submit: () -> Unit = {
        focusManager.clearFocus()

        onAction(
            if (uiState.isExpired) {
                OTPLoginAction.ResendClicked
            } else {
                OTPLoginAction.VerifyClicked
            }
        )
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = colors.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .background(colors.primary)
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.safeDrawing)
                    .imePadding(),
                contentAlignment = Alignment.TopCenter
            ) {
                Column(
                    modifier = Modifier
                        .widthIn(max = 480.dp)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(
                            horizontal = 22.dp,
                            vertical = 8.dp
                        )
                ) {
                    AppBackButton(
                        enabled = interactionsEnabled,
                        onClick = {
                            focusManager.clearFocus()
                            onAction(OTPLoginAction.BackClicked)
                        }
                    )

                    Image(
                        painter = painterResource(R.drawable.tarumtlogo),
                        contentDescription = "TAR UMT logo",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(110.dp)
                    )

                    Spacer(Modifier.height(38.dp))

                    Text(
                        text = "Check your email",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = colors.onBackground
                    )

                    Spacer(Modifier.height(10.dp))

                    Text(
                        text = "Enter the 6-digit code sent to\n" +
                                maskOTPEmail(uiState.email),
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.onSurfaceVariant
                    )

                    Spacer(Modifier.height(48.dp))

                    OTPCodeInput(
                        code = uiState.code,
                        enabled = interactionsEnabled && !uiState.isExpired,
                        isError = uiState.errorMessage != null,
                        onCodeChanged = {
                            onAction(OTPLoginAction.CodeChanged(it))
                        },
                        onDone = {
                            if (
                                interactionsEnabled &&
                                !uiState.isExpired &&
                                uiState.code.length == 6
                            ) {
                                submit()
                            }
                        }
                    )

                    Spacer(Modifier.height(24.dp))

                    Text(
                        text = if (uiState.isExpired) {
                            "This code has expired. Request a new code."
                        } else {
                            "Your code is valid for " +
                                    formatOTPTime(uiState.remainingSeconds) + "."
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = if (uiState.isExpired) {
                            colors.error
                        } else {
                            colors.onSurfaceVariant
                        }
                    )

                    uiState.errorMessage?.let { message ->
                        Spacer(Modifier.height(10.dp))

                        Text(
                            text = message,
                            style = MaterialTheme.typography.bodySmall,
                            color = colors.error
                        )
                    }

                    uiState.infoMessage?.let { message ->
                        Spacer(Modifier.height(10.dp))

                        Text(
                            text = message,
                            style = MaterialTheme.typography.bodySmall,
                            color = colors.onSurfaceVariant
                        )
                    }

                    Spacer(Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(
                            onClick = {
                                focusManager.clearFocus()
                                onAction(OTPLoginAction.ResendClicked)
                            },
                            enabled = interactionsEnabled && uiState.canResend,
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = colors.secondary
                            )
                        ) {
                            Text(
                                text = if (uiState.canResend) {
                                    "Resend code"
                                } else {
                                    "Resend in ${uiState.resendSeconds} seconds"
                                },
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        TextButton(
                            onClick = {
                                focusManager.clearFocus()
                                onAction(OTPLoginAction.ChangeEmailClicked)
                            },
                            enabled = interactionsEnabled,
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = colors.secondary
                            )
                        ) {
                            Text(
                                text = "Change email",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(Modifier.height(72.dp))

                    Button(
                        onClick = submit,
                        enabled = interactionsEnabled &&
                                if (uiState.isExpired) {
                                    uiState.canResend
                                } else {
                                    uiState.code.length == 6
                                },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text(
                            text = if (uiState.isExpired) {
                                "Send a new code"
                            } else {
                                "Verify and sign in"
                            },
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(Modifier.height(34.dp))

                    Text(
                        text = "Never share your sign-in code with anyone.",
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
private fun OTPCodeInput(
    code: String,
    enabled: Boolean,
    isError: Boolean,
    onCodeChanged: (String) -> Unit,
    onDone: () -> Unit
) {
    val colors = MaterialTheme.colorScheme
    var isFocused by remember { mutableStateOf(false) }

    BasicTextField(
        value = code,
        onValueChange = onCodeChanged,
        enabled = enabled,
        singleLine = true,
        textStyle = TextStyle(
            color = Color.Transparent
        ),
        cursorBrush = SolidColor(Color.Transparent),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.NumberPassword,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = { onDone() }
        ),
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged {
                isFocused = it.isFocused
            }
            .semantics {
                contentDescription = "Six-digit verification code"
            },
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Visual boxes; the real editable field is layered above.
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    repeat(6) { index ->
                        val isActive = enabled &&
                                isFocused &&
                                index == code.length

                        val borderColor = when {
                            isError -> colors.error
                            isActive -> colors.primary
                            else -> Color.Transparent
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp)
                                .background(
                                    color = colors.surfaceVariant,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .border(
                                    width = 1.dp,
                                    color = borderColor,
                                    shape = RoundedCornerShape(12.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = code.getOrNull(index)?.toString() ?: "–",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = colors.onSurface
                            )
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .alpha(0f)
                ) {
                    innerTextField()
                }
            }
        }
    )
}

private fun maskOTPEmail(email: String): String {
    val atIndex = email.indexOf('@')

    if (atIndex <= 0 || atIndex == email.lastIndex) {
        return email
    }

    val firstCharacter = email.first()
    val domain = email.substring(atIndex + 1)

    return "$firstCharacter••••••@$domain"
}

private fun formatOTPTime(seconds: Int): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60

    return "$minutes:${remainingSeconds.toString().padStart(2, '0')}"
}
