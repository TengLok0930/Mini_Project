package my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.orders

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import my.edu.tarc.tarumtfoodorderingsystem.ui.components.AppTopBar
import my.edu.tarc.tarumtfoodorderingsystem.ui.components.CustomerBottomBar
import my.edu.tarc.tarumtfoodorderingsystem.ui.components.CustomerTab

@Composable
fun OrdersScreen(
    uiState: OrdersUiState,
    isDarkTheme: Boolean,
    onAction: (OrdersAction) -> Unit,
    modifier: Modifier = Modifier
) {
    CustomerOrdersContent(
        orders = uiState.activeOrders,
        isHistory = false,
        isDarkTheme = isDarkTheme,
        onShowOrders = {
            onAction(OrdersAction.OrdersClicked)
        },
        onShowHistory = {
            onAction(OrdersAction.PurchaseHistoryClicked)
        },
        onBottomTabSelected = { tab ->
            when (tab) {
                CustomerTab.ORDERS -> {
                    onAction(OrdersAction.OrdersClicked)
                }

                CustomerTab.HOME -> {
                    onAction(OrdersAction.HomeClicked)
                }

                CustomerTab.PROFILE -> {
                    onAction(OrdersAction.ProfileClicked)
                }
            }
        },
        modifier = modifier
    )
}

/**
 * Shared layout used by OrdersScreen and PurchaseHistoryScreen.
 */
@Composable
internal fun CustomerOrdersContent(
    orders: List<CustomerOrder>,
    isHistory: Boolean,
    isDarkTheme: Boolean,
    onShowOrders: () -> Unit,
    onShowHistory: () -> Unit,
    onBottomTabSelected: (CustomerTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    val selectedTab = if (isHistory) 1 else 0

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = colors.background,
        topBar = {
            Column {
                AppTopBar(
                    title = "Orders"
                )

                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = colors.surfaceVariant,
                    contentColor = colors.secondary,
                    indicator = { positions ->
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(
                                positions[selectedTab]
                            ),
                            color = colors.secondary
                        )
                    }
                ) {
                    Tab(
                        selected = !isHistory,
                        onClick = {
                            if (isHistory) onShowOrders()
                        },
                        selectedContentColor = colors.secondary,
                        unselectedContentColor = colors.onSurfaceVariant,
                        text = {
                            Text(
                                text = "Orders",
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    )

                    Tab(
                        selected = isHistory,
                        onClick = {
                            if (!isHistory) onShowHistory()
                        },
                        selectedContentColor = colors.secondary,
                        unselectedContentColor = colors.onSurfaceVariant,
                        text = {
                            Text(
                                text = "Purchase History",
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    )
                }
            }
        },
        bottomBar = {
            CustomerBottomBar(
                selectedTab = CustomerTab.ORDERS,
                onTabSelected = onBottomTabSelected
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
                    horizontal = 20.dp,
                    vertical = 22.dp
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item(key = "orders_heading") {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (isHistory) {
                                    "Previous Orders"
                                } else {
                                    "Active orders"
                                },
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = colors.secondary,
                                modifier = Modifier.weight(1f)
                            )

                            if (!isHistory) {
                                Text(
                                    text = if (orders.size == 1) {
                                        "1 order"
                                    } else {
                                        "${orders.size} orders"
                                    },
                                    style = MaterialTheme.typography.bodySmall,
                                    color = colors.onSurfaceVariant
                                )
                            }
                        }

                        if (!isHistory) {
                            Spacer(Modifier.height(4.dp))

                            Text(
                                text = "Show your pickup number at the counter.",
                                style = MaterialTheme.typography.bodySmall,
                                color = colors.onSurfaceVariant
                            )
                        }
                    }
                }

                if (orders.isEmpty()) {
                    item(key = "orders_empty") {
                        Text(
                            text = if (isHistory) {
                                "You have no previous orders."
                            } else {
                                "You have no active orders."
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            color = colors.onSurfaceVariant
                        )
                    }
                }

                items(
                    items = orders,
                    key = { order -> order.id }
                ) { order ->
                    CustomerOrderCard(
                        order = order,
                        isHistory = isHistory,
                        isDarkTheme = isDarkTheme
                    )
                }
            }
        }
    }
}

@Composable
private fun CustomerOrderCard(
    order: CustomerOrder,
    isHistory: Boolean,
    isDarkTheme: Boolean
) {
    val colors = MaterialTheme.colorScheme
    val isReady = !isHistory && order.status == OrderStatus.READY

    val containerColor = if (isReady) {
        if (isDarkTheme) Color(0xFF087A50) else Color(0xFFD8F5E7)
    } else {
        colors.surfaceVariant
    }

    val mainTextColor = if (isReady) {
        if (isDarkTheme) Color.White else Color(0xFF103C2A)
    } else {
        colors.onSurface
    }

    val secondaryTextColor = if (isReady) {
        if (isDarkTheme) Color(0xFFD3F3E3) else Color(0xFF365F4D)
    } else {
        colors.onSurfaceVariant
    }

    val headingColor = if (isReady) {
        if (isDarkTheme) Color(0xFFC5FF8A) else Color(0xFF315A12)
    } else {
        mainTextColor
    }

    val amount = remember(order.totalInSen) {
        String.format(
            Locale.US,
            "RM %,.2f",
            BigDecimal.valueOf(order.totalInSen, 2)
        )
    }

    val supportingText = if (isHistory) {
        formatOrderTimestamp(
            millis = order.placedAtMillis,
            allowToday = false
        )
    } else {
        "${order.storeName} · ${order.canteenName}"
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = containerColor
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = if (isHistory) {
                        "Order # ${order.id}"
                    } else {
                        "Pickup # ${order.pickupNumber}"
                    },
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = headingColor,
                    modifier = Modifier.weight(1f)
                )

                OrderStatusBadge(
                    status = order.status,
                    isDarkTheme = isDarkTheme
                )
            }

            Spacer(Modifier.height(8.dp))

            Text(
                text = supportingText,
                style = MaterialTheme.typography.bodySmall,
                color = secondaryTextColor
            )

            Spacer(Modifier.height(22.dp))

            order.items.forEachIndexed { index, item ->
                if (index > 0) {
                    Spacer(Modifier.height(8.dp))
                }

                Text(
                    text = "${item.quantity} × ${item.name}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = mainTextColor
                )

                item.remarks
                    ?.trim()
                    ?.takeIf { it.isNotBlank() }
                    ?.let { remarks ->
                        Spacer(Modifier.height(3.dp))

                        Text(
                            text = "Remarks: $remarks",
                            style = MaterialTheme.typography.bodySmall,
                            color = secondaryTextColor
                        )
                    }
            }

            Spacer(Modifier.height(22.dp))

            if (!isHistory) {
                val message = when (order.status) {
                    OrderStatus.ORDERED,
                    OrderStatus.PREPARING -> {
                        val minutes = order.estimatedWaitMinutes
                            ?: (
                                    order.items.sumOf { item ->
                                        item.quantity
                                    } * 3
                                    )

                        val unit = if (minutes == 1) {
                            "min"
                        } else {
                            "mins"
                        }

                        "Estimated Wait Time: $minutes $unit"
                    }

                    OrderStatus.READY -> {
                        "Please pick up your food"
                    }

                    OrderStatus.DELIVERED -> null
                }

                message?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = mainTextColor
                    )

                    Spacer(Modifier.height(4.dp))
                }
            }

            Text(
                text = amount,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = mainTextColor
            )
        }
    }
}

@Composable
private fun OrderStatusBadge(
    status: OrderStatus,
    isDarkTheme: Boolean
) {
    val palette = when (status) {
        OrderStatus.ORDERED -> {
            if (isDarkTheme) {
                Color(0xFF263C65) to Color(0xFFC8D9FF)
            } else {
                Color(0xFFDCE7FF) to Color(0xFF214278)
            }
        }

        OrderStatus.PREPARING -> {
            if (isDarkTheme) {
                Color(0xFF51432A) to Color(0xFFFFD68A)
            } else {
                Color(0xFFFFEDC4) to Color(0xFF694700)
            }
        }

        OrderStatus.READY,
        OrderStatus.DELIVERED -> {
            if (isDarkTheme) {
                Color(0xFF214D3B) to Color(0xFFBBF0CD)
            } else {
                Color(0xFFD5EFDD) to Color(0xFF205332)
            }
        }
    }

    Surface(
        color = palette.first,
        contentColor = palette.second,
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = status.displayName,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(
                horizontal = 10.dp,
                vertical = 6.dp
            )
        )
    }
}

private fun formatOrderTimestamp(
    millis: Long,
    allowToday: Boolean
): String {
    val now = Calendar.getInstance()

    val orderDay = Calendar.getInstance().apply {
        timeInMillis = millis
    }

    val isToday =
        now.get(Calendar.ERA) == orderDay.get(Calendar.ERA) &&
                now.get(Calendar.YEAR) == orderDay.get(Calendar.YEAR) &&
                now.get(Calendar.DAY_OF_YEAR) == orderDay.get(Calendar.DAY_OF_YEAR)

    val pattern = if (allowToday && isToday) {
        "'Today,' h:mm a"
    } else {
        "d MMM yyyy, h:mm a"
    }

    return SimpleDateFormat(
        pattern,
        Locale.ENGLISH
    ).format(Date(millis))
}