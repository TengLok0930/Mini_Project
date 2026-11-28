package my.edu.tarc.tarumtfoodorderingsystem.presentation.worker.orders.pickupOrders

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.math.BigDecimal
import java.util.Locale
import my.edu.tarc.tarumtfoodorderingsystem.presentation.worker.orders.pendingOrders.WorkerOrder
import my.edu.tarc.tarumtfoodorderingsystem.ui.components.AppActionButton
import my.edu.tarc.tarumtfoodorderingsystem.ui.components.AppTopBar
import my.edu.tarc.tarumtfoodorderingsystem.ui.components.WorkerNavigationTab
import my.edu.tarc.tarumtfoodorderingsystem.ui.components.WorkerNavigationTabItem
import my.edu.tarc.tarumtfoodorderingsystem.ui.components.WorkerOrdersTab
import my.edu.tarc.tarumtfoodorderingsystem.ui.components.WorkerOrdersTabItem

@Composable
fun WorkerPickupOrderScreen(
    uiState: WorkerPickupOrderUiState,
    onAction: (WorkerPickupOrderAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    val pickupOrders = uiState.pickupOrders

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = colors.background,
        topBar = {
            Column {
                AppTopBar(
                    title = "Orders",
                    subtitle = uiState.storeSubtitle,
                    onProfileClick = {
                        onAction(
                            WorkerPickupOrderAction.ProfileClicked
                        )
                    }
                )

                WorkerNavigationTab(
                    selectedTab = WorkerNavigationTabItem.ORDERS,
                    onTabSelected = { tab ->
                        when (tab) {
                            WorkerNavigationTabItem.ORDERS -> {
                                // Already in the Orders section.
                            }

                            WorkerNavigationTabItem.COMPLETED -> {
                                onAction(
                                    WorkerPickupOrderAction
                                        .CompletedOrdersClicked
                                )
                            }

                            WorkerNavigationTabItem.ITEMS -> {
                                onAction(
                                    WorkerPickupOrderAction.ItemsClicked
                                )
                            }
                        }
                    }
                )
            }
        },
        bottomBar = {
            uiState.lastCollectedOrderId?.let { orderId ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .windowInsetsPadding(
                            WindowInsets.navigationBars.only(
                                WindowInsetsSides.Bottom
                            )
                        )
                        .padding(
                            horizontal = 20.dp,
                            vertical = 16.dp
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    WorkerCollectionSuccessCard(
                        orderId = orderId,
                        modifier = Modifier
                            .widthIn(max = 560.dp)
                            .fillMaxWidth()
                    )
                }
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
                    horizontal = 20.dp,
                    vertical = 18.dp
                ),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                item(
                    key = "order_sub_tabs",
                    contentType = "navigation"
                ) {
                    WorkerOrdersTab(
                        selectedTab = WorkerOrdersTabItem.PICKUPS,
                        onTabSelected = { tab ->
                            when (tab) {
                                WorkerOrdersTabItem.PENDING -> {
                                    onAction(
                                        WorkerPickupOrderAction
                                            .PendingOrdersClicked
                                    )
                                }

                                WorkerOrdersTabItem.ACTIVE -> {
                                    onAction(
                                        WorkerPickupOrderAction
                                            .ActiveOrdersClicked
                                    )
                                }

                                WorkerOrdersTabItem.PICKUPS -> {
                                    // Already viewing Pickups.
                                }
                            }
                        }
                    )
                }

                item(
                    key = "pickup_controls",
                    contentType = "controls"
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (pickupOrders.size == 1) {
                                "1 Pickup"
                            } else {
                                "${pickupOrders.size} Pickups"
                            },
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = colors.secondary,
                            modifier = Modifier.weight(1f)
                        )

                        Text(
                            text = "Accepting orders",
                            style = MaterialTheme.typography.bodySmall,
                            color = colors.onSurface
                        )

                        Switch(
                            checked = uiState.acceptingOrders,
                            onCheckedChange = { accepting ->
                                onAction(
                                    WorkerPickupOrderAction
                                        .AcceptingOrdersChanged(
                                            accepting = accepting
                                        )
                                )
                            }
                        )
                    }
                }

                if (pickupOrders.isEmpty()) {
                    item(
                        key = "pickups_empty",
                        contentType = "message"
                    ) {
                        WorkerPickupEmptyCard()
                    }
                }

                items(
                    items = pickupOrders,
                    key = { order ->
                        order.id
                    },
                    contentType = {
                        "pickup_order"
                    }
                ) { order ->
                    WorkerPickupOrderCard(
                        order = order,
                        expanded = uiState.expandedOrderId == order.id,
                        onClick = {
                            onAction(
                                WorkerPickupOrderAction.OrderClicked(
                                    orderId = order.id
                                )
                            )
                        },
                        onConfirmCollection = {
                            onAction(
                                WorkerPickupOrderAction
                                    .ConfirmCollectionClicked(
                                        orderId = order.id
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
private fun WorkerPickupEmptyCard() {
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
                text = "No orders awaiting pickup",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = colors.onSurface
            )

            Text(
                text = "Orders marked ready in Active will appear here.",
                style = MaterialTheme.typography.bodyMedium,
                color = colors.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun WorkerPickupOrderCard(
    order: WorkerOrder,
    expanded: Boolean,
    onClick: () -> Unit,
    onConfirmCollection: () -> Unit
) {
    val colors = MaterialTheme.colorScheme
    val isDarkBackground = colors.background.luminance() < 0.5f

    val badgeContainerColor = if (isDarkBackground) {
        Color(0xFF214D3B)
    } else {
        Color(0xFFD5EFDD)
    }

    val badgeTextColor = if (isDarkBackground) {
        Color(0xFFBBF0CD)
    } else {
        Color(0xFF205332)
    }

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.surfaceVariant,
            contentColor = colors.onSurface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Pickup # ${order.formattedPickupNumber}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = colors.onSurface,
                    modifier = Modifier.weight(1f)
                )

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = badgeContainerColor,
                    contentColor = badgeTextColor
                ) {
                    Text(
                        text = order.status.displayName,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(
                            horizontal = 10.dp,
                            vertical = 5.dp
                        )
                    )
                }
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                order.items.forEach { item ->
                    Column(
                        verticalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Text(
                            text = "${item.quantity} × ${item.name}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = colors.onSurface
                        )

                        item.drinkOption?.let { option ->
                            Text(
                                text = "Drink: $option",
                                style = MaterialTheme.typography.bodySmall,
                                color = colors.onSurfaceVariant
                            )
                        }

                        if (item.remarks.isNotBlank()) {
                            Text(
                                text = "Remarks: ${item.remarks}",
                                style = MaterialTheme.typography.bodySmall,
                                color = colors.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = formatWorkerPickupAmount(
                        order.totalInSen
                    ) + " · Order #${order.id}",
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.onSurfaceVariant,
                    modifier = Modifier.weight(1f)
                )

                if (!expanded) {
                    Text(
                        text = "Press to expand",
                        style = MaterialTheme.typography.labelSmall,
                        color = colors.onSurfaceVariant,
                        textAlign = TextAlign.End
                    )
                }
            }

            if (expanded) {
                AppActionButton(
                    title = "Confirm Collection",
                    onClick = onConfirmCollection
                )
            }
        }
    }
}

@Composable
private fun WorkerCollectionSuccessCard(
    orderId: Int,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    val isDarkBackground = colors.background.luminance() < 0.5f

    val containerColor = if (isDarkBackground) {
        Color(0xFF214536)
    } else {
        Color(0xFFD9F2E2)
    }

    val headingColor = if (isDarkBackground) {
        Color(0xFF9FE3B3)
    } else {
        Color(0xFF1B6A32)
    }

    val bodyColor = if (isDarkBackground) {
        Color(0xFFEBFFF1)
    } else {
        Color(0xFF173D27)
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = containerColor
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 20.dp
            ),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Order #$orderId collected",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = headingColor
            )

            Text(
                text = "This order is now completed.",
                style = MaterialTheme.typography.bodyMedium,
                color = bodyColor
            )
        }
    }
}

private fun formatWorkerPickupAmount(
    amountInSen: Long
): String {
    return String.format(
        Locale.US,
        "RM %,.2f",
        BigDecimal.valueOf(amountInSen, 2)
    )
}