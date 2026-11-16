package my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.storeMaintenance

sealed interface AdminStoreAction {
    data object BackClicked : AdminStoreAction

    data class CanteenSelected(
        val canteenId: Int
    ) : AdminStoreAction

    data object CreateStoreClicked : AdminStoreAction

    data class RemoveStoreClicked(
        val storeId: Int
    ) : AdminStoreAction

    data object ConfirmRemoveClicked : AdminStoreAction

    data object DismissRemoveClicked : AdminStoreAction
}