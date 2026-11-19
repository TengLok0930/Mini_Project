package my.edu.tarc.tarumtfoodorderingsystem.presentation.worker.orders.pendingOrders

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.math.BigDecimal
import java.util.Locale
import my.edu.tarc.tarumtfoodorderingsystem.ui.components.AppActionButton
import my.edu.tarc.tarumtfoodorderingsystem.ui.components.AppTopBar
import my.edu.tarc.tarumtfoodorderingsystem.ui.components.WorkerNavigationTab
import my.edu.tarc.tarumtfoodorderingsystem.ui.components.WorkerNavigationTabItem
import my.edu.tarc.tarumtfoodorderingsystem.ui.components.WorkerOrdersTab
import my.edu.tarc.tarumtfoodorderingsystem.ui.components.WorkerOrdersTabItem

@Composable
fun WorkerPendingOrderScreen(
    uiState: WorkerPendingOrderUiState,
    onAction: (WorkerPendingOrderAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    val pendingOrders = uiState.pendingOrders

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
                            WorkerPendingOrderAction.LogoutClicked
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
                                    WorkerPendingOrderAction.CompletedOrdersClicked
                                )
                            }

                            WorkerNavigationTabItem.ITEMS -> {
                                onAction(
                                    WorkerPendingOrderAction.ItemsClicked
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
                        selectedTab = WorkerOrdersTabItem.PENDING,
                        onTabSelected = { tab ->
                            when (tab) {
                                WorkerOrdersTabItem.PENDING -> {
                                    // Already viewing Pending.
                                }

                                WorkerOrdersTabItem.ACTIVE -> {
                                    onAction(
                                        WorkerPendingOrderAction
                                            .ActiveOrdersClicked
                                    )
                                }

                                WorkerOrdersTabItem.PICKUPS -> {
                                    onAction(
                                        WorkerPendingOrderAction
                                            .PickupsClicked
                                    )
                                }
                            }
                        }
                    )
                }

                item(
                    key = "pending_controls",
                    contentType = "controls"
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${pendingOrders.size} Pending",
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
                                    WorkerPendingOrderAction
                                        .AcceptingOrdersChanged(
                                            accepting = accepting
                                        )
                                )
                            }
                        )
                    }
                }

                uiState.notice?.let { notice ->
                    item(
                        key = "order_notice",
                        contentType = "message"
                    ) {
                        Text(
                            text = notice,
                            style = MaterialTheme.typography.bodySmall,
                            color = colors.onSurfaceVariant
                        )
                    }
                }

                if (pendingOrders.isEmpty()) {
                    item(
                        key = "pending_empty",
                        contentType = "message"
                    ) {
                        WorkerPendingEmptyCard(
                            acceptingOrders = uiState.acceptingOrders
                        )
                    }
                }

                items(
                    items = pendingOrders,
                    key = { order ->
                        order.id
                    },
                    contentType = {
                        "pending_order"
                    }
                ) { order ->
                    WorkerPendingOrderCard(
                        order = order,
                        expanded = uiState.expandedOrderId == order.id,
                        onClick = {
                            onAction(
                                WorkerPendingOrderAction.OrderClicked(
                                    orderId = order.id
                                )
                            )
                        },
                        onManageClick = {
                            onAction(
                                WorkerPendingOrderAction.ManageOrderClicked(
                                    orderId = order.id
                                )
                            )
                        }
                    )
                }
            }
        }
    }

    uiState.managingOrder?.let { order ->
        WorkerPendingDecisionDialog(
            order = order,
            onAction = onAction
        )
    }

    if (uiState.showLogoutDialog) {
        WorkerPendingLogoutDialog(
            onAction = onAction
        )
    }
}

@Composable
private fun WorkerPendingEmptyCard(
    acceptingOrders: Boolean
) {
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
                text = "No pending orders yet",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = colors.onSurface
            )

            Text(
                text = if (acceptingOrders) {
                    "Please wait for an order."
                } else {
                    "Turn on accepting orders when you are ready."
                },
                style = MaterialTheme.typography.bodyMedium,
                color = colors.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun WorkerPendingOrderCard(
    order: WorkerOrder,
    expanded: Boolean,
    onClick: () -> Unit,
    onManageClick: () -> Unit
) {
    val colors = MaterialTheme.colorScheme

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
                    color = colors.tertiaryContainer,
                    contentColor = colors.onTertiaryContainer
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
                text = formatWorkerPendingAmount(
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
                    title = "Accept or Cancel Order",
                    onClick = onManageClick
                )
            }
        }
    }
}

@Composable
private fun WorkerPendingDecisionDialog(
    order: WorkerOrder,
    onAction: (WorkerPendingOrderAction) -> Unit
) {
    AlertDialog(
        onDismissRequest = {
            onAction(
                WorkerPendingOrderAction.DismissOrderDialogClicked
            )
        },
        title = {
            Text(
                text = "Pickup # ${order.formattedPickupNumber}"
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Accept this order to begin preparing it, " +
                            "or cancel the order."
                )

                TextButton(
                    onClick = {
                        onAction(
                            WorkerPendingOrderAction.CancelOrderClicked(
                                orderId = order.id
                            )
                        )
                    }
                ) {
                    Text(
                        text = "Cancel order",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onAction(
                        WorkerPendingOrderAction.AcceptOrderClicked(
                            orderId = order.id
                        )
                    )
                }
            ) {
                Text("Accept order")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onAction(
                        WorkerPendingOrderAction.DismissOrderDialogClicked
                    )
                }
            ) {
                Text("Close")
            }
        }
    )
}

@Composable
private fun WorkerPendingLogoutDialog(
    onAction: (WorkerPendingOrderAction) -> Unit
) {
    AlertDialog(
        onDismissRequest = {
            onAction(
                WorkerPendingOrderAction.DismissLogoutClicked
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
                        WorkerPendingOrderAction.ConfirmLogoutClicked
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
                        WorkerPendingOrderAction.DismissLogoutClicked
                    )
                }
            ) {
                Text("Stay")
            }
        }
    )
}

private fun formatWorkerPendingAmount(
    amountInSen: Long
): String {
    return String.format(
        Locale.US,
        "RM %,.2f",
        BigDecimal.valueOf(amountInSen, 2)
    )
}