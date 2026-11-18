package my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.home.orderItem

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun OrderItemRoute(
    viewModel: OrderItemViewModel,
    availableQuantity: Int,
    onNavigateBack: () -> Unit,
    onAddToCart: (OrderItemDraft) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(availableQuantity) {
        viewModel.updateAvailableQuantity(
            availableQuantity = availableQuantity
        )
    }

    OrderItemScreen(
        uiState = uiState,
        modifier = modifier,
        onAction = { action ->
            when (action) {
                OrderItemAction.BackClicked -> {
                    onNavigateBack()
                }

                OrderItemAction.OrderNowClicked -> {
                    viewModel.createDraft()?.let { draft ->
                        onAddToCart(draft)
                    }
                }

                else -> {
                    viewModel.onAction(action)
                }
            }
        }
    )
}