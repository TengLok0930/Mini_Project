package my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.home.orderItem

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import java.math.BigDecimal
import java.util.Locale
import my.edu.tarc.tarumtfoodorderingsystem.R
import my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.home.menu.CustomerMenuItem
import my.edu.tarc.tarumtfoodorderingsystem.ui.components.AppSecondActionButton

@Composable
fun OrderItemScreen(
    uiState: OrderItemUiState,
    onAction: (OrderItemAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme

    val priceColor = if (colors.background.luminance() < 0.5f) {
        Color(0xFF4CF05A)
    } else {
        Color(0xFF08752D)
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .imePadding(),
        containerColor = colors.background,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(
                        horizontal = 8.dp,
                        vertical = 4.dp
                    )
            ) {
                IconButton(
                    onClick = {
                        onAction(OrderItemAction.BackClicked)
                    }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.arrow_back_48px),
                        contentDescription = "Back",
                        tint = colors.onSurface
                    )
                }
            }
        },
        bottomBar = {
            AppSecondActionButton(
                title = "Order now",
                enabled = uiState.canOrder,
                onClick = {
                    onAction(OrderItemAction.OrderNowClicked)
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
                    .verticalScroll(
                        rememberScrollState()
                    )
                    .padding(
                        horizontal = 20.dp,
                        vertical = 16.dp
                    ),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OrderItemImage(
                            item = uiState.item
                        )

                        Text(
                            text = uiState.item.name,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = colors.onSurface,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "${uiState.item.quantitySold} sold",
                                style = MaterialTheme.typography.bodySmall,
                                color = colors.onSurfaceVariant
                            )

                            Text(
                                text = "Quantity left: " +
                                        uiState.item.quantityRemaining,
                                style = MaterialTheme.typography.bodySmall,
                                color = colors.onSurfaceVariant
                            )
                        }

                        Text(
                            text = formatOrderItemAmount(
                                uiState.unitPriceInSen
                            ) + " each",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = priceColor
                        )
                    }
                }

                HorizontalDivider(
                    color = colors.outlineVariant
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Quantity",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = colors.onSurface,
                        modifier = Modifier.weight(1f)
                    )

                    Column(
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = colors.surfaceVariant
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(
                                    enabled = uiState.quantity > 1,
                                    onClick = {
                                        onAction(
                                            OrderItemAction.DecreaseQuantityClicked
                                        )
                                    },
                                    modifier = Modifier.semantics {
                                        contentDescription = "Decrease quantity"
                                    }
                                ) {
                                    Text(
                                        text = "−",
                                        fontSize = 24.sp
                                    )
                                }

                                Text(
                                    text = uiState.quantity.toString(),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(
                                        horizontal = 8.dp
                                    )
                                )

                                IconButton(
                                    enabled = uiState.quantity <
                                            uiState.maximumQuantity,
                                    onClick = {
                                        onAction(
                                            OrderItemAction.IncreaseQuantityClicked
                                        )
                                    },
                                    modifier = Modifier.semantics {
                                        contentDescription = "Increase quantity"
                                    }
                                ) {
                                    Text(
                                        text = "+",
                                        fontSize = 24.sp
                                    )
                                }
                            }
                        }

                        Text(
                            text = if (uiState.maximumQuantity > 0) {
                                "Max. ${uiState.maximumQuantity} quantity"
                            } else {
                                "No more available to add"
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = colors.onSurfaceVariant
                        )
                    }
                }

                if (uiState.isBeverage) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Drinks option",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = colors.onSurface
                        )

                        Text(
                            text = "Choose one",
                            style = MaterialTheme.typography.bodySmall,
                            color = colors.onSurfaceVariant
                        )

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .selectableGroup()
                        ) {
                            DrinkOption.values().forEach { option ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .heightIn(min = 48.dp)
                                        .selectable(
                                            selected = uiState.selectedDrinkOption == option,
                                            onClick = {
                                                onAction(
                                                    OrderItemAction.DrinkOptionSelected(
                                                        option = option
                                                    )
                                                )
                                            },
                                            role = Role.RadioButton
                                        )
                                        .padding(
                                            horizontal = 4.dp,
                                            vertical = 4.dp
                                        ),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = uiState.selectedDrinkOption == option,
                                        onClick = null
                                    )

                                    Text(
                                        text = option.displayName,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = colors.onSurface,
                                        modifier = Modifier
                                            .padding(start = 8.dp)
                                            .weight(1f)
                                    )

                                    if (option.surchargeInSen > 0L) {
                                        Text(
                                            text = "+ " + formatOrderItemAmount(
                                                option.surchargeInSen
                                            ),
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = colors.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }

                        HorizontalDivider(
                            color = colors.outlineVariant
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 48.dp)
                                .toggleable(
                                    value = uiState.takeaway,
                                    onValueChange = { checked ->
                                        onAction(
                                            OrderItemAction.TakeawayChanged(
                                                takeaway = checked
                                            )
                                        )
                                    },
                                    role = Role.Checkbox
                                )
                                .padding(
                                    horizontal = 4.dp,
                                    vertical = 4.dp
                                ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = uiState.takeaway,
                                onCheckedChange = null
                            )

                            Text(
                                text = "Takeaway",
                                style = MaterialTheme.typography.bodyLarge,
                                color = colors.onSurface,
                                modifier = Modifier
                                    .padding(start = 8.dp)
                                    .weight(1f)
                            )

                            Text(
                                text = "+ " + formatOrderItemAmount(
                                    TAKEAWAY_SURCHARGE_IN_SEN
                                ),
                                style = MaterialTheme.typography.bodyMedium,
                                color = colors.onSurfaceVariant
                            )
                        }
                    }
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Remarks",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = colors.onSurface
                    )

                    OutlinedTextField(
                        value = uiState.remarks,
                        onValueChange = {
                            onAction(
                                OrderItemAction.RemarksChanged(
                                    remarks = it
                                )
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            Text("Additional remarks")
                        },
                        minLines = 4,
                        maxLines = 6,
                        shape = RoundedCornerShape(20.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = colors.surfaceVariant,
                            unfocusedContainerColor = colors.surfaceVariant
                        ),
                        supportingText = {
                            Text(
                                text = "${uiState.remarks.length}/240",
                                modifier = Modifier.fillMaxWidth(),
                                textAlign =
                                    androidx.compose.ui.text.style.TextAlign.End
                            )
                        }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Item total",
                        style = MaterialTheme.typography.titleMedium,
                        color = colors.onSurface
                    )

                    Text(
                        text = formatOrderItemAmount(
                            uiState.totalInSen
                        ),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = priceColor
                    )
                }
            }
        }
    }
}

@Composable
private fun OrderItemImage(
    item: CustomerMenuItem
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

private fun formatOrderItemAmount(
    amountInSen: Long
): String {
    return String.format(
        Locale.US,
        "RM %,.2f",
        BigDecimal.valueOf(amountInSen, 2)
    )
}