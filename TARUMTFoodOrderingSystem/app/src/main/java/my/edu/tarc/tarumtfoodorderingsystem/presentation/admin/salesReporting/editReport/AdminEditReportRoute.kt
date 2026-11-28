package my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.salesReporting.editReport

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun AdminEditReportRoute(
    viewModel: AdminEditReportViewModel,
    onNavigateBack: () -> Unit,
    onReportSaved: (reportName: String) -> Unit,
    onReportDeleted: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AdminEditReportScreen(
        uiState = uiState,
        modifier = modifier,
        onAction = { action ->
            when (action) {
                AdminEditReportAction.BackClicked -> {
                    onNavigateBack()
                }

                AdminEditReportAction.SaveReportClicked -> {
                    val saved = viewModel.saveReport()

                    if (saved != null) {
                        onReportSaved(saved.name)
                    }
                }

                AdminEditReportAction.ConfirmDeleteClicked -> {
                    viewModel.onAction(
                        AdminEditReportAction.DismissDeleteClicked
                    )

                    if (viewModel.deleteReport()) {
                        onReportDeleted()
                    }
                }

                else -> viewModel.onAction(action)
            }
        }
    )
}