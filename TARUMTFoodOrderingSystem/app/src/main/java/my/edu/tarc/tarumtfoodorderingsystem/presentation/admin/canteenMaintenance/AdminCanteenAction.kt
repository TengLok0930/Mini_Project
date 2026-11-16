package my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.canteenMaintenance

sealed interface AdminCanteenAction {

    data object BackClicked : AdminCanteenAction

    data object CreateCanteenClicked : AdminCanteenAction

    data class RemoveCanteenClicked(
        val canteenId: Int
    ) : AdminCanteenAction

    data object ConfirmRemoveClicked : AdminCanteenAction

    data object DismissRemoveClicked : AdminCanteenAction
}