package my.edu.tarc.tarumtfoodorderingsystem.data.supabase.report

import kotlinx.serialization.Serializable

@Serializable
data class Report (
    val reportId: Int? = null,
    val reportTitle: String,
    val startDate: String,
    val endDate: String,
    val reportType: String,
    val storeId: Int
)

@Serializable
data class NewReport (
    val reportTitle: String,
    val startDate: String,
    val endDate: String,
    val reportType: String,
    val storeId: Int
)