package my.edu.tarc.tarumtfoodorderingsystem.presentation.worker.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import my.edu.tarc.tarumtfoodorderingsystem.ui.components.AppTopBar

@Composable
fun WorkerProfileScreen(
    uiState: WorkerProfileUiState,
    isDarkTheme: Boolean,
    onAction: (WorkerProfileAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.showPasswordChangedMessage) {
        if (uiState.showPasswordChangedMessage) {
            snackbarHostState.showSnackbar(
                "Password changed successfully"
            )
            onAction(WorkerProfileAction.PasswordChangedMessageShown)
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = colors.background,
        topBar = {
            AppTopBar(
                title = "Store Profile",
                onBackClick = {
                    onAction(WorkerProfileAction.BackClicked)
                },
                onLogoutClick = {
                    onAction(WorkerProfileAction.LogoutClicked)
                }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding),
            contentAlignment = Alignment.TopCenter
        ) {
            LazyColumn(
                modifier = Modifier
                    .widthIn(max = 560.dp)
                    .fillMaxSize(),
                contentPadding = PaddingValues(
                    horizontal = 20.dp,
                    vertical = 24.dp
                ),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                item(key = "account_heading") {
                    Text(
                        text = "My Account",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = colors.secondary
                    )
                }

                item(key = "account_card") {
                    WorkerProfileCard {
                        Text(
                            text = uiState.accountTitle,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = colors.onSurface
                        )

                        Spacer(Modifier.height(10.dp))

                        Text(
                            text = uiState.accountRole,
                            style = MaterialTheme.typography.bodySmall,
                            color = colors.onSurfaceVariant
                        )

                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            TextButton(
                                onClick = {
                                    onAction(
                                        WorkerProfileAction
                                            .ChangePasswordClicked
                                    )
                                }
                            ) {
                                Text(
                                    text = "Change password",
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = colors.secondary
                                )
                            }
                        }
                    }
                }

                item(key = "appearance_card") {
                    WorkerProfileCard {
                        Text(
                            text = "Appearance",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = colors.onSurface
                        )

                        Spacer(Modifier.height(14.dp))

                        WorkerAppearanceSelector(
                            isDarkTheme = isDarkTheme,
                            onDarkThemeChanged = { darkTheme ->
                                onAction(
                                    WorkerProfileAction.ThemeSelected(
                                        isDarkTheme = darkTheme
                                    )
                                )
                            }
                        )
                    }
                }
            }
        }
    }

    if (uiState.showLogoutDialog) {
        AlertDialog(
            onDismissRequest = {
                onAction(WorkerProfileAction.DismissLogoutClicked)
            },
            title = {
                Text("Log out?")
            },
            text = {
                Text("Are you sure you want to log out?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onAction(WorkerProfileAction.ConfirmLogoutClicked)
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = colors.error
                    )
                ) {
                    Text("Log out")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        onAction(WorkerProfileAction.DismissLogoutClicked)
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    if (uiState.showChangePasswordDialog) {
        WorkerChangePasswordDialog(
            uiState = uiState,
            onAction = onAction
        )
    }
}

@Composable
private fun WorkerProfileCard(
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            content = content
        )
    }
}

@Composable
private fun WorkerAppearanceSelector(
    isDarkTheme: Boolean,
    onDarkThemeChanged: (Boolean) -> Unit
) {
    val colors = MaterialTheme.colorScheme

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(colors.background)
            .selectableGroup()
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        WorkerAppearanceOption(
            label = "Dark",
            selected = isDarkTheme,
            onClick = { onDarkThemeChanged(true) },
            modifier = Modifier.weight(1f)
        )

        WorkerAppearanceOption(
            label = "Light",
            selected = !isDarkTheme,
            onClick = { onDarkThemeChanged(false) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun WorkerAppearanceOption(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(9.dp))
            .background(
                if (selected) colors.primary else colors.background
            )
            .selectable(
                selected = selected,
                role = Role.RadioButton,
                onClick = onClick
            )
            .heightIn(min = 44.dp)
            .padding(
                horizontal = 12.dp,
                vertical = 10.dp
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            color = if (selected) {
                colors.onPrimary
            } else {
                colors.onSurfaceVariant
            }
        )
    }
}

@Composable
private fun WorkerChangePasswordDialog(
    uiState: WorkerProfileUiState,
    onAction: (WorkerProfileAction) -> Unit
) {
    AlertDialog(
        onDismissRequest = {
            onAction(WorkerProfileAction.DismissChangePasswordClicked)
        },
        title = {
            Text("Change password")
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = uiState.currentPassword,
                    onValueChange = {
                        onAction(
                            WorkerProfileAction.CurrentPasswordChanged(it)
                        )
                    },
                    label = { Text("Current password") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = uiState.newPassword,
                    onValueChange = {
                        onAction(
                            WorkerProfileAction.NewPasswordChanged(it)
                        )
                    },
                    label = { Text("New password") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = uiState.confirmNewPassword,
                    onValueChange = {
                        onAction(
                            WorkerProfileAction.ConfirmNewPasswordChanged(it)
                        )
                    },
                    label = { Text("Confirm new password") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                uiState.passwordError?.let { error ->
                    Text(
                        text = error,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onAction(WorkerProfileAction.SubmitPasswordChangeClicked)
                }
            ) {
                Text("Change")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onAction(WorkerProfileAction.DismissChangePasswordClicked)
                }
            ) {
                Text("Cancel")
            }
        }
    )
}