package my.edu.tarc.tarumtfoodorderingsystem.data.supabase.canteen

import kotlinx.serialization.Serializable

@Serializable
data class Canteen (
    val canteenId: Int,
    val canteenName: String,
    // image attribute here
)

@Serializable
data class NewCanteen (
    val canteenName: String,
    // image attribute here
)
