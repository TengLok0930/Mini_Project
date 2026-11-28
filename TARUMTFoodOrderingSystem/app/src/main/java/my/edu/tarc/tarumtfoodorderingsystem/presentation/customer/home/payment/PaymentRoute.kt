package my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.home.payment

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun PaymentRoute(
    viewModel: PaymentViewModel,
    onNavigateBack: () -> Unit,
    onRequestPayment: (PaymentRequest) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.refreshValidation()
    }

    PaymentScreen(
        uiState = uiState,
        modifier = modifier,
        onAction = { action ->
            when (action) {
                PaymentAction.BackClicked -> {
                    onNavigateBack()
                }

                PaymentAction.PayClicked -> {
                    viewModel.createPaymentRequest()?.let { request ->
                        onRequestPayment(request)
                    }
                }

                else -> {
                    viewModel.onAction(action)
                }
            }
        }
    )
}