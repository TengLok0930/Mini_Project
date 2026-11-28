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
    val expandedOrderId: Int? = null,
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
    val lastCollectedOrderId: Int? = null,
    val expandedOrderId: Int? = null
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
                localState.update { state ->
                    state.copy(
                        expandedOrderId = null,
                        lastCollectedOrderId = action.orderId
                    )
                }
            }

            is WorkerPickupOrderAction.OrderClicked -> {
                toggleExpandedOrder(
                    orderId = action.orderId
                )
            }

            WorkerPickupOrderAction.PendingOrdersClicked,
            WorkerPickupOrderAction.ActiveOrdersClicked,
            WorkerPickupOrderAction.CompletedOrdersClicked,
            WorkerPickupOrderAction.ItemsClicked,
            WorkerPickupOrderAction.ProfileClicked,
            is WorkerPickupOrderAction.AcceptingOrdersChanged -> {

            }
        }
    }

    private fun toggleExpandedOrder(
        orderId: Int
    ) {
        val shared = sharedState.value

        val exists = shared.orders.any { order ->
            order.id == orderId &&
                    order.storeId == shared.storeId &&
                    !order.isCancelled &&
                    order.status == OrderStatus.READY
        }

        if (!exists) {
            return
        }

        localState.update { state ->
            state.copy(
                expandedOrderId = if (
                    state.expandedOrderId == orderId
                ) {
                    null
                } else {
                    orderId
                }
            )
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
            expandedOrderId = local.expandedOrderId,
            acceptingOrders = shared.acceptingOrders,
            showLogoutDialog = local.showLogoutDialog,
            lastCollectedOrderId = local.lastCollectedOrderId
        )
    }
}