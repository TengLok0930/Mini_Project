package my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.canteenMaintenance.editCanteen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import my.edu.tarc.tarumtfoodorderingsystem.ui.components.AppActionButton
import my.edu.tarc.tarumtfoodorderingsystem.ui.components.AppFieldLabel
import my.edu.tarc.tarumtfoodorderingsystem.ui.components.AppTopBar
import my.edu.tarc.tarumtfoodorderingsystem.R

@Composable
fun AdminEditCanteenScreen(
    uiState: AdminEditCanteenUiState,
    onAction: (AdminEditCanteenAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    val focusManager = LocalFocusManager.current

    val enabled = uiState.canteenId != null &&
            uiState.pendingUpdate == null &&
            uiState.pendingDelete == null

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = colors.background,
        topBar = {
            AppTopBar(
                title = "Edit Canteen",
                subtitle = uiState.originalName.takeIf { it.isNotBlank() },
                onBackClick = {
                    focusManager.clearFocus()
                    onAction(AdminEditCanteenAction.BackClicked)
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
                .imePadding(),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = 560.dp)
                    .fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(
                            horizontal = 22.dp,
                            vertical = 24.dp
                        )
                ) {
                    if (uiState.canteenId == null) {
                        Text(
                            text = "Canteen not found. Please return " +
                                    "to Canteen Maintenance.",
                            color = colors.error,
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Spacer(Modifier.height(20.dp))
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier.weight(1f)) {
                            AppFieldLabel(
                                title = "Edit Canteen Name",
                                required = true
                            )
                        }

                        IconButton(
                            enabled = enabled && !uiState.isReadingImage,
                            onClick = {
                                focusManager.clearFocus()
                                onAction(AdminEditCanteenAction.DeleteClicked)
                            }
                        ) {
                            Icon(
                                painter = painterResource(
                                    R.drawable.delete_40px
                                ),
                                contentDescription = "Delete canteen",
                                tint = colors.error,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }

                    Spacer(Modifier.height(4.dp))

                    Spacer(Modifier.height(8.dp))

                    TextField(
                        value = uiState.name,
                        onValueChange = {
                            onAction(
                                AdminEditCanteenAction.NameChanged(it)
                            )
                        },
                        enabled = enabled,
                        singleLine = true,
                        placeholder = {
                            Text("Enter Canteen Name")
                        },
                        isError = uiState.nameError != null,
                        supportingText = uiState.nameError?.let { error ->
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
                            capitalization = KeyboardCapitalization.Words,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                            }
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(40.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            AppFieldLabel(
                                title = "Attach image",
                                required = true
                            )

                            Spacer(Modifier.height(8.dp))

                            Text(
                                text = "Crop to square\n" +
                                        "Saved up to\n" +
                                        "100px × 100px",
                                style = MaterialTheme.typography.bodySmall,
                                color = colors.onSurfaceVariant
                            )
                        }

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(140.dp)
                                    .clip(RoundedCornerShape(18.dp))
                                    .background(Color.White)
                                    .clickable(
                                        enabled = enabled &&
                                                !uiState.isReadingImage,
                                        role = Role.Button,
                                        onClickLabel = "Choose canteen image"
                                    ) {
                                        focusManager.clearFocus()
                                        onAction(
                                            AdminEditCanteenAction.PickImageClicked
                                        )
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                when {
                                    uiState.isReadingImage -> {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(28.dp),
                                            color = colors.primary
                                        )
                                    }

                                    uiState.imageUri != null -> {
                                        AsyncImage(
                                            model = uiState.imageUri,
                                            contentDescription =
                                                "Selected canteen image",
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }

                                    else -> {
                                        Text(
                                            text = "Tap to attach",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color.DarkGray
                                        )
                                    }
                                }
                            }

                            if (
                                uiState.imageUri != null ||
                                uiState.imageError != null
                            ) {
                                TextButton(
                                    enabled = enabled &&
                                            !uiState.isReadingImage,
                                    onClick = {
                                        onAction(
                                            AdminEditCanteenAction.RemoveImageClicked
                                        )
                                    }
                                ) {
                                    Text("Remove image")
                                }
                            }
                        }
                    }

                    uiState.imageError?.let { error ->
                        Spacer(Modifier.height(12.dp))

                        Text(
                            text = error,
                            style = MaterialTheme.typography.bodySmall,
                            color = colors.error
                        )
                    }
                }

                AppActionButton(
                    title = "Update Canteen",
                    enabled = enabled && !uiState.isReadingImage,
                    onClick = {
                        focusManager.clearFocus()
                        onAction(AdminEditCanteenAction.UpdateClicked)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 22.dp,
                            vertical = 20.dp
                        )
                )
            }
        }
    }

    if (uiState.showDeleteDialog) {
        AlertDialog(
            onDismissRequest = {
                onAction(AdminEditCanteenAction.DismissDeleteClicked)
            },
            title = {
                Text("Delete canteen?")
            },
            text = {
                Text(
                    text = "\"${uiState.originalName}\" will be " +
                            "permanently deleted. This cannot be undone."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onAction(AdminEditCanteenAction.ConfirmDeleteClicked)
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = colors.error
                    )
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        onAction(AdminEditCanteenAction.DismissDeleteClicked)
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}