package my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.salesReporting.editReport

import my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.salesReporting.saveReport.AdminReportSaveType

sealed interface AdminEditReportAction {

    data object BackClicked : AdminEditReportAction

    data class ReportNameChanged(
        val name: String
    ) : AdminEditReportAction

    data class TypeToggled(
        val type: AdminReportSaveType,
        val selected: Boolean
    ) : AdminEditReportAction

    data object SaveReportClicked : AdminEditReportAction

    data object DeleteClicked : AdminEditReportAction

    data object ConfirmDeleteClicked : AdminEditReportAction

    data object DismissDeleteClicked : AdminEditReportAction
}