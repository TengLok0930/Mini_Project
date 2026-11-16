package my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.menuMaintenance.addMenuItem

sealed interface AdminAddItemAction {

    data class NameChanged(
        val value: String
    ) : AdminAddItemAction

    data class StoreSelected(
        val storeId: Int
    ) : AdminAddItemAction

    data class CategoryChanged(
        val value: String
    ) : AdminAddItemAction

    data class ItemTypeSelected(
        val itemType: MenuItemType
    ) : AdminAddItemAction

    data class PriceChanged(
        val value: String
    ) : AdminAddItemAction

    data object PickImageClicked : AdminAddItemAction

    data object RemoveImageClicked : AdminAddItemAction

    data class ImageFailed(
        val message: String
    ) : AdminAddItemAction

    data object PublishClicked : AdminAddItemAction

    data object BackClicked : AdminAddItemAction
}