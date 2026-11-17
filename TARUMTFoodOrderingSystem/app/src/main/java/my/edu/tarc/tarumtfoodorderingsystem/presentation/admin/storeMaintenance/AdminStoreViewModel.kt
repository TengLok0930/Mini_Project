package my.edu.tarc.tarumtfoodorderingsystem.presentation.admin.storeMaintenance

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class StoreCanteenOption(
    val id: Int,
    val name: String
)

data class AdminStoreItem(
    val id: Int,
    val canteenId: Int,
    val name: String,
    val foodCount: Int,
    val beverageCount: Int,
    val imageUri: String? = null
)

data class AdminStoreUiState(
    val canteens: List<StoreCanteenOption> = emptyList(),
    val stores: List<AdminStoreItem> = emptyList(),
    val selectedCanteenId: Int? = null
) {
    val selectedCanteen: StoreCanteenOption?
        get() = canteens.firstOrNull {
            it.id == selectedCanteenId
        }

    val visibleStores: List<AdminStoreItem>
        get() = if (selectedCanteen == null) {
            emptyList()
        } else {
            stores.filter {
                it.canteenId == selectedCanteenId
            }
        }
}

class AdminStoreViewModel : ViewModel() {

    // Frontend sample data only.
    private val _uiState = MutableStateFlow(
        AdminStoreUiState(
            canteens = listOf(
                StoreCanteenOption(1, "Yum Yum Cafeteria"),
                StoreCanteenOption(2, "Red Bricks Cafeteria"),
                StoreCanteenOption(3, "Heritage Kitchen"),
                StoreCanteenOption(4, "Casuarina"),
                StoreCanteenOption(5, "FM Café")
            ),
            stores = listOf(
                // Yum Yum Cafeteria
                AdminStoreItem(
                    id = 1,
                    canteenId = 1,
                    name = "Chicken Rice",
                    foodCount = 16,
                    beverageCount = 0
                ),
                AdminStoreItem(
                    id = 2,
                    canteenId = 1,
                    name = "Noodles",
                    foodCount = 24,
                    beverageCount = 0
                ),
                AdminStoreItem(
                    id = 3,
                    canteenId = 1,
                    name = "Mamak Food",
                    foodCount = 50,
                    beverageCount = 12
                ),
                AdminStoreItem(
                    id = 4,
                    canteenId = 1,
                    name = "Masakan Malaysia",
                    foodCount = 32,
                    beverageCount = 0
                ),
                AdminStoreItem(
                    id = 5,
                    canteenId = 1,
                    name = "Drinks",
                    foodCount = 0,
                    beverageCount = 18
                ),

                // Red Bricks Cafeteria
                AdminStoreItem(
                    id = 6,
                    canteenId = 2,
                    name = "Western Food",
                    foodCount = 20,
                    beverageCount = 5
                ),
                AdminStoreItem(
                    id = 7,
                    canteenId = 2,
                    name = "Mixed Rice",
                    foodCount = 28,
                    beverageCount = 0
                ),

                // Heritage Kitchen
                AdminStoreItem(
                    id = 8,
                    canteenId = 3,
                    name = "Claypot Rice",
                    foodCount = 12,
                    beverageCount = 0
                ),

                // Casuarina
                AdminStoreItem(
                    id = 9,
                    canteenId = 4,
                    name = "Local Delights",
                    foodCount = 18,
                    beverageCount = 6
                )

                // FM Café intentionally has no stores
                // so you can test the empty state.
            )
        )
    )

    val uiState = _uiState.asStateFlow()

    fun onAction(action: AdminStoreAction) {
        when (action) {
            is AdminStoreAction.CanteenSelected -> {
                selectCanteen(action.canteenId)
            }

            // Navigation is handled by AdminStoreRoute.
            AdminStoreAction.BackClicked,
            AdminStoreAction.CreateStoreClicked,
            is AdminStoreAction.EditStoreClicked -> Unit
        }
    }

    private fun selectCanteen(canteenId: Int) {
        _uiState.update { state ->
            if (state.canteens.none { it.id == canteenId }) {
                state
            } else {
                state.copy(
                    selectedCanteenId = canteenId
                )
            }
        }
    }

    fun updateStore(
        storeId: Int,
        name: String,
        imageUri: String
    ) {
        _uiState.update { state ->
            state.copy(
                stores = state.stores.map { store ->
                    if (store.id == storeId) {
                        store.copy(
                            name = name,
                            imageUri = imageUri
                        )
                    } else {
                        store
                    }
                }
            )
        }
    }
}