package my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.menuMaintenance.addMenuItem

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.menuMaintenance.AdminMenuStoreOption
import my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.menuMaintenance.adminMenuSampleState
import my.edu.tarc.tarumtfoodorderingsystem.ui.components.AppActionButton
import my.edu.tarc.tarumtfoodorderingsystem.ui.components.AppFieldLabel
import my.edu.tarc.tarumtfoodorderingsystem.ui.components.AppTopBar

@Composable
fun AdminAddItemScreen(
    uiState: AdminAddItemUiState,
    onAction: (AdminAddItemAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    val focusManager = LocalFocusManager.current

    val enabled = uiState.pendingItem == null

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = colors.background,
        topBar = {
            AppTopBar(
                title = "Menu Maintenance",
                subtitle = uiState.selectedStore?.displayName,
                onBackClick = {
                    focusManager.clearFocus()
                    onAction(AdminAddItemAction.BackClicked)
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
                        ),
                    verticalArrangement = Arrangement.spacedBy(22.dp)
                ) {
                    AddItemTextField(
                        label = "New Item Name",
                        value = uiState.name,
                        placeholder = "Enter Item Name",
                        error = uiState.nameError,
                        enabled = enabled,
                        onValueChange = {
                            onAction(
                                AdminAddItemAction.NameChanged(it)
                            )
                        }
                    )

                    AddItemDropdown(
                        label = "Linked Store",
                        selectedText = uiState.selectedStore
                            ?.displayName
                            .orEmpty(),
                        placeholder = "Choose Store",
                        options = uiState.stores,
                        optionText = { store ->
                            store.displayName
                                     },
                        error = uiState.storeError,
                        enabled = enabled,
                        onSelected = { store ->
                            focusManager.clearFocus()

                            onAction(
                                AdminAddItemAction.StoreSelected(
                                    storeId = store.id
                                )
                            )
                        }
                    )

                    AddItemTextField(
                        label = "Category",
                        value = uiState.category,
                        placeholder = "e.g. Rice or Noodle",
                        error = uiState.categoryError,
                        enabled = enabled,
                        onValueChange = { value ->
                            onAction(
                                AdminAddItemAction.CategoryChanged(
                                    value = value
                                )
                            )
                        }
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(20.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        AddItemDropdown<MenuItemType>(
                            label = "Item Type",
                            selectedText = uiState.itemType
                                ?.displayName
                                .orEmpty(),
                            placeholder = "Food",
                            options = MenuItemType.values().toList(),
                            optionText = { itemType ->
                                itemType.displayName },
                            error = uiState.itemTypeError,
                            enabled = enabled,
                            onSelected = { itemType ->
                                focusManager.clearFocus()

                                onAction(
                                    AdminAddItemAction.ItemTypeSelected(
                                        itemType = itemType
                                    )
                                )
                            },
                            modifier = Modifier.weight(1f)
                        )

                        AddItemTextField(
                            label = "Price (RM)",
                            value = uiState.price,
                            placeholder = "0.00",
                            error = uiState.priceError,
                            enabled = enabled,
                            keyboardType = KeyboardType.Decimal,
                            onValueChange = {
                                onAction(
                                    AdminAddItemAction.PriceChanged(it)
                                )
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }

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
                                        onClickLabel = "Choose menu item image"
                                    ) {
                                        focusManager.clearFocus()

                                        onAction(
                                            AdminAddItemAction.PickImageClicked
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
                                                "Selected menu item image",
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
                                            AdminAddItemAction.RemoveImageClicked
                                        )
                                    }
                                ) {
                                    Text("Remove image")
                                }
                            }
                        }
                    }

                    uiState.imageError?.let { error ->
                        Text(
                            text = error,
                            style = MaterialTheme.typography.bodySmall,
                            color = colors.error
                        )
                    }
                }

                AppActionButton(
                    title = "Publish item",
                    onClick = {
                        focusManager.clearFocus()
                        onAction(AdminAddItemAction.PublishClicked)
                    },
                    enabled = enabled && !uiState.isReadingImage,
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
private fun AddItemTextField(
    label: String,
    value: String,
    placeholder: String,
    error: String?,
    enabled: Boolean,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    val focusManager = LocalFocusManager.current

    Column(modifier = modifier) {
        AppFieldLabel(
            title = label,
            required = true
        )

        Spacer(Modifier.height(8.dp))

        TextField(
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
            singleLine = true,
            placeholder = {
                Text(placeholder)
            },
            isError = error != null,
            supportingText = error?.let { message ->
                { Text(message) }
            },
            shape = RoundedCornerShape(14.dp),
            colors = addItemTextFieldColors(),
            keyboardOptions = KeyboardOptions(
                capitalization = if (keyboardType == KeyboardType.Text) {
                    KeyboardCapitalization.Words
                } else {
                    KeyboardCapitalization.None
                },
                keyboardType = keyboardType,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun <T> AddItemDropdown(
    label: String,
    selectedText: String,
    placeholder: String,
    options: List<T>,
    optionText: (T) -> String,
    error: String?,
    enabled: Boolean,
    onSelected: (T) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    val dropdownEnabled = enabled && options.isNotEmpty()

    Column(modifier = modifier) {
        AppFieldLabel(
            title = label,
            required = true
        )

        Spacer(Modifier.height(8.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = it && dropdownEnabled
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            TextField(
                value = selectedText,
                onValueChange = {},
                readOnly = true,
                enabled = dropdownEnabled,
                singleLine = true,
                placeholder = {
                    Text(placeholder)
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
                colors = addItemTextFieldColors(),
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
                },
                modifier = Modifier.heightIn(max = 320.dp)
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Text(optionText(option))
                        },
                        onClick = {
                            expanded = false
                            onSelected(option)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun addItemTextFieldColors() = TextFieldDefaults.colors(
    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
    disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
    errorContainerColor = MaterialTheme.colorScheme.surfaceVariant,

    focusedIndicatorColor = Color.Transparent,
    unfocusedIndicatorColor = Color.Transparent,
    disabledIndicatorColor = Color.Transparent,
    errorIndicatorColor = Color.Transparent
)