package my.edu.tarc.tarumtfoodorderingsystem.presentation.worker.completedOrders

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun WorkerCompletedOrderRoute(
    viewModel: WorkerCompletedOrderViewModel,
    onNavigateToOrders: () -> Unit,
    onNavigateToItems: () -> Unit,
    onNavigateToWorkerProfile: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    WorkerCompletedOrderScreen(
        uiState = uiState,
        modifier = modifier,
        onAction = { action ->
            when (action) {
                WorkerCompletedOrderAction.OrdersClicked -> {
                    onNavigateToOrders()
                }

                WorkerCompletedOrderAction.ItemsClicked -> {
                    onNavigateToItems()
                }

                WorkerCompletedOrderAction.ProfileClicked -> {
                    onNavigateToWorkerProfile()
                }

                else -> {
                    viewModel.onAction(action)
                }
            }
        }
    )
}