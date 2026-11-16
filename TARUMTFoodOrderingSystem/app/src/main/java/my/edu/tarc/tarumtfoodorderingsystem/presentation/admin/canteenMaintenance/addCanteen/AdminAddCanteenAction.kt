package my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.canteenMaintenance.addCanteen

sealed interface AdminAddCanteenAction {

    data class NameChanged(
        val value: String
    ) : AdminAddCanteenAction

    data object PickImageClicked : AdminAddCanteenAction

    data object ImageReadingStarted : AdminAddCanteenAction

    data class ImageSelected(
        val uri: String,
        val width: Int,
        val height: Int
    ) : AdminAddCanteenAction

    data class ImageReadingFailed(
        val message: String
    ) : AdminAddCanteenAction

    data object RemoveImageClicked : AdminAddCanteenAction

    data object CreateClicked : AdminAddCanteenAction

    data object BackClicked : AdminAddCanteenAction
}