package my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.home.store

import androidx.annotation.DrawableRes
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.home.canteen.CustomerCanteenItem
import my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.home.canteen.customerCanteenSampleState

data class CustomerStoreItem(
    val id: Int,
    val canteenId: Int,
    val name: String,
    val foodCount: Int,
    val beverageCount: Int,
    val imageUrl: String? = null,
    @DrawableRes val imageRes: Int? = null,
    val isAvailable: Boolean = true
)

data class StoreUiState(
    val canteen: CustomerCanteenItem? = null,
    val stores: List<CustomerStoreItem> = emptyList()
) {
    val availableStores: List<CustomerStoreItem>
        get() {
            val selectedCanteen = canteen ?: return emptyList()

            return stores.filter {
                it.canteenId == selectedCanteen.id &&
                        it.isAvailable
            }
        }
}

class StoreViewModel(
    canteenId: Int
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        customerStoreSampleState(canteenId)
    )

    val uiState = _uiState.asStateFlow()
}

/**
 * Frontend sample data only.
 * Replace with repository/Supabase data later.
 */
internal fun customerStoreSampleState(
    canteenId: Int = 1
): StoreUiState {
    val canteen = customerCanteenSampleState()
        .canteens
        .firstOrNull {
            it.id == canteenId && it.isAvailable
        }

    return StoreUiState(
        canteen = canteen,
        stores = listOf(
            CustomerStoreItem(
                id = 1,
                canteenId = 1,
                name = "Chicken Rice",
                foodCount = 16,
                beverageCount = 0
            ),
            CustomerStoreItem(
                id = 2,
                canteenId = 1,
                name = "Noodles",
                foodCount = 24,
                beverageCount = 0
            ),
            CustomerStoreItem(
                id = 3,
                canteenId = 1,
                name = "Mamak Food",
                foodCount = 50,
                beverageCount = 12
            ),
            CustomerStoreItem(
                id = 4,
                canteenId = 1,
                name = "Masakan Malaysia",
                foodCount = 32,
                beverageCount = 28
            ),
            CustomerStoreItem(
                id = 5,
                canteenId = 1,
                name = "Drinks",
                foodCount = 0,
                beverageCount = 18
            ),
            CustomerStoreItem(
                id = 6,
                canteenId = 2,
                name = "Western Food",
                foodCount = 20,
                beverageCount = 5
            ),
            CustomerStoreItem(
                id = 7,
                canteenId = 2,
                name = "Mixed Rice",
                foodCount = 28,
                beverageCount = 0
            ),
            CustomerStoreItem(
                id = 8,
                canteenId = 3,
                name = "Claypot Rice",
                foodCount = 12,
                beverageCount = 0
            ),
            CustomerStoreItem(
                id = 9,
                canteenId = 4,
                name = "Local Delights",
                foodCount = 18,
                beverageCount = 6
            )

            // FM Café intentionally has no sample stores
            // so you can test the empty state.
        )
    )
}