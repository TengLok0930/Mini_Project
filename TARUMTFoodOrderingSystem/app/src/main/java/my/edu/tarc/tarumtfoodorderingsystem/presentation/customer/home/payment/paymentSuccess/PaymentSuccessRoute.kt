package my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.home.payment.paymentSuccess

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun PaymentSuccessRoute(
    viewModel: PaymentSuccessViewModel,
    onNavigateToOrders: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    BackHandler {
        onNavigateToOrders()
    }

    PaymentSuccessScreen(
        uiState = uiState,
        modifier = modifier,
        onAction = { action ->
            when (action) {
                PaymentSuccessAction.ViewOrdersClicked -> {
                    onNavigateToOrders()
                }
            }
        }
    )
}