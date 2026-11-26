package my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.salesReporting.reportDetails

sealed interface AdminReportDetailAction {

    data object BackClicked : AdminReportDetailAction

    data class EditReportClicked(
        val storeId: Int,
        val startDateMillis: Long,
        val endDateMillis: Long
    ) : AdminReportDetailAction
}