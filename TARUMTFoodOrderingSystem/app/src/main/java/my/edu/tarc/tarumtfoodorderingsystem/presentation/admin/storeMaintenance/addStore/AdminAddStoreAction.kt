package my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.storeMaintenance.addStore

sealed interface AdminAddStoreAction {

    data class NameChanged(
        val value: String
    ) : AdminAddStoreAction

    data class CanteenSelected(
        val canteenId: Int
    ) : AdminAddStoreAction

    data object PickImageClicked : AdminAddStoreAction

    data object ImageReadingStarted : AdminAddStoreAction

    data class ImageSelected(
        val uri: String,
        val width: Int,
        val height: Int
    ) : AdminAddStoreAction

    data class ImageReadingFailed(
        val message: String
    ) : AdminAddStoreAction

    data object RemoveImageClicked : AdminAddStoreAction

    data object CreateClicked : AdminAddStoreAction

    data object BackClicked : AdminAddStoreAction
}