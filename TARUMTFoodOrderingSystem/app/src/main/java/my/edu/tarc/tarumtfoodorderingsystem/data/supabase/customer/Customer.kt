package my.edu.tarc.tarumtfoodorderingsystem.data.supabase.customer

import kotlinx.serialization.Serializable

@Serializable
data class Customer (
    val userId: Int,
    val name: String,
    val email: String,
    val passwordHash: String,
    val role: String
)

@Serializable
data class NewCustomer (
    val name: String,
    val email: String,
    val passwordHash: String,
    val role: String
)