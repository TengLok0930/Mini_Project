package my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.storeMaintenance.addStore

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.storeMaintenance.StoreCanteenOption
import my.edu.tarc.tarumtfoodorderingsystem.ui.components.AppActionButton
import my.edu.tarc.tarumtfoodorderingsystem.ui.components.AppFieldLabel
import my.edu.tarc.tarumtfoodorderingsystem.ui.components.AppTopBar

@Composable
fun AdminAddStoreScreen(
    uiState: AdminAddStoreUiState,
    onAction: (AdminAddStoreAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    val focusManager = LocalFocusManager.current

    val interactionsEnabled = uiState.pendingStore == null

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = colors.background,
        topBar = {
            AppTopBar(
                title = "New Store",
                subtitle = uiState.selectedCanteen?.name,
                onBackClick = {
                    focusManager.clearFocus()
                    onAction(AdminAddStoreAction.BackClicked)
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
                    AppFieldLabel(
                        title = "New Store Name",
                        required = true
                    )

                    Spacer(Modifier.height(8.dp))

                    TextField(
                        value = uiState.name,
                        onValueChange = {
                            onAction(
                                AdminAddStoreAction.NameChanged(it)
                            )
                        },
                        enabled = interactionsEnabled,
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        placeholder = {
                            Text("Enter Store Name")
                        },
                        isError = uiState.nameError != null,
                        supportingText = uiState.nameError?.let { error ->
                            { Text(error) }
                        },
                        shape = RoundedCornerShape(14.dp),
                        colors = addStoreTextFieldColors(),
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Words,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                            }
                        )
                    )

                    Spacer(Modifier.height(24.dp))

                    AppFieldLabel(
                        title = "Linked Canteen",
                        required = true
                    )

                    Spacer(Modifier.height(8.dp))

                    AddStoreCanteenDropdown(
                        canteens = uiState.canteens,
                        selectedCanteen = uiState.selectedCanteen,
                        error = uiState.canteenError,
                        enabled = interactionsEnabled,
                        onCanteenSelected = { canteenId ->
                            focusManager.clearFocus()

                            onAction(
                                AdminAddStoreAction.CanteenSelected(
                                    canteenId = canteenId
                                )
                            )
                        }
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

                            Spacer(Modifier.height(6.dp))

                            Text(
                                text = "Crop to square\nSaved up to\n100px × 100px",
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
                                        enabled = interactionsEnabled &&
                                                !uiState.isReadingImage,
                                        role = Role.Button,
                                        onClickLabel = "Choose store image"
                                    ) {
                                        focusManager.clearFocus()

                                        onAction(
                                            AdminAddStoreAction.PickImageClicked
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
                                            style = MaterialTheme
                                                .typography
                                                .bodySmall,
                                            color = Color.DarkGray,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }

                            if (
                                uiState.imageUri != null ||
                                uiState.imageError != null
                            ) {
                                TextButton(
                                    enabled = interactionsEnabled &&
                                            !uiState.isReadingImage,
                                    onClick = {
                                        onAction(
                                            AdminAddStoreAction.RemoveImageClicked
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
                    title = "+ Create New Store",
                    onClick = {
                        focusManager.clearFocus()
                        onAction(AdminAddStoreAction.CreateClicked)
                    },
                    enabled = interactionsEnabled,
                    isLoading = uiState.isReadingImage,
                    modifier = Modifier.padding(
                        horizontal = 22.dp,
                        vertical = 20.dp
                    )
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddStoreCanteenDropdown(
    canteens: List<StoreCanteenOption>,
    selectedCanteen: StoreCanteenOption?,
    error: String?,
    enabled: Boolean,
    onCanteenSelected: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    val dropdownEnabled = enabled && canteens.isNotEmpty()

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = it && dropdownEnabled
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        TextField(
            value = selectedCanteen?.name.orEmpty(),
            onValueChange = {},
            readOnly = true,
            enabled = dropdownEnabled,
            singleLine = true,
            placeholder = {
                Text("Choose Canteen")
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            isError = error != null,
            supportingText = error?.let { message ->
                { Text(message) }
            },
            shape = RoundedCornerShape(14.dp),
            colors = addStoreTextFieldColors(),
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(
                    type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                    enabled = dropdownEnabled
                )
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            canteens.forEach { canteen ->
                DropdownMenuItem(
                    text = {
                        Text(canteen.name)
                    },
                    onClick = {
                        expanded = false
                        onCanteenSelected(canteen.id)
                    }
                )
            }
        }
    }
}

@Composable
private fun addStoreTextFieldColors() = TextFieldDefaults.colors(
    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
    disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
    errorContainerColor = MaterialTheme.colorScheme.surfaceVariant,

    focusedIndicatorColor = Color.Transparent,
    unfocusedIndicatorColor = Color.Transparent,
    disabledIndicatorColor = Color.Transparent,
    errorIndicatorColor = Color.Transparent
)