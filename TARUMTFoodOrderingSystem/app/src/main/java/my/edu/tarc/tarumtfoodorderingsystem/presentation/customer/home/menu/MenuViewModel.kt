package my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.home.menu

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class CustomerItemType {
    FOOD,
    BEVERAGE
}

data class CustomerMenuItem(
    val id: Int,
    val storeId: Int,
    val name: String,
    val category: String,
    val priceInSen: Long,
    val quantitySold: Int,
    val quantityRemaining: Int,
    val imageUrl: String? = null,
    val imageRes: Int? = null,
    val itemType: CustomerItemType = CustomerItemType.FOOD
) {
    val isAvailable: Boolean
        get() = quantityRemaining > 0
}

data class MenuUiState(
    val storeId: Int,
    val storeName: String,
    val canteenName: String,
    val items: List<CustomerMenuItem> = emptyList()
) {
    val storeSubtitle: String
        get() = listOf(
            storeName,
            canteenName
        )
            .filter { it.isNotBlank() }
            .joinToString(" · ")

    val storeItems: List<CustomerMenuItem>
        get() = items.filter {
            it.storeId == storeId
        }

    val topSellingItems: List<CustomerMenuItem>
        get() = storeItems
            .filter {
                it.quantitySold > 0
            }
            .sortedWith(
                compareByDescending<CustomerMenuItem> {
                    it.quantitySold
                }.thenBy {
                    it.id
                }
            )
            .take(3)

    val itemsByCategory: Map<String, List<CustomerMenuItem>>
        get() = storeItems.groupBy {
            it.category
        }
}

class MenuViewModel(
    storeId: Int,
    storeName: String,
    canteenName: String
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        MenuUiState(
            storeId = storeId,
            storeName = storeName,
            canteenName = canteenName,
            items = createSampleMenuItems(
                storeId = storeId
            )
        )
    )

    val uiState = _uiState.asStateFlow()
}

/**
 * Frontend sample data only.
 *
 * Each store currently receives the same demonstration menu,
 * with the items associated with the selected store ID.
 *
 * Replace this with repository data when persistence is added.
 */
private fun createSampleMenuItems(
    storeId: Int
): List<CustomerMenuItem> {
    return listOf(
        CustomerMenuItem(
            id = 1,
            storeId = storeId,
            name = "Chicken Rice",
            category = "Rice",
            priceInSen = 650L,
            quantitySold = 320,
            quantityRemaining = 125
        ),
        CustomerMenuItem(
            id = 2,
            storeId = storeId,
            name = "Chicken Chop Rice",
            category = "Rice",
            priceInSen = 750L,
            quantitySold = 240,
            quantityRemaining = 110
        ),
        CustomerMenuItem(
            id = 3,
            storeId = storeId,
            name = "Ipoh Hor Fun",
            category = "Noodles",
            priceInSen = 700L,
            quantitySold = 150,
            quantityRemaining = 105
        ),
        CustomerMenuItem(
            id = 4,
            storeId = storeId,
            name = "Fried Rice",
            category = "Rice",
            priceInSen = 600L,
            quantitySold = 80,
            quantityRemaining = 35
        ),
        CustomerMenuItem(
            id = 5,
            storeId = storeId,
            name = "Teh",
            category = "Beverages",
            priceInSen = 320L,
            quantitySold = 53,
            quantityRemaining = 31,
            itemType = CustomerItemType.BEVERAGE
        )
    )
}