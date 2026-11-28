package my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.home.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import my.edu.tarc.tarumtfoodorderingsystem.ui.components.AppSecondActionButton
import java.math.BigDecimal
import java.util.Locale

import my.edu.tarc.tarumtfoodorderingsystem.ui.components.AppTopBar

@Composable
fun CartScreen(
    uiState: CartUiState,
    onAction: (CartAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = colors.background,
        topBar = {
            AppTopBar(
                title = "Cart",
                subtitle = uiState.storeSubtitle,
                onBackClick = {
                    onAction(CartAction.BackClicked)
                }
            )
        },
        bottomBar = {
            AppSecondActionButton(
                title = "Proceed to payment",
                enabled = uiState.canProceedToPayment,
                onClick = {
                    onAction(
                        CartAction.ProceedToPaymentClicked
                    )
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
                modifier = Modifier
                    .widthIn(max = 560.dp)
                    .fillMaxSize(),
                contentPadding = PaddingValues(
                    horizontal = 16.dp,
                    vertical = 24.dp
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item(
                    key = "cart_heading",
                    contentType = "heading"
                ) {
                    Text(
                        text = "Cart items",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = colors.secondary,
                        modifier = Modifier.padding(
                            horizontal = 4.dp
                        )
                    )
                }

                if (uiState.items.isEmpty()) {
                    item(
                        key = "cart_empty",
                        contentType = "message"
                    ) {
                        Text(
                            text = "Your cart is empty. " +
                                    "Return to the menu to choose an item.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = colors.onSurfaceVariant,
                            modifier = Modifier.padding(
                                horizontal = 4.dp
                            )
                        )
                    }
                }

                items(
                    items = uiState.items,
                    key = { item ->
                        item.id
                    },
                    contentType = {
                        "cart_item"
                    }
                ) { item ->
                    CustomerCartItemRow(
                        item = item,
                        onModifyClick = {
                            onAction(
                                CartAction.ModifyItemClicked(
                                    cartItemId = item.id
                                )
                            )
                        }
                    )
                }

                if (uiState.items.isNotEmpty()) {
                    item(
                        key = "cart_subtotal",
                        contentType = "subtotal"
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp)
                        ) {
                            HorizontalDivider(
                                color = colors.outlineVariant
                            )

                            Spacer(Modifier.height(16.dp))

                            Text(
                                text = "Subtotal: " +
                                        formatCartAmount(
                                            uiState.subtotalInSen
                                        ),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = colors.onSurface,
                                modifier = Modifier.align(
                                    Alignment.End
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    uiState.editingItem?.let { item ->
        ModifyCartItemDialog(
            item = item,
            quantity = uiState.editedQuantity,
            remarks = uiState.editedRemarks,
            quantityError = uiState.quantityError,
            onAction = onAction
        )
    }
}

@Composable
private fun CustomerCartItemRow(
    item: CustomerCartItem,
    onModifyClick: () -> Unit
) {
    val colors = MaterialTheme.colorScheme

    val priceColor = if (colors.background.luminance() < 0.5f) {
        Color(0xFF4CF05A)
    } else {
        Color(0xFF08752D)
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        CustomerCartItemImage(
            item = item
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.onSurface,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = "${item.quantity}x",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.onSurfaceVariant
                )
            }

            item.drinkSummary?.let { summary ->
                Spacer(Modifier.height(6.dp))

                Text(
                    text = "Drink: $summary",
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.onSurfaceVariant
                )
            }

            if (item.remarks.isNotBlank()) {
                Spacer(Modifier.height(6.dp))

                Text(
                    text = "Remarks: ${item.remarks}",
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.onSurfaceVariant
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = onModifyClick,
                    contentPadding = PaddingValues(
                        horizontal = 0.dp,
                        vertical = 8.dp
                    )
                ) {
                    Text(
                        text = "Modify item",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = colors.primary
                    )
                }

                Spacer(Modifier.weight(1f))

                Text(
                    text = formatCartAmount(
                        item.lineTotalInSen
                    ),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = priceColor
                )
            }
        }
    }
}

@Composable
private fun CustomerCartItemImage(
    item: CustomerCartItem
) {
    val imageModel: Any? = item.imageUrl
        ?.takeIf { it.isNotBlank() }
        ?: item.imageRes

    Box(
        modifier = Modifier
            .size(84.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White)
    ) {
        if (imageModel != null) {
            AsyncImage(
                model = imageModel,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
private fun ModifyCartItemDialog(
    item: CustomerCartItem,
    quantity: String,
    remarks: String,
    quantityError: String?,
    onAction: (CartAction) -> Unit
) {
    AlertDialog(
        onDismissRequest = {
            onAction(
                CartAction.DismissEditorClicked
            )
        },
        title = {
            Text(
                text = "Modify order"
            )
        },
        text = {
            Column(
                modifier = Modifier.verticalScroll(
                    rememberScrollState()
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = "Unit price: " +
                            formatCartAmount(
                                item.unitPriceInSen
                            ),
                    style = MaterialTheme.typography.bodyMedium
                )

                OutlinedTextField(
                    value = quantity,
                    onValueChange = {
                        onAction(
                            CartAction.QuantityChanged(
                                quantity = it
                            )
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("Quantity")
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    isError = quantityError != null,
                    supportingText = {
                        Text(
                            text = quantityError
                                ?: "Available: ${item.maximumQuantity}"
                        )
                    }
                )

                OutlinedTextField(
                    value = remarks,
                    onValueChange = {
                        onAction(
                            CartAction.RemarksChanged(
                                remarks = it
                            )
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("Remarks (optional)")
                    },
                    minLines = 2,
                    maxLines = 4,
                    supportingText = {
                        Text(
                            text = "${remarks.length}/240",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.End
                        )
                    }
                )

                TextButton(
                    onClick = {
                        onAction(
                            CartAction.RemoveEditingItemClicked
                        )
                    }
                ) {
                    Text(
                        text = "Remove item",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onAction(
                        CartAction.SaveChangesClicked
                    )
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onAction(
                        CartAction.DismissEditorClicked
                    )
                }
            ) {
                Text("Cancel")
            }
        }
    )
}

private fun formatCartAmount(
    amountInSen: Long
): String {
    return String.format(
        Locale.US,
        "RM %,.2f",
        BigDecimal.valueOf(amountInSen, 2)
    )
}