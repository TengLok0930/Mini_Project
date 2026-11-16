package my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.canteenMaintenance

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun AdminCanteenRoute(
    viewModel: AdminCanteenViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToCreateCanteen: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AdminCanteenScreen(
        uiState = uiState,
        modifier = modifier,
        onAction = { action ->
            when (action) {
                AdminCanteenAction.BackClicked -> {
                    onNavigateBack()
                }

                AdminCanteenAction.CreateCanteenClicked -> {
                    onNavigateToCreateCanteen()
                }

                else -> viewModel.onAction(action)
            }
        }
    )
}