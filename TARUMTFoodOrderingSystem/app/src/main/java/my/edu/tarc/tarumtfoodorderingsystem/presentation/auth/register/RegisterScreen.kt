package my.edu.tarc.tarumtfoodorderingsystem.presentation.auth.register

import android.R.attr.maxHeight
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import my.edu.tarc.tarumtfoodorderingsystem.R
import my.edu.tarc.tarumtfoodorderingsystem.ui.components.AppBackButton

@Composable
fun RegisterScreen(
    uiState: RegisterUiState,
    onAction: (RegisterAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    val focusManager = LocalFocusManager.current

    val interactionsEnabled =
        !uiState.isLoading && !uiState.isRegistrationSuccessful

    val submitRegistration: () -> Unit = {
        focusManager.clearFocus()
        onAction(RegisterAction.RegisterClicked)
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

            BoxWithConstraints(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.safeDrawing)
                    .imePadding(),
                contentAlignment = Alignment.TopCenter
            ) {
                val availableHeight = maxHeight

                Column(
                    modifier = Modifier
                        .widthIn(max = 480.dp)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .heightIn(min = availableHeight)
                        .padding(
                            horizontal = 18.dp,
                            vertical = 16.dp
                        )
                ) {
                    AppBackButton(
                        enabled = interactionsEnabled,
                        onClick = {
                            onAction(RegisterAction.BackClicked)
                        }
                    )

                    Spacer(Modifier.height(16.dp))

                    Text(
                        text = "Registration",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = colors.onBackground
                    )

                    Spacer(Modifier.height(28.dp))

                    RegisterFieldLabel(text = "User ID")

                    Spacer(Modifier.height(8.dp))

                    TextField(
                        value = uiState.userId,
                        onValueChange = {
                            onAction(RegisterAction.UserIdChanged(it))
                        },
                        enabled = interactionsEnabled,
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        placeholder = {
                            Text("Enter your TARUMT ID")
                        },
                        isError = uiState.userIdError != null,
                        supportingText = uiState.userIdError?.let { error ->
                            { Text(error) }
                        },
                        shape = RoundedCornerShape(14.dp),
                        colors = registerTextFieldColors(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        )
                    )

                    Spacer(Modifier.height(20.dp))

                    RegisterFieldLabel(text = "Email Address")

                    Spacer(Modifier.height(8.dp))

                    TextField(
                        value = uiState.email,
                        onValueChange = {
                            onAction(RegisterAction.EmailChanged(it))
                        },
                        enabled = interactionsEnabled,
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        placeholder = {
                            Text("Enter your TARUMT email address")
                        },
                        isError = uiState.emailError != null,
                        supportingText = uiState.emailError?.let { error ->
                            { Text(error) }
                        },
                        shape = RoundedCornerShape(14.dp),
                        colors = registerTextFieldColors(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        )
                    )

                    Spacer(Modifier.height(20.dp))

                    RegisterFieldLabel(text = "Password")

                    Spacer(Modifier.height(8.dp))

                    TextField(
                        value = uiState.password,
                        onValueChange = {
                            onAction(RegisterAction.PasswordChanged(it))
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
                            RegisterVisibilityButton(
                                isVisible = uiState.isPasswordVisible,
                                enabled = interactionsEnabled,
                                onClick = {
                                    onAction(
                                        RegisterAction.TogglePasswordVisibility
                                    )
                                }
                            )
                        },
                        isError = uiState.passwordError != null,
                        supportingText = uiState.passwordError?.let { error ->
                            { Text(error) }
                        },
                        shape = RoundedCornerShape(14.dp),
                        colors = registerTextFieldColors(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Next
                        )
                    )

                    Spacer(Modifier.height(20.dp))

                    RegisterFieldLabel(text = "Confirm Password")

                    Spacer(Modifier.height(8.dp))

                    TextField(
                        value = uiState.confirmPassword,
                        onValueChange = {
                            onAction(
                                RegisterAction.ConfirmPasswordChanged(it)
                            )
                        },
                        enabled = interactionsEnabled,
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        placeholder = {
                            Text("Re-enter your password")
                        },
                        visualTransformation =
                            if (uiState.isConfirmPasswordVisible) {
                                VisualTransformation.None
                            } else {
                                PasswordVisualTransformation()
                            },
                        trailingIcon = {
                            RegisterVisibilityButton(
                                isVisible =
                                    uiState.isConfirmPasswordVisible,
                                enabled = interactionsEnabled,
                                onClick = {
                                    onAction(
                                        RegisterAction
                                            .ToggleConfirmPasswordVisibility
                                    )
                                }
                            )
                        },
                        isError = uiState.confirmPasswordError != null,
                        supportingText =
                            uiState.confirmPasswordError?.let { error ->
                                { Text(error) }
                            },
                        shape = RoundedCornerShape(14.dp),
                        colors = registerTextFieldColors(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                if (interactionsEnabled) {
                                    submitRegistration()
                                }
                            }
                        )
                    )

                    uiState.registerError?.let { message ->
                        Spacer(Modifier.height(12.dp))

                        Text(
                            text = message,
                            style = MaterialTheme.typography.bodySmall,
                            color = colors.error
                        )
                    }

                    Spacer(
                        modifier = Modifier
                            .weight(1f)
                            .heightIn(min = 48.dp)
                    )

                    Button(
                        onClick = submitRegistration,
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
                                text = "Register",
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(Modifier.height(80.dp))
                }
            }
        }
    }
}

@Composable
private fun RegisterVisibilityButton(
    isVisible: Boolean,
    enabled: Boolean,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        enabled = enabled
    ) {
        Icon(
            painter = painterResource(R.drawable.visibility_24px),
            contentDescription = if (isVisible) {
                "Hide password"
            } else {
                "Show password"
            },
            tint = if (enabled) {
                MaterialTheme.colorScheme.tertiary
            } else {
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
            }
        )
    }
}

@Composable
private fun RegisterFieldLabel(
    text: String
) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
private fun registerTextFieldColors() = TextFieldDefaults.colors(
    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
    disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
    errorContainerColor = MaterialTheme.colorScheme.surfaceVariant,

    focusedIndicatorColor = Color.Transparent,
    unfocusedIndicatorColor = Color.Transparent,
    disabledIndicatorColor = Color.Transparent,
    errorIndicatorColor = Color.Transparent
)