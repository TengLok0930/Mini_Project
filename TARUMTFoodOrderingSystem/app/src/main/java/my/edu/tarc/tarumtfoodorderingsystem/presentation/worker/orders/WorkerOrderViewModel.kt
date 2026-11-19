package my.edu.tarc.tarumtfoodorderingsystem.presentation.worker.orders

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

data class WorkerOrderUiState(
    val storeId: Int,
    val storeName: String,
    val canteenName: String,
    val orders: List<WorkerOrder> = emptyList(),
    val acceptingOrders: Boolean = true,
    val expandedOrderId: Int? = null,
    val showLogoutDialog: Boolean = false
) {
    val storeSubtitle: String
        get() = listOf(
            storeName,
            canteenName
        )
            .filter { it.isNotBlank() }
            .joinToString(" · ")

    val activeOrders: List<WorkerOrder>
        get() = orders
            .filter { order ->
                order.storeId == storeId &&
                        !order.isCancelled &&
                        order.status == OrderStatus.PREPARING
            }
            .sortedWith(
                compareBy<WorkerOrder> {
                    it.createdAtMillis
                }.thenBy {
                    it.id
                }
            )
}

private data class WorkerOrderLocalState(
    val expandedOrderId: Int? = null,
    val showLogoutDialog: Boolean = false
)

class WorkerOrderViewModel(
    private val sharedState: StateFlow<WorkerPendingOrderUiState>
) : ViewModel() {

    private val localState = MutableStateFlow(
        WorkerOrderLocalState()
    )

    val uiState: StateFlow<WorkerOrderUiState> = combine(
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
        action: WorkerOrderAction
    ) {
        when (action) {
            is WorkerOrderAction.OrderClicked -> {
                toggleExpandedOrder(
                    orderId = action.orderId
                )
            }

            is WorkerOrderAction.MarkReadyForPickupClicked -> {
                localState.update {
                    it.copy(
                        expandedOrderId = null
                    )
                }
            }

            WorkerOrderAction.LogoutClicked -> {
                localState.update {
                    it.copy(
                        showLogoutDialog = true
                    )
                }
            }

            WorkerOrderAction.DismissLogoutClicked -> {
                localState.update {
                    it.copy(
                        showLogoutDialog = false
                    )
                }
            }

            WorkerOrderAction.PendingOrdersClicked,
            WorkerOrderAction.PickupsClicked,
            WorkerOrderAction.CompletedOrdersClicked,
            WorkerOrderAction.ItemsClicked,
            WorkerOrderAction.ConfirmLogoutClicked,
            is WorkerOrderAction.AcceptingOrdersChanged -> {
                // Navigation and shared-state changes are handled
                // through callbacks in WorkerOrderRoute.
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
                    order.status == OrderStatus.PREPARING
        }

        if (!exists) {
            return
        }

        localState.update {
            it.copy(
                expandedOrderId = if (
                    it.expandedOrderId == orderId
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
        local: WorkerOrderLocalState
    ): WorkerOrderUiState {
        return WorkerOrderUiState(
            storeId = shared.storeId,
            storeName = shared.storeName,
            canteenName = shared.canteenName,
            orders = shared.orders,
            acceptingOrders = shared.acceptingOrders,
            expandedOrderId = local.expandedOrderId,
            showLogoutDialog = local.showLogoutDialog
        )
    }
}