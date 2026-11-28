package my.edu.tarc.tarumtfoodorderingsystem.data.supabase.itemOrder

import kotlinx.serialization.Serializable

@Serializable
data class ItemOrder (
    val orderId: Int,
    val itemId: Int,
    val quantity: Int,
    val unitPrice: Double,
    val subtotal: Double,
    val remark: String
)

@Serializable
data class NewItemOrder (
    val orderId: Int,
    val itemId: Int,
    val quantity: Int,
    val unitPrice: Double,
    val subtotal: Double,
    val remark: String
)