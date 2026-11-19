package my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.profile.voucher

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun VoucherRoute(
    viewModel: VoucherViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToOrders: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToProfile: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.refreshAvailability()
    }

    VoucherScreen(
        uiState = uiState,
        modifier = modifier,
        onAction = { action ->
            when (action) {
                VoucherAction.BackClicked -> {
                    onNavigateBack()
                }

                VoucherAction.OrdersClicked -> {
                    onNavigateToOrders()
                }

                VoucherAction.HomeClicked -> {
                    onNavigateToHome()
                }

                VoucherAction.ProfileClicked -> {
                    onNavigateToProfile()
                }
            }
        }
    )
}