package my.edu.tarc.tarumtfoodorderingsystem.presentation.worker.orders.pickupOrders

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

data class WorkerPickupOrderUiState(
    val storeId: Int,
    val storeName: String,
    val canteenName: String,
    val orders: List<WorkerOrder> = emptyList(),
    val acceptingOrders: Boolean = true,
    val showLogoutDialog: Boolean = false,
    val lastCollectedOrderId: Int? = null
) {
    val storeSubtitle: String
        get() = listOf(
            storeName,
            canteenName
        )
            .filter { it.isNotBlank() }
            .joinToString(" · ")

    val pickupOrders: List<WorkerOrder>
        get() = orders
            .filter { order ->
                order.storeId == storeId &&
                        !order.isCancelled &&
                        order.status == OrderStatus.READY
            }
            .sortedWith(
                compareBy<WorkerOrder> {
                    it.createdAtMillis
                }.thenBy {
                    it.id
                }
            )
}

private data class WorkerPickupOrderLocalState(
    val showLogoutDialog: Boolean = false,
    val lastCollectedOrderId: Int? = null
)

class WorkerPickupOrderViewModel(
    private val sharedState: StateFlow<WorkerPendingOrderUiState>
) : ViewModel() {

    private val localState = MutableStateFlow(
        WorkerPickupOrderLocalState()
    )

    val uiState: StateFlow<WorkerPickupOrderUiState> = combine(
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
        action: WorkerPickupOrderAction
    ) {
        when (action) {
            is WorkerPickupOrderAction.ConfirmCollectionClicked -> {
                // The Route calls this only after the shared
                // order has successfully changed to DELIVERED.
                localState.update {
                    it.copy(
                        lastCollectedOrderId = action.orderId
                    )
                }
            }

            WorkerPickupOrderAction.LogoutClicked -> {
                localState.update {
                    it.copy(
                        showLogoutDialog = true
                    )
                }
            }

            WorkerPickupOrderAction.DismissLogoutClicked -> {
                localState.update {
                    it.copy(
                        showLogoutDialog = false
                    )
                }
            }

            WorkerPickupOrderAction.PendingOrdersClicked,
            WorkerPickupOrderAction.ActiveOrdersClicked,
            WorkerPickupOrderAction.CompletedOrdersClicked,
            WorkerPickupOrderAction.ItemsClicked,
            WorkerPickupOrderAction.ConfirmLogoutClicked,
            is WorkerPickupOrderAction.AcceptingOrdersChanged -> {
                // Navigation and shared-state changes are handled
                // through callbacks in WorkerPickupOrderRoute.
            }
        }
    }

    private fun buildUiState(
        shared: WorkerPendingOrderUiState,
        local: WorkerPickupOrderLocalState
    ): WorkerPickupOrderUiState {
        return WorkerPickupOrderUiState(
            storeId = shared.storeId,
            storeName = shared.storeName,
            canteenName = shared.canteenName,
            orders = shared.orders,
            acceptingOrders = shared.acceptingOrders,
            showLogoutDialog = local.showLogoutDialog,
            lastCollectedOrderId = local.lastCollectedOrderId
        )
    }
}