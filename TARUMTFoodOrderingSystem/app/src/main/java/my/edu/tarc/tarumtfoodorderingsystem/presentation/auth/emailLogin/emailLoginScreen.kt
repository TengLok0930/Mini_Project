package my.edu.tarc.tarumtfoodorderingsystem.presentation.auth.emailLogin

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import my.edu.tarc.tarumtfoodorderingsystem.R
import my.edu.tarc.tarumtfoodorderingsystem.ui.theme.TARUMTFoodOrderingSystemTheme

@Composable
fun EmailLoginScreen(
    uiState: EmailLoginUiState,
    isDarkTheme: Boolean,
    onDarkThemeChanged: (Boolean) -> Unit,
    onAction: (EmailLoginAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    val focusManager = LocalFocusManager.current

    val interactionsEnabled = uiState.pendingEmail == null

    val submitEmail: () -> Unit = {
        focusManager.clearFocus()
        onAction(EmailLoginAction.SendVerificationCodeClicked)
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
                            vertical = 32.dp
                        )
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Dark mode",
                            style = MaterialTheme.typography.labelLarge,
                            color = colors.onBackground,
                            modifier = Modifier.padding(end = 12.dp)
                        )

                        Switch(
                            checked = isDarkTheme,
                            onCheckedChange = onDarkThemeChanged
                        )
                    }

                    Spacer(Modifier.height(16.dp))

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
                        text = "Welcome back",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = colors.onBackground
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = "Sign in to Your TARUMT Account.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.onSurfaceVariant
                    )

                    Spacer(Modifier.height(24.dp))

                    EmailLoginMethodSelector(
                        enabled = interactionsEnabled,
                        onPasswordClick = {
                            focusManager.clearFocus()
                            onAction(
                                EmailLoginAction.BackToPasswordClicked
                            )
                        }
                    )

                    Spacer(Modifier.height(28.dp))

                    Text(
                        text = "Registered email",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = colors.onSurfaceVariant
                    )

                    Spacer(Modifier.height(8.dp))

                    TextField(
                        value = uiState.email,
                        onValueChange = {
                            onAction(EmailLoginAction.EmailChanged(it))
                        },
                        enabled = interactionsEnabled,
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        placeholder = {
                            Text("Enter your registered email")
                        },
                        isError = uiState.emailError != null,
                        supportingText = uiState.emailError?.let { error ->
                            { Text(error) }
                        },
                        shape = RoundedCornerShape(14.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = colors.surfaceVariant,
                            unfocusedContainerColor = colors.surfaceVariant,
                            disabledContainerColor = colors.surfaceVariant,
                            errorContainerColor = colors.surfaceVariant,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            errorIndicatorColor = Color.Transparent
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                if (interactionsEnabled) {
                                    submitEmail()
                                }
                            }
                        )
                    )

                    Spacer(Modifier.height(28.dp))

                    Text(
                        text = "We will email you a 6-digit one-time code.\n" +
                                "No password is needed.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.onSurfaceVariant
                    )

                    Spacer(Modifier.height(80.dp))

                    Button(
                        onClick = submitEmail,
                        enabled = interactionsEnabled,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text(
                            text = "Send verification code",
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(Modifier.height(12.dp))

                    TextButton(
                        onClick = {
                            focusManager.clearFocus()
                            onAction(
                                EmailLoginAction.BackToPasswordClicked
                            )
                        },
                        enabled = interactionsEnabled,
                        modifier = Modifier.align(
                            Alignment.CenterHorizontally
                        ),
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = colors.secondary
                        )
                    ) {
                        Text(
                            text = "Back to password login",
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(Modifier.height(28.dp))

                    Text(
                        text = "Only use an email linked to your account.",
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
private fun EmailLoginMethodSelector(
    enabled: Boolean,
    onPasswordClick: () -> Unit
) {
    val colors = MaterialTheme.colorScheme

    Surface(
        color = colors.surfaceVariant,
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            TextButton(
                onClick = onPasswordClick,
                enabled = enabled,
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.textButtonColors(
                    contentColor = colors.onSurfaceVariant
                )
            ) {
                Text(
                    text = "Password",
                    fontWeight = FontWeight.SemiBold
                )
            }

            Surface(
                color = colors.primary,
                contentColor = colors.onPrimary,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Email code",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Preview(
    name = "Email login - Dark",
    showBackground = true
)
@Composable
private fun EmailLoginDarkPreview() {
    TARUMTFoodOrderingSystemTheme(darkTheme = true) {
        EmailLoginScreen(
            uiState = EmailLoginUiState(
                email = "student@tarc.edu.my"
            ),
            isDarkTheme = true,
            onDarkThemeChanged = {},
            onAction = {}
        )
    }
}

@Preview(
    name = "Email login - Light",
    showBackground = true
)
@Composable
private fun EmailLoginLightPreview() {
    TARUMTFoodOrderingSystemTheme(darkTheme = false) {
        EmailLoginScreen(
            uiState = EmailLoginUiState(),
            isDarkTheme = false,
            onDarkThemeChanged = {},
            onAction = {}
        )
    }
}