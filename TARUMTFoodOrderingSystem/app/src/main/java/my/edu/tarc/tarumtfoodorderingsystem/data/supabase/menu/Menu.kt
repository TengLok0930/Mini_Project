package my.edu.tarc.tarumtfoodorderingsystem.data.supabase.menu

import kotlinx.serialization.Serializable

@Serializable
data class Menu (
    val menuId: Int,
    val storeId: Int
)

@Serializable
data class NewMenu (
    val storeId: Int
)