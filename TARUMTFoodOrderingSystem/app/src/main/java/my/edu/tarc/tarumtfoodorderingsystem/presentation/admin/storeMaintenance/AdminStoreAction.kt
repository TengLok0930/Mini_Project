package my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.storeMaintenance

sealed interface AdminStoreAction {

    data object BackClicked : AdminStoreAction

    data class CanteenSelected(
        val canteenId: Int
    ) : AdminStoreAction

    data object CreateStoreClicked : AdminStoreAction

    data class EditStoreClicked(
        val storeId: Int
    ) : AdminStoreAction
}