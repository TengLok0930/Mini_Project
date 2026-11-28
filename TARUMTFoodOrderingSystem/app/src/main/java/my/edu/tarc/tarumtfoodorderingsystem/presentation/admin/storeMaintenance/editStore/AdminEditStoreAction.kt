package my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.storeMaintenance.editStore

sealed interface AdminEditStoreAction {

    data object BackClicked : AdminEditStoreAction

    data object DeleteClicked : AdminEditStoreAction

    data object ConfirmDeleteClicked : AdminEditStoreAction

    data object DismissDeleteClicked : AdminEditStoreAction

    data class NameChanged(
        val value: String
    ) : AdminEditStoreAction

    data object PickImageClicked : AdminEditStoreAction

    data object RemoveImageClicked : AdminEditStoreAction

    data class ImageFailed(
        val message: String
    ) : AdminEditStoreAction

    data object UpdateClicked : AdminEditStoreAction
}