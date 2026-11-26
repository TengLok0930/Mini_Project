package my.edu.tarc.tarumtfoodorderingsystem.presentation.worker.orders.pickupOrders

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun WorkerPickupOrderRoute(
    viewModel: WorkerPickupOrderViewModel,
    onNavigateToPendingOrders: () -> Unit,
    onNavigateToActiveOrders: () -> Unit,
    onNavigateToCompletedOrders: () -> Unit,
    onNavigateToItems: () -> Unit,
    onAcceptingOrdersChanged: (Boolean) -> Unit,
    onConfirmCollection: (Int) -> Boolean,
    onNavigateToWorkerProfile: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    WorkerPickupOrderScreen(
        uiState = uiState,
        modifier = modifier,
        onAction = { action ->
            when (action) {
                WorkerPickupOrderAction.PendingOrdersClicked -> {
                    onNavigateToPendingOrders()
                }

                WorkerPickupOrderAction.ActiveOrdersClicked -> {
                    onNavigateToActiveOrders()
                }

                WorkerPickupOrderAction.CompletedOrdersClicked -> {
                    onNavigateToCompletedOrders()
                }

                WorkerPickupOrderAction.ItemsClicked -> {
                    onNavigateToItems()
                }

                is WorkerPickupOrderAction.AcceptingOrdersChanged -> {
                    onAcceptingOrdersChanged(
                        action.accepting
                    )
                }

                is WorkerPickupOrderAction.ConfirmCollectionClicked -> {
                    val updated = onConfirmCollection(
                        action.orderId
                    )

                    if (updated) {
                        viewModel.onAction(action)
                    }
                }

                WorkerPickupOrderAction.ProfileClicked -> {
                    onNavigateToWorkerProfile()
                }

                else -> {
                    viewModel.onAction(action)
                }
            }
        }
    )
}