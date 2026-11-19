package my.edu.tarc.tarumtfoodorderingsystem.presentation.worker.completedOrders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.orders.OrderStatus
import my.edu.tarc.tarumtfoodorderingsystem.presentation.worker.orders.pendingOrders.WorkerOrder
import my.edu.tarc.tarumtfoodorderingsystem.presentation.worker.orders.pendingOrders.WorkerPendingOrderUiState

data class WorkerCompletedOrderUiState(
    val storeId: Int,
    val storeName: String,
    val canteenName: String,
    val orders: List<WorkerOrder> = emptyList(),
    val searchQuery: String = "",
    val showLogoutDialog: Boolean = false
) {
    val storeSubtitle: String
        get() = listOf(
            storeName,
            canteenName
        )
            .filter { it.isNotBlank() }
            .joinToString(" · ")

    val completedOrders: List<WorkerOrder>
        get() = orders
            .filter { order ->
                order.storeId == storeId &&
                        !order.isCancelled &&
                        order.status == OrderStatus.DELIVERED
            }
            .sortedWith(
                compareByDescending<WorkerOrder> {
                    it.collectedAtMillis ?: it.createdAtMillis
                }.thenByDescending {
                    it.id
                }
            )

    val filteredOrders: List<WorkerOrder>
        get() {
            if (searchQuery.isBlank()) {
                return completedOrders
            }

            val normalizedQuery = searchQuery
                .trim()
                .removePrefix("#")
                .trim()

            if (normalizedQuery.isBlank()) {
                return emptyList()
            }

            return completedOrders.filter { order ->
                order.id
                    .toString()
                    .contains(normalizedQuery)
            }
        }
}

private data class WorkerCompletedOrderLocalState(
    val searchQuery: String = "",
    val showLogoutDialog: Boolean = false
)

class WorkerCompletedOrderViewModel(
    private val sharedState: StateFlow<WorkerPendingOrderUiState>
) : ViewModel() {

    private val localState = MutableStateFlow(
        WorkerCompletedOrderLocalState()
    )

    val uiState: StateFlow<WorkerCompletedOrderUiState> = combine(
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
        action: WorkerCompletedOrderAction
    ) {
        when (action) {
            is WorkerCompletedOrderAction.SearchQueryChanged -> {
                localState.update {
                    it.copy(
                        searchQuery = action.query
                    )
                }
            }

            WorkerCompletedOrderAction.LogoutClicked -> {
                localState.update {
                    it.copy(
                        showLogoutDialog = true
                    )
                }
            }

            WorkerCompletedOrderAction.DismissLogoutClicked -> {
                localState.update {
                    it.copy(
                        showLogoutDialog = false
                    )
                }
            }

            WorkerCompletedOrderAction.OrdersClicked,
            WorkerCompletedOrderAction.ItemsClicked,
            WorkerCompletedOrderAction.ConfirmLogoutClicked -> {
                // Navigation is handled by the Route.
            }
        }
    }

    private fun buildUiState(
        shared: WorkerPendingOrderUiState,
        local: WorkerCompletedOrderLocalState
    ): WorkerCompletedOrderUiState {
        return WorkerCompletedOrderUiState(
            storeId = shared.storeId,
            storeName = shared.storeName,
            canteenName = shared.canteenName,
            orders = shared.orders,
            searchQuery = local.searchQuery,
            showLogoutDialog = local.showLogoutDialog
        )
    }
}