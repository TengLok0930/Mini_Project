package my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.salesReporting.editReport

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.salesReporting.adminReportSampleState
import my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.salesReporting.reportDetails.reportTitle
import my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.salesReporting.saveReport.AdminReportSaveType
import my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.salesReporting.saveReport.AdminSavedReport
import my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.salesReporting.saveReport.MAX_SAVED_REPORT_NAME_LENGTH

data class AdminEditReportUiState(
    val storeId: Int,
    val storeDisplayName: String,
    val startDateMillis: Long,
    val endDateMillis: Long,
    val reportName: String,
    val selectedTypes: Set<AdminReportSaveType>,
    val showDeleteDialog: Boolean = false
) {
    val canSave: Boolean
        get() = reportName.isNotBlank() && selectedTypes.isNotEmpty()
}

class AdminEditReportViewModel(
    storeId: Int,
    startDateMillis: Long,
    endDateMillis: Long
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        AdminEditReportUiState(
            storeId = storeId,
            // Sample lookup only. Replace with the report repository.
            storeDisplayName = adminReportSampleState().stores
                .firstOrNull { it.id == storeId }
                ?.displayName
                .orEmpty(),
            startDateMillis = startDateMillis,
            endDateMillis = endDateMillis,
            reportName = reportTitle(
                startDateMillis = startDateMillis,
                endDateMillis = endDateMillis
            ).take(MAX_SAVED_REPORT_NAME_LENGTH),
            // Sample: replace with the types stored on the saved report.
            selectedTypes = AdminReportSaveType.entries.toSet()
        )
    )

    val uiState = _uiState.asStateFlow()

    fun onAction(action: AdminEditReportAction) {
        when (action) {
            is AdminEditReportAction.ReportNameChanged -> {
                _uiState.update {
                    it.copy(
                        reportName = action.name.take(
                            MAX_SAVED_REPORT_NAME_LENGTH
                        )
                    )
                }
            }

            is AdminEditReportAction.TypeToggled -> {
                _uiState.update { state ->
                    state.copy(
                        selectedTypes = if (action.selected) {
                            state.selectedTypes + action.type
                        } else {
                            state.selectedTypes - action.type
                        }
                    )
                }
            }

            AdminEditReportAction.DeleteClicked -> {
                _uiState.update {
                    it.copy(showDeleteDialog = true)
                }
            }

            AdminEditReportAction.DismissDeleteClicked -> {
                _uiState.update {
                    it.copy(showDeleteDialog = false)
                }
            }

            // Handled by AdminEditReportRoute.
            AdminEditReportAction.BackClicked,
            AdminEditReportAction.SaveReportClicked,
            AdminEditReportAction.ConfirmDeleteClicked -> Unit
        }
    }

    /** Returns the updated report, or null when the form is incomplete. */
    fun saveReport(): AdminSavedReport? {
        val state = _uiState.value

        if (!state.canSave) {
            return null
        }

        // TODO: Update the stored report through the report repository.
        return AdminSavedReport(
            name = state.reportName.trim(),
            storeId = state.storeId,
            startDateMillis = state.startDateMillis,
            endDateMillis = state.endDateMillis,
            types = state.selectedTypes
        )
    }

    /** Returns true once the report has been deleted. */
    fun deleteReport(): Boolean {
        // TODO: Delete the stored report through the report repository.
        return true
    }
}