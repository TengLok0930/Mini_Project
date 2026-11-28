package my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.salesReporting.saveReport

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun AdminSaveReportRoute(
    viewModel: AdminSaveReportViewModel,
    onNavigateBack: () -> Unit,
    onReportSaved: (reportName: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AdminSaveReportScreen(
        uiState = uiState,
        modifier = modifier,
        onAction = { action ->
            when (action) {
                AdminSaveReportAction.BackClicked -> {
                    onNavigateBack()
                }

                AdminSaveReportAction.SaveReportClicked -> {
                    val saved = viewModel.saveReport()

                    if (saved != null) {
                        onReportSaved(saved.name)
                    }
                }

                else -> viewModel.onAction(action)
            }
        }
    )
}