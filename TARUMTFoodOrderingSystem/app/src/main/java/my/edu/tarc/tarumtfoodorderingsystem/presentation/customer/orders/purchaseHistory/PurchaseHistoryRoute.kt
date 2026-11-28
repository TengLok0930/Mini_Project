package my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.orders.purchaseHistory

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun PurchaseHistoryRoute(
    viewModel: PurchaseHistoryViewModel,
    isDarkTheme: Boolean,
    onNavigateToOrders: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToProfile: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    PurchaseHistoryScreen(
        uiState = uiState,
        isDarkTheme = isDarkTheme,
        modifier = modifier,
        onAction = { action ->
            when (action) {
                PurchaseHistoryAction.OrdersClicked -> {
                    onNavigateToOrders()
                }

                PurchaseHistoryAction.HomeClicked -> {
                    onNavigateToHome()
                }

                PurchaseHistoryAction.ProfileClicked -> {
                    onNavigateToProfile()
                }

                PurchaseHistoryAction.PurchaseHistoryClicked -> {
                    // Already viewing Purchase History.
                }
            }
        }
    )
}