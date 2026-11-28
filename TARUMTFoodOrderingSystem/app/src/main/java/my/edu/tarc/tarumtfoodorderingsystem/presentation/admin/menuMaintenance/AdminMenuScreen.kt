package my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.menuMaintenance

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.util.Locale
import my.edu.tarc.tarumtfoodorderingsystem.ui.components.AppActionButton
import my.edu.tarc.tarumtfoodorderingsystem.ui.components.AppTopBar
import my.edu.tarc.tarumtfoodorderingsystem.ui.theme.LocalPriceColor

@Composable
fun AdminMenuScreen(
    uiState: AdminMenuUiState,
    onAction: (AdminMenuAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    val listState = rememberLazyListState()

    val selectedStore = uiState.selectedStore
    val groupedItems = uiState.groupedPublishedItems

    LaunchedEffect(uiState.selectedStoreId) {
        listState.scrollToItem(0)
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = colors.background,
        topBar = {
            AppTopBar(
                title = "Menu Maintenance",
                subtitle = selectedStore?.displayName,
                onBackClick = {
                    onAction(AdminMenuAction.BackClicked)
                }
            )
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
                state = listState,
                modifier = Modifier
                    .widthIn(max = 560.dp)
                    .fillMaxSize(),
                contentPadding = PaddingValues(
                    horizontal = 22.dp,
                    vertical = 24.dp
                ),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item(
                    key = "store_selector",
                    contentType = "selector"
                ) {
                    Column {
                        Text(
                            text = if (selectedStore == null) {
                                "Choose a store"
                            } else {
                                "Store menu"
                            },
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = colors.secondary
                        )

                        Spacer(Modifier.height(16.dp))

                        AdminMenuStoreDropdown(
                            stores = uiState.stores,
                            selectedStore = selectedStore,
                            onStoreSelected = { storeId ->
                                onAction(
                                    AdminMenuAction.StoreSelected(
                                        storeId = storeId
                                    )
                                )
                            }
                        )
                    }
                }

                if (selectedStore == null) {
                    if (uiState.stores.isEmpty()) {
                        item(
                            key = "no_stores",
                            contentType = "message"
                        ) {
                            Text(
                                text = "No stores available. " +
                                        "Create a store before adding menu items.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = colors.onSurfaceVariant
                            )
                        }
                    }
                } else {
                    item(
                        key = "create_menu_item",
                        contentType = "action"
                    ) {
                        AppActionButton(
                            title = "+ Create menu item",
                            onClick = {
                                onAction(
                                    AdminMenuAction.CreateMenuItemClicked
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 6.dp)
                        )
                    }

                    item(
                        key = "published_heading",
                        contentType = "heading"
                    ) {
                        Text(
                            text = "Published items",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = colors.secondary,
                            modifier = Modifier.padding(top = 16.dp)
                        )
                    }

                    if (groupedItems.isEmpty()) {
                        item(
                            key = "no_menu_items",
                            contentType = "message"
                        ) {
                            Text(
                                text = "No published menu items for this store.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = colors.onSurfaceVariant,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                    } else {
                        groupedItems.forEach { (category, menuItems) ->
                            item(
                                key = "category:$category",
                                contentType = "category"
                            ) {
                                Text(
                                    text = "$category (${menuItems.size})",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = colors.secondary,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                            }

                            items(
                                items = menuItems,
                                key = { item -> "menu_item:${item.id}" },
                                contentType = { "menu_item" }
                            ) { menuItem ->
                                AdminMenuItemRow(
                                    item = menuItem,
                                    onRemoveClick = {
                                        onAction(
                                            AdminMenuAction.RemoveMenuItemClicked(
                                                menuItemId = menuItem.id
                                            )
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    uiState.pendingRemoval?.let { item ->
        AlertDialog(
            onDismissRequest = {
                onAction(AdminMenuAction.DismissRemoveClicked)
            },
            title = {
                Text("Remove menu item?")
            },
            text = {
                Text(
                    text = "Remove \"${item.name}\" from " +
                            "\"${selectedStore?.displayName.orEmpty()}\"?"
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onAction(AdminMenuAction.ConfirmRemoveClicked)
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = colors.error
                    )
                ) {
                    Text("Remove")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        onAction(AdminMenuAction.DismissRemoveClicked)
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AdminMenuStoreDropdown(
    stores: List<AdminMenuStoreOption>,
    selectedStore: AdminMenuStoreOption?,
    onStoreSelected: (Int) -> Unit
) {
    val colors = MaterialTheme.colorScheme
    var expanded by remember { mutableStateOf(false) }

    val enabled = stores.isNotEmpty()

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = it && enabled
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        TextField(
            value = selectedStore?.displayName.orEmpty(),
            onValueChange = {},
            readOnly = true,
            enabled = enabled,
            singleLine = true,
            label = {
                Text("Store / Canteen")
            },
            placeholder = {
                Text("Select store")
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            shape = RoundedCornerShape(14.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = colors.surfaceVariant,
                unfocusedContainerColor = colors.surfaceVariant,
                disabledContainerColor = colors.surfaceVariant,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(
                    type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                    enabled = enabled
                )
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            },
            modifier = Modifier.heightIn(max = 360.dp)
        ) {
            stores.forEach { store ->
                DropdownMenuItem(
                    text = {
                        Text(store.displayName)
                    },
                    onClick = {
                        expanded = false
                        onStoreSelected(store.id)
                    }
                )
            }
        }
    }
}

@Composable
private fun AdminMenuItemRow(
    item: AdminMenuItem,
    onRemoveClick: () -> Unit
) {
    val colors = MaterialTheme.colorScheme

    val formattedPrice = remember(item.priceInSen) {
        String.format(
            Locale.US,
            "RM %.2f",
            item.priceInSen / 100.0
        )
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = colors.surfaceVariant
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.onSurface
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = formattedPrice,
                    style = MaterialTheme.typography.bodySmall,
                    color = LocalPriceColor.current
                )
            }

            Button(
                onClick = onRemoveClick,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF970F0F),
                    contentColor = Color(0xFFDCB2FB)
                ),
                contentPadding = PaddingValues(
                    horizontal = 12.dp,
                    vertical = 10.dp
                ),
                modifier = Modifier.heightIn(min = 48.dp)
            ) {
                Text(
                    text = "Remove",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}