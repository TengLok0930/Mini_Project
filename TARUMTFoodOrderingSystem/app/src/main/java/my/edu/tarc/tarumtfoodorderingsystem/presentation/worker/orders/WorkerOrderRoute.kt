package my.edu.tarc.tarumtfoodorderingsystem.presentation.worker.orders

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun WorkerOrderRoute(
    viewModel: WorkerOrderViewModel,
    onNavigateToPendingOrders: () -> Unit,
    onNavigateToPickups: () -> Unit,
    onNavigateToCompletedOrders: () -> Unit,
    onNavigateToItems: () -> Unit,
    onAcceptingOrdersChanged: (Boolean) -> Unit,
    onMarkReadyForPickup: (Int) -> Boolean,
    onLogoutRequested: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    WorkerOrderScreen(
        uiState = uiState,
        modifier = modifier,
        onAction = { action ->
            when (action) {
                WorkerOrderAction.PendingOrdersClicked -> {
                    onNavigateToPendingOrders()
                }

                WorkerOrderAction.PickupsClicked -> {
                    onNavigateToPickups()
                }

                WorkerOrderAction.CompletedOrdersClicked -> {
                    onNavigateToCompletedOrders()
                }

                WorkerOrderAction.ItemsClicked -> {
                    onNavigateToItems()
                }

                is WorkerOrderAction.AcceptingOrdersChanged -> {
                    onAcceptingOrdersChanged(
                        action.accepting
                    )
                }

                is WorkerOrderAction.MarkReadyForPickupClicked -> {
                    val updated = onMarkReadyForPickup(
                        action.orderId
                    )

                    if (updated) {
                        viewModel.onAction(action)
                    }
                }

                WorkerOrderAction.ConfirmLogoutClicked -> {
                    viewModel.onAction(
                        WorkerOrderAction.DismissLogoutClicked
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