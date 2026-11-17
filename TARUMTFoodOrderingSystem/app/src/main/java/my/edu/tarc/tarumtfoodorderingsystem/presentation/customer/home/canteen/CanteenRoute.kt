package my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.home.canteen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun CanteenRoute(
    viewModel: CanteenViewModel,
    onNavigateToStores: (Int) -> Unit,
    onNavigateToOrders: () -> Unit,
    onNavigateToProfile: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CanteenScreen(
        uiState = uiState,
        modifier = modifier,
        onAction = { action ->
            when (action) {
                is CanteenAction.CanteenClicked -> {
                    val canteen = uiState.availableCanteens.firstOrNull {
                        it.id == action.canteenId
                    }

                    if (canteen != null) {
                        onNavigateToStores(canteen.id)
                    }
                }

                CanteenAction.OrdersClicked -> {
                    onNavigateToOrders()
                }

                CanteenAction.ProfileClicked -> {
                    onNavigateToProfile()
                }

                CanteenAction.HomeClicked -> {
                    // Already on the Home destination.
                }
            }
        }
    )
}