package my.edu.tarc.tarumtfoodorderingsystem.data.supabase.worker

import kotlinx.serialization.Serializable

@Serializable
data class Worker (
    val userId: Int,
    val name: String,
    val email: String,
    val passwordHash: String,
    val role: String
)

@Serializable
data class NewWorker (
    val name: String,
    val email: String,
    val passwordHash: String,
    val role: String
)