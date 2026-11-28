package my.edu.tarc.tarumtfoodorderingsystem.data.supabase.store

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from

interface StoreRepository {
    suspend fun getAll(): List<Store>
    suspend fun getById(id: Int): Store?
    suspend fun insert(store: NewStore)
    suspend fun updateName(id: Int, name: String)
    suspend fun delete(id: Int)
}

class StoreRepositoryImpl (
    private val client: SupabaseClient
): StoreRepository {

    override suspend fun getAll(): List<Store> =
        client.from("store")
            .select()
            .decodeList<Store>()

    override suspend fun getById(id: Int): Store? =
        client.from("store")
            .select { filter { eq("id", id) } }
            .decodeSingleOrNull<Store>()

    override suspend fun insert(store: NewStore) {
        client.from("store")
            .insert(store)
    }

    override suspend fun updateName(id: Int, name: String) {
        client.from("store")
            .update({ set("name", name) }) {
                filter { eq("id", id) }
            }
    }

    // Image update here

    override suspend fun delete(id: Int) {
        client.from("store")
            .delete {
                filter { eq("id", id) }
            }
    }
}