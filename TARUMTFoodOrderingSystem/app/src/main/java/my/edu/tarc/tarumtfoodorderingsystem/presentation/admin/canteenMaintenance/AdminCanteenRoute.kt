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
    onNavigateToEditCanteen: (Int) -> Unit,
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

                is AdminCanteenAction.EditCanteenClicked -> {
                    val canteen = uiState.canteens.firstOrNull {
                        it.id == action.canteenId
                    }

                    if (canteen != null) {
                        onNavigateToEditCanteen(canteen.id)
                    }
                }
            }
        }
    )
}