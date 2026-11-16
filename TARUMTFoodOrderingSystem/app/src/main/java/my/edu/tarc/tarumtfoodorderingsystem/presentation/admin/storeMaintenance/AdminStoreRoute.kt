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
                    val selectedCanteen = uiState.selectedCanteen

                    if (selectedCanteen != null) {
                        onNavigateToCreateStore(selectedCanteen.id)
                    }
                }

                else -> viewModel.onAction(action)
            }
        }
    )
}