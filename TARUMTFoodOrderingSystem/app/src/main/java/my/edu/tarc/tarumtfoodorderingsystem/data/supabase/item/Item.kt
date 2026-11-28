package my.edu.tarc.tarumtfoodorderingsystem.data.supabase.item

import kotlinx.serialization.Serializable

@Serializable
data class Item (
    val itemId: Int,
    val menuId: Int,
    val canteenId: Int,
    val itemName: String,
    val itemType: String,
    val itemCategory: String,
    val stock: Int,
    val unitPrice: Double,
    // image attribute here
)

@Serializable
data class NewItem (
    val menuId: Int,
    val canteenId: Int,
    val itemName: String,
    val itemType: String,
    val itemCategory: String,
    val stock: Int,
    val unitPrice: Double,
    // image attribute here
)