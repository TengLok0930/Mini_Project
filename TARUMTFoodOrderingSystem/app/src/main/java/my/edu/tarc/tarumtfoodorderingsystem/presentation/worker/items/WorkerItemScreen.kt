package my.edu.tarc.tarumtfoodorderingsystem.presentation.worker.items

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.math.BigDecimal
import java.util.Locale
import my.edu.tarc.tarumtfoodorderingsystem.ui.components.AppTopBar
import my.edu.tarc.tarumtfoodorderingsystem.ui.components.WorkerNavigationTab
import my.edu.tarc.tarumtfoodorderingsystem.ui.components.WorkerNavigationTabItem

@Composable
fun WorkerItemScreen(
    uiState: WorkerItemUiState,
    onAction: (WorkerItemAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = colors.background,
        topBar = {
            Column {
                AppTopBar(
                    title = "Items",
                    subtitle = uiState.storeSubtitle,
                    onProfileClick = {
                        onAction(
                            WorkerItemAction.ProfileClicked
                        )
                    }
                )

                WorkerNavigationTab(
                    selectedTab = WorkerNavigationTabItem.ITEMS,
                    onTabSelected = { tab ->
                        when (tab) {
                            WorkerNavigationTabItem.ORDERS -> {
                                onAction(
                                    WorkerItemAction.OrdersClicked
                                )
                            }

                            WorkerNavigationTabItem.COMPLETED -> {
                                onAction(
                                    WorkerItemAction.CompletedOrdersClicked
                                )
                            }

                            WorkerNavigationTabItem.ITEMS -> {
                                // Already viewing Items.
                            }
                        }
                    }
                )
            }
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
                    horizontal = 18.dp,
                    vertical = 22.dp
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item(
                    key = "menu_heading",
                    contentType = "heading"
                ) {
                    Text(
                        text = "Manage your items",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = colors.secondary,
                        modifier = Modifier.padding(
                            bottom = 24.dp
                        )
                    )
                }

                if (uiState.items.isEmpty()) {
                    item(
                        key = "menu_empty",
                        contentType = "message"
                    ) {
                        WorkerItemEmptyCard()
                    }
                }

                items(
                    items = uiState.items,
                    key = { item ->
                        item.id
                    },
                    contentType = {
                        "menu_item"
                    }
                ) { item ->
                    WorkerMenuItemCard(
                        item = item,
                        onEditClick = {
                            onAction(
                                WorkerItemAction.EditItemClicked(
                                    itemId = item.id
                                )
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun WorkerMenuItemCard(
    item: WorkerMenuItem,
    onEditClick: () -> Unit
) {
    val colors = MaterialTheme.colorScheme
    val isDarkBackground = colors.background.luminance() < 0.5f

    val priceColor = if (isDarkBackground) {
        Color(0xFF9FE3B3)
    } else {
        Color(0xFF1B6A32)
    }

    val stockColor = if (item.isLowStock) {
        colors.error
    } else {
        colors.onSurfaceVariant
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = colors.surfaceVariant
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = colors.onSurface,
                    modifier = Modifier.weight(1f)
                )

                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = if (item.availableQuantity == 0) {
                            "Out of stock"
                        } else {
                            "${item.availableQuantity} Available"
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = stockColor
                    )

                    if (item.isLowStock && item.availableQuantity > 0) {
                        Text(
                            text = "Low stock",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = colors.error
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "Price (RM)",
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.onSurfaceVariant
                    )

                    Text(
                        text = formatWorkerItemPrice(
                            item.priceInSen
                        ),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = priceColor
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "Sold",
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.onSurfaceVariant
                    )

                    Text(
                        text = item.soldQuantity.toString(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = colors.onSurface
                    )
                }

                TextButton(
                    onClick = onEditClick,
                    contentPadding = PaddingValues(
                        horizontal = 12.dp,
                        vertical = 8.dp
                    )
                ) {
                    Text(
                        text = "Edit ›",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = colors.secondary
                    )
                }
            }
        }
    }
}

@Composable
private fun WorkerItemEmptyCard() {
    val colors = MaterialTheme.colorScheme

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = colors.surfaceVariant
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "No menu items yet",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = colors.onSurface
            )

            Text(
                text = "Your store's menu items will appear here.",
                style = MaterialTheme.typography.bodyMedium,
                color = colors.onSurfaceVariant
            )
        }
    }
}

private fun formatWorkerItemPrice(
    priceInSen: Long
): String {
    return String.format(
        Locale.US,
        "%,.2f",
        BigDecimal.valueOf(priceInSen, 2)
    )
}