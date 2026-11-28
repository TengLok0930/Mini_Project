package my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.canteenMaintenance

sealed interface AdminCanteenAction {

    data object BackClicked : AdminCanteenAction

    data object CreateCanteenClicked : AdminCanteenAction

    data class EditCanteenClicked(
        val canteenId: Int
    ) : AdminCanteenAction
}