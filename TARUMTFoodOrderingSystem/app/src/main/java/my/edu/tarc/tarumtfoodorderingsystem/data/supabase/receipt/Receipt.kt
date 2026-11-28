package my.edu.tarc.tarumtfoodorderingsystem.data.supabase.receipt

import kotlinx.serialization.Serializable

@Serializable
data class Receipt (
    val receiptId: Int,
    val paymentId: Int,
    val discount: Double,
    val grandTotal: Double
)

@Serializable
data class NewReceipt (
    val paymentId: Int,
    val discount: Double,
    val grandTotal: Double
)
