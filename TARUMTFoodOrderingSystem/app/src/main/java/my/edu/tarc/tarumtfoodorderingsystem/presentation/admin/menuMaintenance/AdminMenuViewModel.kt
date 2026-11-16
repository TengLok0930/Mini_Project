package my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.menuMaintenance

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class AdminMenuStoreOption(
    val id: Int,
    val canteenId: Int,
    val name: String,
    val canteenName: String
) {
    val displayName: String
        get() = "$name · $canteenName"
}

data class AdminMenuItem(
    val id: Int,
    val storeId: Int,
    val name: String,
    val category: String,
    val priceInSen: Int,
    val isPublished: Boolean = true
)

data class AdminMenuUiState(
    val stores: List<AdminMenuStoreOption> = emptyList(),
    val menuItems: List<AdminMenuItem> = emptyList(),
    val selectedStoreId: Int? = null,
    val pendingRemoval: AdminMenuItem? = null
) {
    val selectedStore: AdminMenuStoreOption?
        get() = stores.firstOrNull {
            it.id == selectedStoreId
        }

    val publishedItems: List<AdminMenuItem>
        get() = if (selectedStore == null) {
            emptyList()
        } else {
            menuItems.filter {
                it.storeId == selectedStoreId && it.isPublished
            }
        }

    val groupedPublishedItems: Map<String, List<AdminMenuItem>>
        get() = publishedItems.groupBy { item ->
            item.category.trim().ifBlank { "Uncategorized" }
        }
}

class AdminMenuViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        adminMenuSampleState()
    )

    val uiState = _uiState.asStateFlow()

    fun onAction(action: AdminMenuAction) {
        when (action) {
            is AdminMenuAction.StoreSelected -> {
                selectStore(action.storeId)
            }

            is AdminMenuAction.RemoveMenuItemClicked -> {
                requestRemoval(action.menuItemId)
            }

            AdminMenuAction.ConfirmRemoveClicked -> {
                removeSelectedItem()
            }

            AdminMenuAction.DismissRemoveClicked -> {
                _uiState.update {
                    it.copy(pendingRemoval = null)
                }
            }

            // Handled by AdminMenuRoute.
            AdminMenuAction.BackClicked,
            AdminMenuAction.CreateMenuItemClicked -> Unit
        }
    }

    private fun selectStore(storeId: Int) {
        _uiState.update { state ->
            if (state.stores.none { it.id == storeId }) {
                state
            } else {
                state.copy(
                    selectedStoreId = storeId,
                    pendingRemoval = null
                )
            }
        }
    }

    private fun requestRemoval(menuItemId: Int) {
        _uiState.update { state ->
            state.copy(
                pendingRemoval = state.publishedItems.firstOrNull {
                    it.id == menuItemId
                }
            )
        }
    }

    private fun removeSelectedItem() {
        _uiState.update { state ->
            val item = state.pendingRemoval

            if (
                item == null ||
                item.storeId != state.selectedStoreId
            ) {
                state.copy(pendingRemoval = null)
            } else {
                state.copy(
                    menuItems = state.menuItems.filterNot {
                        it.id == item.id
                    },
                    pendingRemoval = null
                )
            }
        }
    }
}

/**
 * Frontend sample data shared with the screen previews.
 * Replace with repository data when persistence is implemented.
 */
internal fun adminMenuSampleState(): AdminMenuUiState {
    return AdminMenuUiState(
        stores = listOf(
            AdminMenuStoreOption(
                id = 1,
                canteenId = 1,
                name = "Chicken Rice",
                canteenName = "Yum Yum Cafeteria"
            ),
            AdminMenuStoreOption(
                id = 2,
                canteenId = 1,
                name = "Noodles",
                canteenName = "Yum Yum Cafeteria"
            ),
            AdminMenuStoreOption(
                id = 3,
                canteenId = 1,
                name = "Mamak Food",
                canteenName = "Yum Yum Cafeteria"
            ),
            AdminMenuStoreOption(
                id = 6,
                canteenId = 2,
                name = "Western Food",
                canteenName = "Red Bricks Cafeteria"
            ),
            AdminMenuStoreOption(
                id = 8,
                canteenId = 3,
                name = "Claypot Rice",
                canteenName = "Heritage Kitchen"
            )
        ),
        menuItems = listOf(
            // Chicken Rice · Yum Yum Cafeteria
            AdminMenuItem(
                id = 1,
                storeId = 1,
                name = "Chicken Rice",
                category = "Rice",
                priceInSen = 650
            ),
            AdminMenuItem(
                id = 2,
                storeId = 1,
                name = "Chicken Chop Rice",
                category = "Rice",
                priceInSen = 750
            ),
            AdminMenuItem(
                id = 3,
                storeId = 1,
                name = "Ipoh Hor Fun",
                category = "Noodle",
                priceInSen = 700
            ),
            AdminMenuItem(
                id = 4,
                storeId = 1,
                name = "Curry Mee",
                category = "Noodle",
                priceInSen = 700
            ),

            // Noodles · Yum Yum Cafeteria
            AdminMenuItem(
                id = 5,
                storeId = 2,
                name = "Wantan Mee",
                category = "Noodle",
                priceInSen = 650
            ),
            AdminMenuItem(
                id = 6,
                storeId = 2,
                name = "Pan Mee",
                category = "Noodle",
                priceInSen = 700
            ),

            // Mamak Food · Yum Yum Cafeteria
            AdminMenuItem(
                id = 7,
                storeId = 3,
                name = "Nasi Goreng",
                category = "Rice",
                priceInSen = 650
            ),
            AdminMenuItem(
                id = 8,
                storeId = 3,
                name = "Teh Tarik",
                category = "Beverage",
                priceInSen = 250
            ),

            // Western Food · Red Bricks Cafeteria
            AdminMenuItem(
                id = 9,
                storeId = 6,
                name = "Chicken Chop",
                category = "Western",
                priceInSen = 1200
            ),
            AdminMenuItem(
                id = 10,
                storeId = 6,
                name = "Fish and Chips",
                category = "Western",
                priceInSen = 1350
            )

            // Claypot Rice intentionally has no items
            // so you can test the empty state.
        )
    )
}