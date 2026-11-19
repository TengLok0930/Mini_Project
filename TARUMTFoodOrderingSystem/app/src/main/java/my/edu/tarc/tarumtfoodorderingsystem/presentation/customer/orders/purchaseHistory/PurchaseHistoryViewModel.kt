package my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.orders.purchaseHistory

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.orders.CustomerOrder
import my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.orders.OrderStatus
import my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.orders.customerOrderSampleData

data class PurchaseHistoryUiState(
    val orders: List<CustomerOrder> = emptyList()
) {
    val previousOrders: List<CustomerOrder>
        get() = orders
            .filter {
                it.status == OrderStatus.DELIVERED
            }
            .sortedByDescending {
                it.placedAtMillis
            }
}

class PurchaseHistoryViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        PurchaseHistoryUiState(
            orders = customerOrderSampleData()
        )
    )

    val uiState = _uiState.asStateFlow()
}