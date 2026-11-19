package my.edu.tarc.tarumtfoodorderingsystem.presentation.worker.orders.pendingOrders

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.orders.OrderStatus

data class WorkerOrderLine(
    val id: Int,
    val name: String,
    val quantity: Int,
    val remarks: String = "",
    val drinkOption: String? = null
)

data class WorkerOrder(
    val id: Int,
    val storeId: Int,
    val pickupNumber: Int,
    val createdAtMillis: Long,
    val items: List<WorkerOrderLine>,
    val totalInSen: Long,
    val status: OrderStatus = OrderStatus.ORDERED,
    val isCancelled: Boolean = false,
    val collectedAtMillis: Long? = null
) {
    val formattedPickupNumber: String
        get() = pickupNumber
            .toString()
            .padStart(3, '0')
}

data class WorkerPendingOrderUiState(
    val storeId: Int,
    val storeName: String,
    val canteenName: String,
    val orders: List<WorkerOrder> = emptyList(),
    val acceptingOrders: Boolean = true,
    val expandedOrderId: Int? = null,
    val managingOrderId: Int? = null,
    val showLogoutDialog: Boolean = false,
    val notice: String? = null
) {
    val storeSubtitle: String
        get() = listOf(
            storeName,
            canteenName
        )
            .filter { it.isNotBlank() }
            .joinToString(" · ")

    val pendingOrders: List<WorkerOrder>
        get() = orders
            .filter { order ->
                order.storeId == storeId &&
                        !order.isCancelled &&
                        order.status == OrderStatus.ORDERED
            }
            .sortedWith(
                compareBy<WorkerOrder> {
                    it.createdAtMillis
                }.thenBy {
                    it.id
                }
            )

    val managingOrder: WorkerOrder?
        get() = pendingOrders.firstOrNull {
            it.id == managingOrderId
        }
}

class WorkerPendingOrderViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        WorkerPendingOrderUiState(
            storeId = 1,
            storeName = "Chicken Rice",
            canteenName = "Yum Yum Cafeteria",
            orders = workerPendingOrderSampleData(
                storeId = 1
            )
        )
    )

    val uiState = _uiState.asStateFlow()

    fun onAction(
        action: WorkerPendingOrderAction
    ) {
        when (action) {
            is WorkerPendingOrderAction.AcceptingOrdersChanged -> {
                _uiState.update { state ->
                    state.copy(
                        acceptingOrders = action.accepting,
                        notice = null
                    )
                }
            }

            is WorkerPendingOrderAction.OrderClicked -> {
                toggleExpandedOrder(
                    orderId = action.orderId
                )
            }

            is WorkerPendingOrderAction.ManageOrderClicked -> {
                openOrderDialog(
                    orderId = action.orderId
                )
            }

            is WorkerPendingOrderAction.AcceptOrderClicked -> {
                acceptOrder(
                    orderId = action.orderId
                )
            }

            is WorkerPendingOrderAction.CancelOrderClicked -> {
                cancelOrder(
                    orderId = action.orderId
                )
            }

            WorkerPendingOrderAction.DismissOrderDialogClicked -> {
                _uiState.update { state ->
                    state.copy(
                        managingOrderId = null
                    )
                }
            }

            WorkerPendingOrderAction.LogoutClicked -> {
                _uiState.update { state ->
                    state.copy(
                        showLogoutDialog = true
                    )
                }
            }

            WorkerPendingOrderAction.DismissLogoutClicked -> {
                _uiState.update { state ->
                    state.copy(
                        showLogoutDialog = false
                    )
                }
            }

            WorkerPendingOrderAction.CompletedOrdersClicked,
            WorkerPendingOrderAction.ItemsClicked,
            WorkerPendingOrderAction.ActiveOrdersClicked,
            WorkerPendingOrderAction.PickupsClicked,
            WorkerPendingOrderAction.ConfirmLogoutClicked -> {
                // Navigation is handled by the Route.
            }
        }
    }

    private fun toggleExpandedOrder(
        orderId: Int
    ) {
        _uiState.update { state ->
            val exists = state.pendingOrders.any {
                it.id == orderId
            }

            if (!exists) {
                return@update state
            }

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

    private fun openOrderDialog(
        orderId: Int
    ) {
        _uiState.update { state ->
            val exists = state.pendingOrders.any {
                it.id == orderId
            }

            if (exists) {
                state.copy(
                    managingOrderId = orderId
                )
            } else {
                state
            }
        }
    }

    private fun acceptOrder(
        orderId: Int
    ) {
        _uiState.update { state ->
            val order = state.pendingOrders.firstOrNull {
                it.id == orderId
            } ?: return@update state

            state.copy(
                orders = state.orders.map { existingOrder ->
                    if (existingOrder.id == orderId) {
                        existingOrder.copy(
                            status = OrderStatus.PREPARING
                        )
                    } else {
                        existingOrder
                    }
                },
                expandedOrderId = null,
                managingOrderId = null,
                notice = "Pickup #${order.formattedPickupNumber} " +
                        "accepted and moved to Active."
            )
        }
    }

    private fun cancelOrder(
        orderId: Int
    ) {
        _uiState.update { state ->
            val order = state.pendingOrders.firstOrNull {
                it.id == orderId
            } ?: return@update state

            state.copy(
                orders = state.orders.map { existingOrder ->
                    if (existingOrder.id == orderId) {
                        existingOrder.copy(
                            isCancelled = true
                        )
                    } else {
                        existingOrder
                    }
                },
                expandedOrderId = null,
                managingOrderId = null,
                notice = "Pickup #${order.formattedPickupNumber} cancelled."
            )
        }
    }

    fun markOrderReady(
        orderId: Int
    ): Boolean {
        val state = _uiState.value

        val order = state.orders.firstOrNull {
            it.id == orderId &&
                    it.storeId == state.storeId &&
                    !it.isCancelled &&
                    it.status == OrderStatus.PREPARING
        } ?: return false

        _uiState.update { currentState ->
            currentState.copy(
                orders = currentState.orders.map { existingOrder ->
                    if (existingOrder.id == orderId) {
                        existingOrder.copy(
                            status = OrderStatus.READY
                        )
                    } else {
                        existingOrder
                    }
                },
                expandedOrderId = null,
                managingOrderId = null,
                notice = "Pickup #${order.formattedPickupNumber} " +
                        "is ready for collection."
            )
        }

        return true
    }

    fun confirmOrderCollection(
        orderId: Int
    ): Boolean {
        val state = _uiState.value

        val order = state.orders.firstOrNull {
            it.id == orderId &&
                    it.storeId == state.storeId &&
                    !it.isCancelled &&
                    it.status == OrderStatus.READY
        } ?: return false

        val collectedAtMillis = System.currentTimeMillis()

        _uiState.update { currentState ->
            currentState.copy(
                orders = currentState.orders.map { existingOrder ->
                    if (existingOrder.id == orderId) {
                        existingOrder.copy(
                            status = OrderStatus.DELIVERED,
                            collectedAtMillis = collectedAtMillis
                        )
                    } else {
                        existingOrder
                    }
                },
                expandedOrderId = null,
                managingOrderId = null,
                notice = "Pickup #${order.formattedPickupNumber} collected."
            )
        }

        return true
    }
}

private fun workerPendingOrderSampleData(
    storeId: Int
): List<WorkerOrder> {
    return listOf(
        WorkerOrder(
            id = 1042,
            storeId = storeId,
            pickupNumber = 42,
            createdAtMillis = System.currentTimeMillis(),
            items = listOf(
                WorkerOrderLine(
                    id = 1,
                    name = "Chicken Rice",
                    quantity = 1,
                    remarks = "Chicken Breast"
                ),
                WorkerOrderLine(
                    id = 2,
                    name = "Chicken Chop Rice",
                    quantity = 2,
                    remarks = "Black Pepper Sauce"
                )
            ),
            totalInSen = 2150L
        )
    )
}