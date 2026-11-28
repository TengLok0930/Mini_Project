package my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.salesReporting.reportDetails

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun AdminReportDetailRoute(
    viewModel: AdminReportDetailViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToEditReport: (
        storeId: Int,
        startDateMillis: Long,
        endDateMillis: Long
    ) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AdminReportDetailScreen(
        uiState = uiState,
        modifier = modifier,
        onAction = { action ->
            when (action) {
                AdminReportDetailAction.BackClicked -> {
                    onNavigateBack()
                }

                is AdminReportDetailAction.EditReportClicked -> {
                    onNavigateToEditReport(
                        action.storeId,
                        action.startDateMillis,
                        action.endDateMillis
                    )
                }
            }
        }
    )
}