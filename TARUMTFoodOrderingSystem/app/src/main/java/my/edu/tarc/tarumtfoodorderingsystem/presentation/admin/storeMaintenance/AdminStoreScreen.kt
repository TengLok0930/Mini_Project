package my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.storeMaintenance

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import my.edu.tarc.tarumtfoodorderingsystem.ui.components.AppActionButton
import my.edu.tarc.tarumtfoodorderingsystem.ui.components.AppTopBar

@Composable
fun AdminStoreScreen(
    uiState: AdminStoreUiState,
    onAction: (AdminStoreAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    val listState = rememberLazyListState()

    LaunchedEffect(uiState.selectedCanteenId) {
        listState.scrollToItem(0)
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = colors.background,
        topBar = {
            AppTopBar(
                title = "Store Maintenance",
                onBackClick = {
                    onAction(AdminStoreAction.BackClicked)
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
            Column(
                modifier = Modifier
                    .widthIn(max = 560.dp)
                    .fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 22.dp,
                            vertical = 24.dp
                        )
                ) {
                    Text(
                        text = "Choose a canteen",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = colors.secondary
                    )

                    Spacer(Modifier.height(16.dp))

                    StoreCanteenDropdown(
                        canteens = uiState.canteens,
                        selectedCanteen = uiState.selectedCanteen,
                        onCanteenSelected = { canteenId ->
                            onAction(
                                AdminStoreAction.CanteenSelected(
                                    canteenId = canteenId
                                )
                            )
                        }
                    )
                }

                if (uiState.selectedCanteen != null) {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentPadding = PaddingValues(
                            start = 22.dp,
                            end = 22.dp,
                            top = 8.dp,
                            bottom = 16.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item(key = "stores_heading") {
                            Text(
                                text = "Stores (${uiState.visibleStores.size})",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = colors.secondary,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                        }

                        if (uiState.visibleStores.isEmpty()) {
                            item(key = "stores_empty") {
                                Text(
                                    text = "No stores available for this canteen.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = colors.onSurfaceVariant,
                                    modifier = Modifier.padding(
                                        vertical = 12.dp
                                    )
                                )
                            }
                        }

                        items(
                            items = uiState.visibleStores,
                            key = { store -> store.id }
                        ) { store ->
                            AdminStoreRow(
                                store = store,
                                onRemoveClick = {
                                    onAction(
                                        AdminStoreAction.RemoveStoreClicked(
                                            storeId = store.id
                                        )
                                    )
                                }
                            )
                        }
                    }

                    AppActionButton(
                        title = "+ Create New Store",
                        onClick = {
                            onAction(
                                AdminStoreAction.CreateStoreClicked
                            )
                        },
                        modifier = Modifier.padding(
                            horizontal = 22.dp,
                            vertical = 20.dp
                        )
                    )
                } else {
                    Spacer(Modifier.weight(1f))

                    if (uiState.canteens.isEmpty()) {
                        Text(
                            text = "No canteens available. " +
                                    "Create a canteen before adding stores.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = colors.onSurfaceVariant,
                            modifier = Modifier.padding(22.dp)
                        )
                    }
                }
            }
        }
    }

    uiState.pendingRemoval?.let { store ->
        AlertDialog(
            onDismissRequest = {
                onAction(AdminStoreAction.DismissRemoveClicked)
            },
            title = {
                Text("Remove store?")
            },
            text = {
                Text(
                    text = "Remove \"${store.name}\" from " +
                            "\"${uiState.selectedCanteen?.name.orEmpty()}\"?"
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onAction(
                            AdminStoreAction.ConfirmRemoveClicked
                        )
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
                        onAction(
                            AdminStoreAction.DismissRemoveClicked
                        )
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
private fun StoreCanteenDropdown(
    canteens: List<StoreCanteenOption>,
    selectedCanteen: StoreCanteenOption?,
    onCanteenSelected: (Int) -> Unit
) {
    val colors = MaterialTheme.colorScheme
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { shouldExpand ->
            expanded = shouldExpand && canteens.isNotEmpty()
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        TextField(
            value = selectedCanteen?.name.orEmpty(),
            onValueChange = {},
            readOnly = true,
            enabled = canteens.isNotEmpty(),
            label = {
                Text("Canteen")
            },
            placeholder = {
                Text("Select Canteen")
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            singleLine = true,
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
                    enabled = canteens.isNotEmpty()
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
private fun AdminStoreRow(
    store: AdminStoreItem,
    onRemoveClick: () -> Unit
) {
    val colors = MaterialTheme.colorScheme

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = colors.surfaceVariant,
        shape = RoundedCornerShape(14.dp)
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
                    text = store.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.onSurface
                )

                Spacer(Modifier.height(10.dp))

                Text(
                    text = "${store.foodCount} " +
                            if (store.foodCount == 1) {
                                "Food Available"
                            } else {
                                "Foods Available"
                            },
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.onSurfaceVariant
                )

                Text(
                    text = "${store.beverageCount} " +
                            if (store.beverageCount == 1) {
                                "Beverage Available"
                            } else {
                                "Beverages Available"
                            },
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.onSurfaceVariant
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
                modifier = Modifier.heightIn(min = 52.dp)
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