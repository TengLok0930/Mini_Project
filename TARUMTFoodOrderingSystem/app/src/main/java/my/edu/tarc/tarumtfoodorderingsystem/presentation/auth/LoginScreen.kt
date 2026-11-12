package my.edu.tarc.tarumtfoodorderingsystem.presentation.auth

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import my.edu.tarc.tarumtfoodorderingsystem.R

@Composable
fun LoginScreen(
    uiState: LoginUiState,
    isDarkTheme: Boolean,
    onDarkThemeChanged: (Boolean) -> Unit,
    onAction: (LoginAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    val focusManager = LocalFocusManager.current

    val interactionsEnabled =
        !uiState.isLoading && !uiState.isLoginSuccessful

    val submitLogin: () -> Unit = {
        focusManager.clearFocus()
        onAction(LoginAction.LoginClicked)
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

                    LoginMethodSelector(
                        enabled = interactionsEnabled,
                        onEmailCodeClick = {
                            onAction(LoginAction.EmailCodeClicked)
                        }
                    )

                    Spacer(Modifier.height(28.dp))

                    LoginFieldLabel(text = "User ID")

                    Spacer(Modifier.height(8.dp))

                    TextField(
                        value = uiState.userId,
                        onValueChange = {
                            onAction(LoginAction.UserIdChanged(it))
                        },
                        enabled = interactionsEnabled,
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        placeholder = {
                            Text("Enter your user ID")
                        },
                        isError = uiState.userIdError != null,
                        supportingText = uiState.userIdError?.let { error ->
                            { Text(error) }
                        },
                        shape = RoundedCornerShape(14.dp),
                        colors = loginTextFieldColors(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        )
                    )

                    Spacer(Modifier.height(22.dp))

                    LoginFieldLabel(text = "Password")

                    Spacer(Modifier.height(8.dp))

                    TextField(
                        value = uiState.password,
                        onValueChange = {
                            onAction(LoginAction.PasswordChanged(it))
                        },
                        enabled = interactionsEnabled,
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        placeholder = {
                            Text("Enter your password")
                        },
                        visualTransformation =
                            if (uiState.isPasswordVisible) {
                                VisualTransformation.None
                            } else {
                                PasswordVisualTransformation()
                            },
                        trailingIcon = {
                            IconButton(
                                enabled = interactionsEnabled,
                                onClick = {
                                    onAction(
                                        LoginAction.TogglePasswordVisibility
                                    )
                                }
                            ) {
                                Icon(
                                    painter = painterResource(
                                        R.drawable.visibility_24px
                                    ),
                                    contentDescription =
                                        if (uiState.isPasswordVisible) {
                                            "Hide password"
                                        } else {
                                            "Show password"
                                        },
                                    tint =
                                        if (uiState.isPasswordVisible) {
                                            colors.tertiary
                                        } else {
                                            colors.onSurfaceVariant
                                        }
                                )
                            }
                        },
                        isError = uiState.passwordError != null,
                        supportingText =
                            uiState.passwordError?.let { error ->
                                { Text(error) }
                            },
                        shape = RoundedCornerShape(14.dp),
                        colors = loginTextFieldColors(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                if (interactionsEnabled) {
                                    submitLogin()
                                }
                            }
                        )
                    )

                    uiState.loginError?.let { message ->
                        Spacer(Modifier.height(12.dp))

                        Text(
                            text = message,
                            color = colors.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Spacer(Modifier.height(48.dp))

                    Button(
                        onClick = submitLogin,
                        enabled = interactionsEnabled,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        if (uiState.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(22.dp),
                                strokeWidth = 2.dp,
                                color = colors.onSurface
                            )
                        } else {
                            Text(
                                text = "Login",
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    TextButton(
                        onClick = {
                            onAction(LoginAction.RegisterClicked)
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
                            text = "Register User",
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(Modifier.height(28.dp))

                    Text(
                        text = "Use your registered TAR UMT " +
                                "Student/Lecturer ID.",
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
private fun LoginMethodSelector(
    enabled: Boolean,
    onEmailCodeClick: () -> Unit
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
            // Already selected; this is not another destination.
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
                        text = "Password",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            TextButton(
                onClick = onEmailCodeClick,
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
                    text = "Email code",
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun LoginFieldLabel(
    text: String
) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        fontWeight = FontWeight.SemiBold
    )
}

@Composable
private fun loginTextFieldColors() = TextFieldDefaults.colors(
    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
    disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
    errorContainerColor = MaterialTheme.colorScheme.surfaceVariant,

    focusedIndicatorColor = Color.Transparent,
    unfocusedIndicatorColor = Color.Transparent,
    disabledIndicatorColor = Color.Transparent,
    errorIndicatorColor = Color.Transparent
)
