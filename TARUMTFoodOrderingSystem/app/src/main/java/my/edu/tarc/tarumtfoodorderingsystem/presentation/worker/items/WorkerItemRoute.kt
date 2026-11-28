package my.edu.tarc.tarumtfoodorderingsystem.presentation.worker.items

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun WorkerItemRoute(
    viewModel: WorkerItemViewModel,
    onNavigateToOrders: () -> Unit,
    onNavigateToCompletedOrders: () -> Unit,
    onNavigateToEditItem: (Int) -> Unit,
    onNavigateToWorkerProfile: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    WorkerItemScreen(
        uiState = uiState,
        modifier = modifier,
        onAction = { action ->
            when (action) {
                WorkerItemAction.OrdersClicked -> {
                    onNavigateToOrders()
                }

                WorkerItemAction.CompletedOrdersClicked -> {
                    onNavigateToCompletedOrders()
                }

                is WorkerItemAction.EditItemClicked -> {
                    onNavigateToEditItem(
                        action.itemId
                    )
                }

                WorkerItemAction.ProfileClicked -> {
                    onNavigateToWorkerProfile()
                }

                else -> {
                    viewModel.onAction(action)
                }
            }
        }
    )
}