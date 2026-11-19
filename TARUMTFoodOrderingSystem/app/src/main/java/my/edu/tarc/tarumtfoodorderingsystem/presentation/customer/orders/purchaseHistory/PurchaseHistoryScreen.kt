package my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.orders.purchaseHistory

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.orders.CustomerOrdersContent
import my.edu.tarc.tarumtfoodorderingsystem.ui.components.CustomerTab

@Composable
fun PurchaseHistoryScreen(
    uiState: PurchaseHistoryUiState,
    isDarkTheme: Boolean,
    onAction: (PurchaseHistoryAction) -> Unit,
    modifier: Modifier = Modifier
) {
    CustomerOrdersContent(
        orders = uiState.previousOrders,
        isHistory = true,
        isDarkTheme = isDarkTheme,
        onShowOrders = {
            onAction(
                PurchaseHistoryAction.OrdersClicked
            )
        },
        onShowHistory = {
            onAction(
                PurchaseHistoryAction.PurchaseHistoryClicked
            )
        },
        onBottomTabSelected = { tab ->
            when (tab) {
                CustomerTab.ORDERS -> {
                    onAction(
                        PurchaseHistoryAction.OrdersClicked
                    )
                }

                CustomerTab.HOME -> {
                    onAction(
                        PurchaseHistoryAction.HomeClicked
                    )
                }

                CustomerTab.PROFILE -> {
                    onAction(
                        PurchaseHistoryAction.ProfileClicked
                    )
                }
            }
        },
        modifier = modifier
    )
}