package my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.orders

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun OrdersRoute(
    viewModel: OrdersViewModel,
    isDarkTheme: Boolean,
    onNavigateToPurchaseHistory: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToProfile: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    OrdersScreen(
        uiState = uiState,
        isDarkTheme = isDarkTheme,
        modifier = modifier,
        onAction = { action ->
            when (action) {
                OrdersAction.PurchaseHistoryClicked -> {
                    onNavigateToPurchaseHistory()
                }

                OrdersAction.HomeClicked -> {
                    onNavigateToHome()
                }

                OrdersAction.ProfileClicked -> {
                    onNavigateToProfile()
                }

                OrdersAction.OrdersClicked -> {
                    // Already viewing active orders.
                }
            }
        }
    )
}