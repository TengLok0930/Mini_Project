package my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.menuMaintenance

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun AdminMenuRoute(
    viewModel: AdminMenuViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToCreateMenuItem: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AdminMenuScreen(
        uiState = uiState,
        modifier = modifier,
        onAction = { action ->
            when (action) {
                AdminMenuAction.BackClicked -> {
                    onNavigateBack()
                }

                AdminMenuAction.CreateMenuItemClicked -> {
                    val store = uiState.selectedStore

                    if (store != null) {
                        onNavigateToCreateMenuItem(store.id)
                    }
                }

                else -> viewModel.onAction(action)
            }
        }
    )
}