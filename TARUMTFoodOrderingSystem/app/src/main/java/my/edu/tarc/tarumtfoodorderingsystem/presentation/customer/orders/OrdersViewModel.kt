package my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.orders

import androidx.lifecycle.ViewModel
import java.util.Calendar
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.home.payment.PaymentRequest

enum class OrderStatus(
    val displayName: String
) {
    ORDERED("Ordered"),
    PREPARING("Preparing"),
    READY("Ready"),
    DELIVERED("Delivered")
}

data class CustomerOrderLine(
    val id: Int,
    val name: String,
    val quantity: Int,
    val remarks: String? = null
)

data class CustomerOrder(
    val id: Int,
    val pickupNumber: String,
    val placedAtMillis: Long,
    val status: OrderStatus,
    val items: List<CustomerOrderLine>,
    val totalInSen: Long,
    val storeName: String,
    val canteenName: String,
    val estimatedWaitMinutes: Int? = null
)

data class OrdersUiState(
    val orders: List<CustomerOrder> = emptyList()
) {
    val activeOrders: List<CustomerOrder>
        get() {
            val estimatesByOrderId = orders
                .filter { order ->
                    order.status == OrderStatus.ORDERED ||
                            order.status == OrderStatus.PREPARING
                }
                .groupBy { order ->
                    order.storeName to order.canteenName
                }
                .values
                .flatMap { storeQueue ->
                    storeQueue
                        .sortedWith(
                            compareBy<CustomerOrder> {
                                it.placedAtMillis
                            }.thenBy {
                                it.id
                            }
                        )
                        .mapIndexed { ordersAhead, order ->
                            val preparationMinutes =
                                order.items.sumOf { item ->
                                    item.quantity
                                } * 3

                            order.id to (
                                    preparationMinutes + ordersAhead
                                    )
                        }
                }
                .toMap()

            return orders
                .filter { order ->
                    order.status != OrderStatus.DELIVERED
                }
                .map { order ->
                    order.copy(
                        estimatedWaitMinutes =
                            estimatesByOrderId[order.id]
                    )
                }
                .sortedWith(
                    compareByDescending<CustomerOrder> {
                        it.status == OrderStatus.READY
                    }.thenBy {
                        it.placedAtMillis
                    }.thenBy {
                        it.id
                    }
                )
        }
}

class OrdersViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        OrdersUiState(
            orders = customerOrderSampleData()
        )
    )

    val uiState = _uiState.asStateFlow()

    // Frontend functions
    fun completeDemoPayment(
        request: PaymentRequest,
        storeName: String,
        canteenName: String
    ): CustomerOrder? {
        val state = _uiState.value

        // Reserve pickup numbers already used by active orders.
        val usedPickupNumbers = state.activeOrders
            .mapNotNull {
                it.pickupNumber.toIntOrNull()
            }
            .toSet()

        val pickupNumber = (1..999).firstOrNull {
            it !in usedPickupNumbers
        } ?: return null

        val nextOrderId = (
                state.orders.maxOfOrNull {
                    it.id
                } ?: 0
                ) + 1

        val order = CustomerOrder(
            id = nextOrderId,
            pickupNumber = pickupNumber
                .toString()
                .padStart(3, '0'),
            placedAtMillis = System.currentTimeMillis(),
            status = OrderStatus.PREPARING,
            items = request.items.map { cartItem ->
                // Keep the existing Orders line model.
                // Include the drink choice in its remarks summary.
                val remarksSummary = listOfNotNull(
                    cartItem.drinkOption?.let {
                        "Drink: ${it.displayName}"
                    },
                    cartItem.remarks.takeIf {
                        it.isNotBlank()
                    }
                )
                    .joinToString(" · ")
                    .takeIf {
                        it.isNotBlank()
                    }

                CustomerOrderLine(
                    id = cartItem.id,
                    name = cartItem.name,
                    quantity = cartItem.quantity,
                    remarks = remarksSummary
                )
            },
            totalInSen = request.totalInSen,
            storeName = storeName,
            canteenName = canteenName,
            estimatedWaitMinutes = null
        )

        _uiState.value = state.copy(
            orders = state.orders + order
        )

        return order
    }
}

/**
 * Shared frontend fixtures for Orders and Purchase History.
 * These are not live orders or calculated checkout totals.
 */
internal fun customerOrderSampleData(): List<CustomerOrder> {
    fun sampleTime(
        daysAgo: Int,
        hour: Int,
        minute: Int
    ): Long {
        return Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, -daysAgo)
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }

    return listOf(
        CustomerOrder(
            id = 1040,
            pickupNumber = "040",
            placedAtMillis = sampleTime(
                daysAgo = 0,
                hour = 12,
                minute = 15
            ),
            status = OrderStatus.READY,
            items = listOf(
                CustomerOrderLine(
                    id = 1,
                    name = "Ipoh Hor Fun",
                    quantity = 1
                )
            ),
            totalInSen = 700L,
            storeName = "Chicken Rice",
            canteenName = "Yum Yum Cafeteria"
        ),
        CustomerOrder(
            id = 1042,
            pickupNumber = "042",
            placedAtMillis = sampleTime(
                daysAgo = 0,
                hour = 12,
                minute = 24
            ),
            status = OrderStatus.PREPARING,
            items = listOf(
                CustomerOrderLine(
                    id = 2,
                    name = "Chicken Rice",
                    quantity = 1,
                    remarks = "Less rice"
                ),
                CustomerOrderLine(
                    id = 3,
                    name = "Chicken Chop Rice",
                    quantity = 1
                )
            ),
            totalInSen = 2150L,
            storeName = "Chicken Rice",
            canteenName = "Yum Yum Cafeteria",
            estimatedWaitMinutes = 2
        ),
        CustomerOrder(
            id = 1036,
            pickupNumber = "036",
            placedAtMillis = sampleTime(
                daysAgo = 8,
                hour = 12,
                minute = 30
            ),
            status = OrderStatus.DELIVERED,
            items = listOf(
                CustomerOrderLine(
                    id = 4,
                    name = "Chicken Rice",
                    quantity = 1
                )
            ),
            storeName = "Chicken Rice",
            canteenName = "Yum Yum Cafeteria",
            totalInSen = 650L
        ),
        CustomerOrder(
            id = 1028,
            pickupNumber = "028",
            placedAtMillis = sampleTime(
                daysAgo = 9,
                hour = 13,
                minute = 10
            ),
            status = OrderStatus.DELIVERED,
            items = listOf(
                CustomerOrderLine(
                    id = 5,
                    name = "Chicken Chop Rice",
                    quantity = 1,
                    remarks = "Blackpepper Sauce"
                ),
                CustomerOrderLine(
                    id = 6,
                    name = "Chicken Chop Rice",
                    quantity = 1,
                    remarks = "Thai Sauce"
                )
            ),
            storeName = "Chicken Rice",
            canteenName = "Yum Yum Cafeteria",
            totalInSen = 1500L
        )
    )
}

