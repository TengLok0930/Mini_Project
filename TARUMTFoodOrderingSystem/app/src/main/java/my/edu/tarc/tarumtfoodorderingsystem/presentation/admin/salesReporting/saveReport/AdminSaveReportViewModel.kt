package my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.salesReporting.saveReport

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.salesReporting.adminReportSampleState

internal const val MAX_SAVED_REPORT_NAME_LENGTH = 40

enum class AdminReportSaveType(
    val displayName: String
) {
    SALES("Sales"),
    ITEM_SOLD_COUNT("Item Sold Count")
}

data class AdminSavedReport(
    val name: String,
    val storeId: Int,
    val startDateMillis: Long,
    val endDateMillis: Long,
    val types: Set<AdminReportSaveType>
)

data class AdminSaveReportUiState(
    val storeId: Int,
    val storeDisplayName: String,
    val startDateMillis: Long,
    val endDateMillis: Long,
    val reportName: String = "",
    val selectedTypes: Set<AdminReportSaveType> = emptySet()
) {
    val canSave: Boolean
        get() = reportName.isNotBlank() && selectedTypes.isNotEmpty()
}

class AdminSaveReportViewModel(
    storeId: Int,
    startDateMillis: Long,
    endDateMillis: Long
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        AdminSaveReportUiState(
            storeId = storeId,
            // Sample lookup only. Replace with the store repository later.
            storeDisplayName = adminReportSampleState().stores
                .firstOrNull { it.id == storeId }
                ?.displayName
                .orEmpty(),
            startDateMillis = startDateMillis,
            endDateMillis = endDateMillis
        )
    )

    val uiState = _uiState.asStateFlow()

    fun onAction(action: AdminSaveReportAction) {
        when (action) {
            is AdminSaveReportAction.ReportNameChanged -> {
                _uiState.update {
                    it.copy(
                        reportName = action.name.take(
                            MAX_SAVED_REPORT_NAME_LENGTH
                        )
                    )
                }
            }

            is AdminSaveReportAction.TypeToggled -> {
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

            // Handled by AdminSaveReportRoute.
            AdminSaveReportAction.BackClicked,
            AdminSaveReportAction.SaveReportClicked -> Unit
        }
    }

    /**
     * Returns the saved report, or null when the form is incomplete.
     */
    fun saveReport(): AdminSavedReport? {
        val state = _uiState.value

        if (!state.canSave) {
            return null
        }

        val report = AdminSavedReport(
            name = state.reportName.trim(),
            storeId = state.storeId,
            startDateMillis = state.startDateMillis,
            endDateMillis = state.endDateMillis,
            types = state.selectedTypes
        )

        // TODO: Persist through the report repository, saving the Sales
        //  total and/or the Item Sold Count for this store and date range
        //  according to report.types.

        return report
    }
}