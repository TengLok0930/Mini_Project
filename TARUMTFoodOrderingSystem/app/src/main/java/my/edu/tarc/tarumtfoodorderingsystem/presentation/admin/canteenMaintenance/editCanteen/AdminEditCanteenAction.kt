package my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.canteenMaintenance.editCanteen

sealed interface AdminEditCanteenAction {

    data object BackClicked : AdminEditCanteenAction

    data object DeleteClicked : AdminEditCanteenAction

    data object ConfirmDeleteClicked : AdminEditCanteenAction

    data object DismissDeleteClicked : AdminEditCanteenAction

    data class NameChanged(
        val value: String
    ) : AdminEditCanteenAction

    data object PickImageClicked : AdminEditCanteenAction

    data object RemoveImageClicked : AdminEditCanteenAction

    data class ImageFailed(
        val message: String
    ) : AdminEditCanteenAction

    data object UpdateClicked : AdminEditCanteenAction
}