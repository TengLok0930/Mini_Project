package my.edu.tarc.tarumtfoodorderingsystem.data.supabase.store

import kotlinx.serialization.Serializable

@Serializable
data class Store (
    val storeId: Int,
    val canteenId: Int,
    val storeName: String,
    val owner: Int,
    // image attribute here
)

@Serializable
data class NewStore (
    val canteenId: Int,
    val storeName: String,
    val owner: Int,
    // image attribute here
)