package my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.menuMaintenance

sealed interface AdminMenuAction {

    data object BackClicked : AdminMenuAction

    data class StoreSelected(
        val storeId: Int
    ) : AdminMenuAction

    data object CreateMenuItemClicked : AdminMenuAction

    data class RemoveMenuItemClicked(
        val menuItemId: Int
    ) : AdminMenuAction

    data object ConfirmRemoveClicked : AdminMenuAction

    data object DismissRemoveClicked : AdminMenuAction
}