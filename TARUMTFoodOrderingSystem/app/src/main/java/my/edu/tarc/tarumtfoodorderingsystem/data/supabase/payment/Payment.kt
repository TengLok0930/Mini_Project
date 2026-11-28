package my.edu.tarc.tarumtfoodorderingsystem.data.supabase.payment

import kotlinx.serialization.Serializable

@Serializable
data class Payment (
    val paymentId: Int,
    val orderId: Int,
    val method: String,
    val status: String,
    val paidAt: String
)

@Serializable
data class NewPayment (
    val orderId: Int,
    val method: String,
)
