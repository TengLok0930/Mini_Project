package my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.salesReporting

sealed interface AdminReportAction {

    data object BackClicked : AdminReportAction

    data class StoreSelected(
        val storeId: Int
    ) : AdminReportAction

    data class StartDateSelected(
        val dateMillis: Long
    ) : AdminReportAction

    data class EndDateSelected(
        val dateMillis: Long
    ) : AdminReportAction

    data class ViewReportClicked(
        val storeId: Int,
        val startDateMillis: Long,
        val endDateMillis: Long
    ) : AdminReportAction

    data class SaveReportClicked(
        val storeId: Int,
        val startDateMillis: Long,
        val endDateMillis: Long
    ) : AdminReportAction
}