package my.edu.tarc.tarumtfoodorderingsystem.data.supabase.order

import kotlinx.serialization.Serializable

@Serializable
data class Order (
    val orderId: Int,
    val userId: Int,
    val storeId: Int,
    val orderTime: String,
    val orderStatus: String,
    val queueNo: Int,
    val totalAmount: Double
)

@Serializable
data class NewOrder (
    val userId: Int,
    val storeId: Int,
    val queueNo: Int,
    val totalAmount: Double
)