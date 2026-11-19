package my.edu.tarc.tarumtfoodorderingsystem.presentation.worker.orders.pendingOrders

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun WorkerPendingOrderRoute(
    viewModel: WorkerPendingOrderViewModel,
    onNavigateToCompletedOrders: () -> Unit,
    onNavigateToItems: () -> Unit,
    onNavigateToActiveOrders: () -> Unit,
    onNavigateToPickups: () -> Unit,
    onLogoutRequested: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    WorkerPendingOrderScreen(
        uiState = uiState,
        modifier = modifier,
        onAction = { action ->
            when (action) {
                WorkerPendingOrderAction.CompletedOrdersClicked -> {
                    onNavigateToCompletedOrders()
                }

                WorkerPendingOrderAction.ItemsClicked -> {
                    onNavigateToItems()
                }

                WorkerPendingOrderAction.ActiveOrdersClicked -> {
                    onNavigateToActiveOrders()
                }

                WorkerPendingOrderAction.PickupsClicked -> {
                    onNavigateToPickups()
                }

                WorkerPendingOrderAction.ConfirmLogoutClicked -> {
                    viewModel.onAction(
                        WorkerPendingOrderAction.DismissLogoutClicked
                    )

                    onLogoutRequested()
                }

                else -> {
                    viewModel.onAction(action)
                }
            }
        }
    )
}