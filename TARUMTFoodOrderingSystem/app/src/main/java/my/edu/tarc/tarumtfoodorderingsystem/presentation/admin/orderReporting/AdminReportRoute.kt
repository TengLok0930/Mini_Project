package my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.orderReporting

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun AdminReportRoute(
    viewModel: AdminReportViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AdminReportScreen(
        uiState = uiState,
        modifier = modifier,
        onAction = { action ->
            when (action) {
                AdminReportAction.BackClicked -> {
                    onNavigateBack()
                }

                else -> viewModel.onAction(action)
            }
        }
    )
}