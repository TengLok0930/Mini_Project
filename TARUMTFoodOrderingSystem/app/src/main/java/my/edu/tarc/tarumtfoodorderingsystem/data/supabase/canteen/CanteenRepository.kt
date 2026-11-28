package my.edu.tarc.tarumtfoodorderingsystem.data.supabase.canteen

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from

interface CanteenRepository {
    suspend fun getAll(): List<Canteen>
    suspend fun getById(id: Int): Canteen?
    suspend fun insert(canteen: NewCanteen)
    suspend fun updateName(id: Int, name: String)
    suspend fun delete(id: Int)
}

class CanteenRepositoryImpl (
    private val client: SupabaseClient
): CanteenRepository {

    override suspend fun getAll(): List<Canteen> =
        client.from("canteen")
            .select()
            .decodeList<Canteen>()

    override suspend fun getById(id: Int): Canteen? =
        client.from("canteen")
            .select { filter { eq("id", id) } }
            .decodeSingleOrNull<Canteen>()

    override suspend fun insert(canteen: NewCanteen) {
        client.from("canteen")
            .insert(canteen)
    }

    override suspend fun updateName(id: Int, name: String) {
        client.from("canteen")
            .update({ set("name", name) }) {
                filter { eq("id", id) }
            }
    }

    // image attribute here

    override suspend fun delete(id: Int) {
        client.from("canteen")
            .delete {
                filter { eq("id", id) }
            }
    }
}