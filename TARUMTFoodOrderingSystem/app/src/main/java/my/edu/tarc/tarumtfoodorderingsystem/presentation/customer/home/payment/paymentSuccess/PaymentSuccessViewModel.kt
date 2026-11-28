package my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.home.payment.paymentSuccess

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.orders.CustomerOrder
import my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.orders.OrderStatus

data class PaymentSuccessUiState(
    val orderId: Int,
    val amountPaidInSen: Long,
    val storeName: String,
    val canteenName: String,
    val pickupNumber: String,
    val status: OrderStatus
) {
    val storeSubtitle: String
        get() = listOf(
            storeName,
            canteenName
        )
            .filter { it.isNotBlank() }
            .joinToString(" · ")
}

class PaymentSuccessViewModel(
    order: CustomerOrder
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        PaymentSuccessUiState(
            orderId = order.id,
            amountPaidInSen = order.totalInSen,
            storeName = order.storeName,
            canteenName = order.canteenName,
            pickupNumber = order.pickupNumber,
            status = order.status
        )
    )

    val uiState = _uiState.asStateFlow()
}