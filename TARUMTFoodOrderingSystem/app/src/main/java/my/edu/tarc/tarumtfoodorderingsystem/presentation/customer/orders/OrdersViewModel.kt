package my.edu.tarc.tarumtfoodorderingsystem.presentation.customer.orders

import androidx.lifecycle.ViewModel
import java.util.Calendar
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

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
        get() = orders
            .filter {
                it.status == OrderStatus.ORDERED ||
                        it.status == OrderStatus.PREPARING ||
                        it.status == OrderStatus.READY
            }
            .sortedWith(
                compareByDescending<CustomerOrder> {
                    it.status == OrderStatus.READY
                }.thenBy {
                    it.placedAtMillis
                }
            )
}

class OrdersViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        OrdersUiState(
            orders = customerOrderSampleData()
        )
    )

    val uiState = _uiState.asStateFlow()
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