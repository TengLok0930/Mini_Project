package my.edu.tarc.tarumtfoodorderingsystem.data.supabase.worker

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from

interface WorkerRepository {
    suspend fun getAll(): List<Worker>
    suspend fun getById(id: Int): Worker?
    suspend fun insert(worker: NewWorker)
    suspend fun updateName(id: Int, name: String)
    suspend fun delete(id: Int)
}

class WorkerRepositoryImpl (
    private val client: SupabaseClient
): WorkerRepository {

    override suspend fun getAll(): List<Worker> =
        client.from("worker")
            .select()
            .decodeList<Worker>()

    override suspend fun getById(id: Int): Worker? =
        client.from("worker")
            .select { filter { eq("id", id) } }
            .decodeSingleOrNull<Worker>()

    override suspend fun insert(worker: NewWorker) {
        client.from("worker")
            .insert(worker)
    }

    override suspend fun updateName(id: Int, name: String) {
        client.from("worker")
            .update({ set("name", name) }) {
                filter { eq("id", id) }
            }
    }

    override suspend fun delete(id: Int) {
        client.from("worker")
            .delete {
                filter { eq("id", id) }
            }
    }
}