package my.edu.tarc.tarumtfoodorderingsystem.data.supabase.item

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from

interface ItemRepository {
    suspend fun getAll(): List<Item>
    suspend fun getById(id: Int): Item?
    suspend fun insert(item: NewItem)
    suspend fun updatePrice(id: Int, price: Double)
    suspend fun updateStock(id: Int, stock: Int)
    suspend fun delete(id: Int)
}

class ItemRepositoryImpl (
    private val client: SupabaseClient
): ItemRepository {

    override suspend fun getAll(): List<Item> =
        client.from("item")
            .select()
            .decodeList<Item>()

    override suspend fun getById(id: Int): Item? =
        client.from("item")
            .select { filter { eq("id", id) } }
            .decodeSingleOrNull<Item>()

    override suspend fun insert(item: NewItem) {
        client.from("item")
            .insert(item)
    }

    override suspend fun updatePrice(id: Int, price: Double) {
        client.from("item")
            .update({ set("unitPrice", price) }) {
                filter { eq("id", id) }
            }
    }

    override suspend fun updateStock(id: Int, stock: Int) {
        client.from("item")
            .update({ set("stock", stock) }) {
                filter { eq("id", id) }
            }
    }

    // image update here

    override suspend fun delete(id: Int) {
        client.from("item")
            .delete {
                filter { eq("id", id) }
            }
    }
}
