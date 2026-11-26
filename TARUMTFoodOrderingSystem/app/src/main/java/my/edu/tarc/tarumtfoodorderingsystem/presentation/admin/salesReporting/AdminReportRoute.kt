package my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.salesReporting

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun AdminReportRoute(
    viewModel: AdminReportViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToReportDetails: (
        storeId: Int,
        startDateMillis: Long,
        endDateMillis: Long
    ) -> Unit,
    onNavigateToSaveReport: (
        storeId: Int,
        startDateMillis: Long,
        endDateMillis: Long
    ) -> Unit,
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

                is AdminReportAction.ViewReportClicked -> {
                    onNavigateToReportDetails(
                        action.storeId,
                        action.startDateMillis,
                        action.endDateMillis
                    )
                }

                is AdminReportAction.SaveReportClicked -> {
                    onNavigateToSaveReport(
                        action.storeId,
                        action.startDateMillis,
                        action.endDateMillis
                    )
                }

                else -> viewModel.onAction(action)
            }
        }
    )
}