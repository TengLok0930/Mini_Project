package my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.salesReporting.saveReport

sealed interface AdminSaveReportAction {

    data object BackClicked : AdminSaveReportAction

    data class ReportNameChanged(
        val name: String
    ) : AdminSaveReportAction

    data class TypeToggled(
        val type: AdminReportSaveType,
        val selected: Boolean
    ) : AdminSaveReportAction

    data object SaveReportClicked : AdminSaveReportAction
}