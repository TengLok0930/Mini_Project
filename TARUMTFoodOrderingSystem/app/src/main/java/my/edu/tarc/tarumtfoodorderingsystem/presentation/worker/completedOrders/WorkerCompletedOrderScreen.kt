package my.edu.tarc.tarumtfoodorderingsystem.presentation.worker.completedOrders

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import my.edu.tarc.tarumtfoodorderingsystem.R
import my.edu.tarc.tarumtfoodorderingsystem.presentation.worker.orders.pendingOrders.WorkerOrder
import my.edu.tarc.tarumtfoodorderingsystem.ui.components.AppTopBar
import my.edu.tarc.tarumtfoodorderingsystem.ui.components.WorkerNavigationTab
import my.edu.tarc.tarumtfoodorderingsystem.ui.components.WorkerNavigationTabItem

@Composable
fun WorkerCompletedOrderScreen(
    uiState: WorkerCompletedOrderUiState,
    onAction: (WorkerCompletedOrderAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme

    val completedOrders = uiState.completedOrders
    val filteredOrders = uiState.filteredOrders

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .imePadding(),
        containerColor = colors.background,
        topBar = {
            Column {
                AppTopBar(
                    title = "Completed Orders",
                    subtitle = uiState.storeSubtitle,
                    onLogoutClick = {
                        onAction(
                            WorkerCompletedOrderAction.LogoutClicked
                        )
                    }
                )

                WorkerNavigationTab(
                    selectedTab = WorkerNavigationTabItem.COMPLETED,
                    onTabSelected = { tab ->
                        when (tab) {
                            WorkerNavigationTabItem.ORDERS -> {
                                onAction(
                                    WorkerCompletedOrderAction.OrdersClicked
                                )
                            }

                            WorkerNavigationTabItem.COMPLETED -> {
                                // Already viewing Completed.
                            }

                            WorkerNavigationTabItem.ITEMS -> {
                                onAction(
                                    WorkerCompletedOrderAction.ItemsClicked
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
                    vertical = 20.dp
                ),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                item(
                    key = "completed_search",
                    contentType = "search"
                ) {
                    WorkerCompletedSearchField(
                        query = uiState.searchQuery,
                        onQueryChanged = { query ->
                            onAction(
                                WorkerCompletedOrderAction.SearchQueryChanged(
                                    query = query
                                )
                            )
                        }
                    )
                }

                if (completedOrders.isEmpty()) {
                    item(
                        key = "completed_empty",
                        contentType = "message"
                    ) {
                        WorkerCompletedMessageCard(
                            title = "No completed orders yet",
                            message = "When an order is collected, " +
                                    "it will appear here. Return to Active " +
                                    "to manage the queue."
                        )
                    }
                } else if (filteredOrders.isEmpty()) {
                    item(
                        key = "completed_no_results",
                        contentType = "message"
                    ) {
                        WorkerCompletedMessageCard(
                            title = "No matching orders",
                            message = "Try another order ID " +
                                    "or clear the search."
                        )
                    }
                }

                items(
                    items = filteredOrders,
                    key = { order ->
                        order.id
                    },
                    contentType = {
                        "completed_order"
                    }
                ) { order ->
                    WorkerCompletedOrderCard(
                        order = order
                    )
                }
            }
        }
    }

    if (uiState.showLogoutDialog) {
        WorkerCompletedLogoutDialog(
            onAction = onAction
        )
    }
}

@Composable
private fun WorkerCompletedSearchField(
    query: String,
    onQueryChanged: (String) -> Unit
) {
    val colors = MaterialTheme.colorScheme
    val keyboardController = LocalSoftwareKeyboardController.current

    OutlinedTextField(
        value = query,
        onValueChange = onQueryChanged,
        modifier = Modifier.fillMaxWidth(),
        placeholder = {
            Text("Search Order ID")
        },
        leadingIcon = {
            Icon(
                painter = painterResource(R.drawable.search_40px),
                contentDescription = null
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(
                    onClick = {
                        onQueryChanged("")
                    }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.close_40px),
                        contentDescription = "Clear search"
                    )
                }
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(16.dp),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                keyboardController?.hide()
            }
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = colors.surfaceVariant,
            unfocusedContainerColor = colors.surfaceVariant,
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent
        )
    )
}

@Composable
private fun WorkerCompletedMessageCard(
    title: String,
    message: String
) {
    val colors = MaterialTheme.colorScheme

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = colors.surfaceVariant
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = 20.dp,
                vertical = 24.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = colors.onSurface
            )

            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = colors.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun WorkerCompletedOrderCard(
    order: WorkerOrder
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

    val paidTextColor = if (isDarkBackground) {
        Color(0xFF9FE3B3)
    } else {
        Color(0xFF1B6A32)
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = colors.surfaceVariant
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 20.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Order #${order.id}",
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

            Text(
                text = formatWorkerCollectionDate(
                    collectedAtMillis = order.collectedAtMillis
                ),
                style = MaterialTheme.typography.bodySmall,
                color = colors.onSurfaceVariant
            )

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
                text = formatWorkerCompletedAmount(
                    order.totalInSen
                ) + " · Paid",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = paidTextColor
            )
        }
    }
}

@Composable
private fun WorkerCompletedLogoutDialog(
    onAction: (WorkerCompletedOrderAction) -> Unit
) {
    AlertDialog(
        onDismissRequest = {
            onAction(
                WorkerCompletedOrderAction.DismissLogoutClicked
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
                        WorkerCompletedOrderAction.ConfirmLogoutClicked
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
                        WorkerCompletedOrderAction.DismissLogoutClicked
                    )
                }
            ) {
                Text("Stay")
            }
        }
    )
}

private fun formatWorkerCompletedAmount(
    amountInSen: Long
): String {
    return String.format(
        Locale.US,
        "RM %,.2f",
        BigDecimal.valueOf(amountInSen, 2)
    )
}

private fun formatWorkerCollectionDate(
    collectedAtMillis: Long?
): String {
    if (collectedAtMillis == null) {
        return "Collected at the counter"
    }

    val now = Calendar.getInstance()

    val collectedDay = Calendar.getInstance().apply {
        timeInMillis = collectedAtMillis
    }

    val isToday =
        now.get(Calendar.ERA) == collectedDay.get(Calendar.ERA) &&
                now.get(Calendar.YEAR) == collectedDay.get(Calendar.YEAR) &&
                now.get(Calendar.DAY_OF_YEAR) ==
                collectedDay.get(Calendar.DAY_OF_YEAR)

    val dateLabel = if (isToday) {
        "Today"
    } else {
        SimpleDateFormat(
            "d MMM yyyy, h:mm a",
            Locale.ENGLISH
        ).format(
            Date(collectedAtMillis)
        )
    }

    return "$dateLabel · Collected at the counter"
}