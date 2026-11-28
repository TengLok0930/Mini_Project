package my.edu.tarc.tarumtfoodorderingsystem.data.supabase.report

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from

interface ReportRepository {
    suspend fun getAll(): List<Report>
    suspend fun getById(id: Int): Report?
    suspend fun insert(report: NewReport)
    suspend fun updateType(id: Int, type: String)
    suspend fun delete(id: Int)
}

class ReportRepositoryImpl (
    private val client: SupabaseClient
): ReportRepository {

    override suspend fun getAll(): List<Report> =
        client.from("report")
            .select()
            .decodeList<Report>()

    override suspend fun getById(id: Int): Report? =
        client.from("report")
            .select { filter { eq("id", id) } }
            .decodeSingleOrNull<Report>()

    override suspend fun insert(report: NewReport) {
        client.from("report")
            .insert(report)
    }

    override suspend fun updateType(id: Int, type: String) {
        client.from("report")
            .update({ set("reportType", type) }) {
                filter { eq("id", id) }
            }
    }

    override suspend fun delete(id: Int) {
        client.from("report")
            .delete {
                filter { eq("id", id) }
            }
    }
}