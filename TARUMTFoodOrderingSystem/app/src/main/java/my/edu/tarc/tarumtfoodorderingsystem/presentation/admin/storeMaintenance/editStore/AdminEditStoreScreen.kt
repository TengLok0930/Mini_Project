package my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.storeMaintenance.editStore

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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import my.edu.tarc.tarumtfoodorderingsystem.ui.components.AppActionButton
import my.edu.tarc.tarumtfoodorderingsystem.ui.components.AppFieldLabel
import my.edu.tarc.tarumtfoodorderingsystem.ui.components.AppTopBar

@Composable
fun AdminEditStoreScreen(
    uiState: AdminEditStoreUiState,
    onAction: (AdminEditStoreAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    val focusManager = LocalFocusManager.current

    val enabled = uiState.storeId != null &&
            uiState.pendingUpdate == null

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = colors.background,
        topBar = {
            AppTopBar(
                title = "Edit Store",
                subtitle = uiState.canteenName.takeIf {
                    it.isNotBlank()
                },
                onBackClick = {
                    focusManager.clearFocus()
                    onAction(AdminEditStoreAction.BackClicked)
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
                    if (uiState.storeId == null) {
                        Text(
                            text = "Store not found. Please return " +
                                    "to Store Maintenance.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = colors.error
                        )

                        Spacer(Modifier.height(20.dp))
                    }

                    AppFieldLabel(
                        title = "Edit Store Name",
                        required = true
                    )

                    Spacer(Modifier.height(8.dp))

                    TextField(
                        value = uiState.name,
                        onValueChange = {
                            onAction(
                                AdminEditStoreAction.NameChanged(it)
                            )
                        },
                        enabled = enabled,
                        singleLine = true,
                        placeholder = {
                            Text("Enter Store Name")
                        },
                        isError = uiState.nameError != null,
                        supportingText = uiState.nameError?.let { error ->
                            { Text(error) }
                        },
                        shape = RoundedCornerShape(14.dp),
                        colors = editStoreTextFieldColors(),
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

                    Spacer(Modifier.height(24.dp))

                    AppFieldLabel(
                        title = "Linked Canteen",
                        required = false
                    )

                    Spacer(Modifier.height(8.dp))

                    TextField(
                        value = uiState.canteenName,
                        onValueChange = {},
                        readOnly = true,
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = editStoreTextFieldColors(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(36.dp))

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
                                        onClickLabel = "Choose store image"
                                    ) {
                                        focusManager.clearFocus()

                                        onAction(
                                            AdminEditStoreAction.PickImageClicked
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
                                                "Selected store image",
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
                                            AdminEditStoreAction.RemoveImageClicked
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
                    title = "Update Store",
                    enabled = enabled && !uiState.isReadingImage,
                    onClick = {
                        focusManager.clearFocus()
                        onAction(AdminEditStoreAction.UpdateClicked)
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
}

@Composable
private fun editStoreTextFieldColors() = TextFieldDefaults.colors(
    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
    disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
    errorContainerColor = MaterialTheme.colorScheme.surfaceVariant,

    focusedIndicatorColor = Color.Transparent,
    unfocusedIndicatorColor = Color.Transparent,
    disabledIndicatorColor = Color.Transparent,
    errorIndicatorColor = Color.Transparent
)