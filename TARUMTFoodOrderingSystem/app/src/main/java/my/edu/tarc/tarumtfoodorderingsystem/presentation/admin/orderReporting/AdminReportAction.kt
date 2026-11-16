package my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.orderReporting

sealed interface AdminReportAction {

    data object BackClicked : AdminReportAction

    data class StoreSelected(
        val storeId: Int
    ) : AdminReportAction
}