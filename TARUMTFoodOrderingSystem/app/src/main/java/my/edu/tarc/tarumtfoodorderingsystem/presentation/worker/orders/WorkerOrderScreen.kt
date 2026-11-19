package my.edu.tarc.tarumtfoodorderingsystem.presentation.worker.orders

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
import androidx.compose.ui.unit.dp
import my.edu.tarc.tarumtfoodorderingsystem.presentation.worker.orders.pendingOrders.WorkerOrder
import java.math.BigDecimal
import java.util.Locale
import my.edu.tarc.tarumtfoodorderingsystem.ui.components.AppActionButton
import my.edu.tarc.tarumtfoodorderingsystem.ui.components.AppTopBar
import my.edu.tarc.tarumtfoodorderingsystem.ui.components.WorkerNavigationTab
import my.edu.tarc.tarumtfoodorderingsystem.ui.components.WorkerNavigationTabItem
import my.edu.tarc.tarumtfoodorderingsystem.ui.components.WorkerOrdersTab
import my.edu.tarc.tarumtfoodorderingsystem.ui.components.WorkerOrdersTabItem

@Composable
fun WorkerOrderScreen(
    uiState: WorkerOrderUiState,
    onAction: (WorkerOrderAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    val activeOrders = uiState.activeOrders

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = colors.background,
        topBar = {
            Column {
                AppTopBar(
                    title = "Orders",
                    subtitle = uiState.storeSubtitle,
                    onLogoutClick = {
                        onAction(
                            WorkerOrderAction.LogoutClicked
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
                                    WorkerOrderAction.CompletedOrdersClicked
                                )
                            }

                            WorkerNavigationTabItem.ITEMS -> {
                                onAction(
                                    WorkerOrderAction.ItemsClicked
                                )
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
                        selectedTab = WorkerOrdersTabItem.ACTIVE,
                        onTabSelected = { tab ->
                            when (tab) {
                                WorkerOrdersTabItem.PENDING -> {
                                    onAction(
                                        WorkerOrderAction.PendingOrdersClicked
                                    )
                                }

                                WorkerOrdersTabItem.ACTIVE -> {
                                    // Already viewing Active.
                                }

                                WorkerOrdersTabItem.PICKUPS -> {
                                    onAction(
                                        WorkerOrderAction.PickupsClicked
                                    )
                                }
                            }
                        }
                    )
                }

                item(
                    key = "active_controls",
                    contentType = "controls"
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${activeOrders.size} Active",
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
                                    WorkerOrderAction.AcceptingOrdersChanged(
                                        accepting = accepting
                                    )
                                )
                            }
                        )
                    }
                }

                if (activeOrders.isEmpty()) {
                    item(
                        key = "active_empty",
                        contentType = "message"
                    ) {
                        WorkerActiveEmptyCard()
                    }
                }

                items(
                    items = activeOrders,
                    key = { order ->
                        order.id
                    },
                    contentType = {
                        "active_order"
                    }
                ) { order ->
                    WorkerActiveOrderCard(
                        order = order,
                        expanded = uiState.expandedOrderId == order.id,
                        onClick = {
                            onAction(
                                WorkerOrderAction.OrderClicked(
                                    orderId = order.id
                                )
                            )
                        },
                        onMarkReadyClick = {
                            onAction(
                                WorkerOrderAction.MarkReadyForPickupClicked(
                                    orderId = order.id
                                )
                            )
                        }
                    )
                }
            }
        }
    }

    if (uiState.showLogoutDialog) {
        WorkerOrderLogoutDialog(
            onAction = onAction
        )
    }
}

@Composable
private fun WorkerActiveEmptyCard() {
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
                text = "No active orders yet",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = colors.onSurface
            )

            Text(
                text = "Accepted orders will appear here.",
                style = MaterialTheme.typography.bodyMedium,
                color = colors.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun WorkerActiveOrderCard(
    order: WorkerOrder,
    expanded: Boolean,
    onClick: () -> Unit,
    onMarkReadyClick: () -> Unit
) {
    val colors = MaterialTheme.colorScheme
    val isDarkBackground = colors.background.luminance() < 0.5f

    val badgeContainerColor = if (isDarkBackground) {
        Color(0xFF51432A)
    } else {
        Color(0xFFFFEDC4)
    }

    val badgeTextColor = if (isDarkBackground) {
        Color(0xFFFFD68A)
    } else {
        Color(0xFF694700)
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

            Text(
                text = formatWorkerActiveAmount(
                    order.totalInSen
                ) + " · Order #${order.id}",
                style = MaterialTheme.typography.bodySmall,
                color = colors.onSurfaceVariant,
                modifier = Modifier.padding(
                    top = 8.dp
                )
            )

            if (expanded) {
                AppActionButton(
                    title = "Mark ready for pickup",
                    onClick = onMarkReadyClick
                )
            }
        }
    }
}

@Composable
private fun WorkerOrderLogoutDialog(
    onAction: (WorkerOrderAction) -> Unit
) {
    AlertDialog(
        onDismissRequest = {
            onAction(
                WorkerOrderAction.DismissLogoutClicked
            )
        },
        title = {
            Text("Log out?")
        },
        text = {
            Text(
                text = "You will return to the login screen."
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onAction(
                        WorkerOrderAction.ConfirmLogoutClicked
                    )
                }
            ) {
                Text("Log out")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onAction(
                        WorkerOrderAction.DismissLogoutClicked
                    )
                }
            ) {
                Text("Stay")
            }
        }
    )
}

private fun formatWorkerActiveAmount(
    amountInSen: Long
): String {
    return String.format(
        Locale.US,
        "RM %,.2f",
        BigDecimal.valueOf(amountInSen, 2)
    )
}