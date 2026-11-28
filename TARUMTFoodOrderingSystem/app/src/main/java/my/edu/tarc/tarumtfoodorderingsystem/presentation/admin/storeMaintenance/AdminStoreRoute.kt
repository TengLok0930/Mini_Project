package my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.storeMaintenance

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun AdminStoreRoute(
    viewModel: AdminStoreViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToCreateStore: (Int) -> Unit,
    onNavigateToEditStore: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AdminStoreScreen(
        uiState = uiState,
        modifier = modifier,
        onAction = { action ->
            when (action) {
                AdminStoreAction.BackClicked -> {
                    onNavigateBack()
                }

                AdminStoreAction.CreateStoreClicked -> {
                    uiState.selectedCanteen?.let { canteen ->
                        onNavigateToCreateStore(canteen.id)
                    }
                }

                is AdminStoreAction.EditStoreClicked -> {
                    val store = uiState.visibleStores.firstOrNull {
                        it.id == action.storeId
                    }

                    if (store != null) {
                        onNavigateToEditStore(store.id)
                    }
                }

                is AdminStoreAction.CanteenSelected -> {
                    viewModel.onAction(action)
                }
            }
        }
    )
}