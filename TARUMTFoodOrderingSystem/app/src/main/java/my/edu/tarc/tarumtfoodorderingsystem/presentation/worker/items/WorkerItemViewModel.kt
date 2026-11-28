package my.edu.tarc.tarumtfoodorderingsystem.presentation.worker.items

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import my.edu.tarc.tarumtfoodorderingsystem.presentation.worker.orders.pendingOrders.WorkerPendingOrderUiState

data class WorkerMenuItem(
    val id: Int,
    val storeId: Int,
    val name: String,
    val priceInSen: Long,
    val availableQuantity: Int,
    val soldQuantity: Int
) {
    val isLowStock: Boolean
        get() = availableQuantity < 70
}

data class WorkerItemUiState(
    val storeId: Int,
    val storeName: String,
    val canteenName: String,
    val items: List<WorkerMenuItem> = emptyList(),
    val showLogoutDialog: Boolean = false
) {
    val storeSubtitle: String
        get() = listOf(
            storeName,
            canteenName
        )
            .filter { it.isNotBlank() }
            .joinToString(" · ")
}

private data class WorkerItemLocalState(
    val items: List<WorkerMenuItem>,
    val showLogoutDialog: Boolean = false
)

class WorkerItemViewModel(
    private val sharedState: StateFlow<WorkerPendingOrderUiState>
) : ViewModel() {

    private val localState = MutableStateFlow(
        WorkerItemLocalState(
            items = workerMenuSampleData(
                storeId = sharedState.value.storeId
            )
        )
    )

    val uiState: StateFlow<WorkerItemUiState> = combine(
        sharedState,
        localState
    ) { shared, local ->
        buildUiState(
            shared = shared,
            local = local
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = buildUiState(
            shared = sharedState.value,
            local = localState.value
        )
    )

    fun onAction(
        action: WorkerItemAction
    ) {
        when (action) {
            WorkerItemAction.OrdersClicked,
            WorkerItemAction.CompletedOrdersClicked,
            WorkerItemAction.ProfileClicked,
            is WorkerItemAction.EditItemClicked -> {
                // Navigation is handled by WorkerItemRoute.
            }
        }
    }

    private fun buildUiState(
        shared: WorkerPendingOrderUiState,
        local: WorkerItemLocalState
    ): WorkerItemUiState {
        return WorkerItemUiState(
            storeId = shared.storeId,
            storeName = shared.storeName,
            canteenName = shared.canteenName,
            items = local.items.filter { item ->
                item.storeId == shared.storeId
            },
            showLogoutDialog = local.showLogoutDialog
        )
    }

    fun findItem(
        itemId: Int
    ): WorkerMenuItem? {
        val storeId = sharedState.value.storeId

        return localState.value.items.firstOrNull { item ->
            item.id == itemId &&
                    item.storeId == storeId
        }
    }

    fun updateItem(
        itemId: Int,
        priceInSen: Long,
        availableQuantity: Int
    ): Boolean {
        if (priceInSen <= 0L || availableQuantity < 0) {
            return false
        }

        val existingItem = findItem(
            itemId = itemId
        ) ?: return false

        localState.update { state ->
            state.copy(
                items = state.items.map { item ->
                    if (
                        item.id == existingItem.id &&
                        item.storeId == existingItem.storeId
                    ) {
                        item.copy(
                            priceInSen = priceInSen,
                            availableQuantity = availableQuantity
                        )
                    } else {
                        item
                    }
                }
            )
        }

        return true
    }
}

private fun workerMenuSampleData(
    storeId: Int
): List<WorkerMenuItem> {
    return listOf(
        WorkerMenuItem(
            id = 1,
            storeId = storeId,
            name = "Chicken Rice",
            priceInSen = 650L,
            availableQuantity = 124,
            soldQuantity = 362
        ),
        WorkerMenuItem(
            id = 2,
            storeId = storeId,
            name = "Chicken Chop Rice",
            priceInSen = 750L,
            availableQuantity = 110,
            soldQuantity = 210
        ),
        WorkerMenuItem(
            id = 3,
            storeId = storeId,
            name = "Ipoh Hor Fun",
            priceInSen = 700L,
            availableQuantity = 20,
            soldQuantity = 152
        )
    )
}