package my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.home.cart

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun CartRoute(
    viewModel: CartViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToPayment: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CartScreen(
        uiState = uiState,
        modifier = modifier,
        onAction = { action ->
            when (action) {
                CartAction.BackClicked -> {
                    onNavigateBack()
                }

                CartAction.ProceedToPaymentClicked -> {
                    if (uiState.canProceedToPayment) {
                        onNavigateToPayment()
                    }
                }

                else -> {
                    viewModel.onAction(action)
                }
            }
        }
    )
}