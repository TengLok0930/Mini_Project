package my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.home.store

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.home.canteen.CanteenAction

@Composable
fun StoreRoute(
    viewModel: StoreViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToMenu: (Int) -> Unit,
    onNavigateToOrders: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToInformation: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    StoreScreen(
        uiState = uiState,
        modifier = modifier,
        onAction = { action ->
            when (action) {
                StoreAction.BackClicked -> {
                    onNavigateBack()
                }

                is StoreAction.StoreClicked -> {
                    val store = uiState.availableStores.firstOrNull {
                        it.id == action.storeId
                    }

                    if (store != null) {
                        onNavigateToMenu(store.id)
                    }
                }

                StoreAction.OrdersClicked -> {
                    onNavigateToOrders()
                }

                StoreAction.HomeClicked -> {
                    onNavigateToHome()
                }

                StoreAction.ProfileClicked -> {
                    onNavigateToProfile()
                }

                StoreAction.InformationClicked -> {
                    onNavigateToInformation()
                }
            }
        }
    )
}