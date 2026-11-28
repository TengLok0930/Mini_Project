package my.edu.tarc.tarumtfoodorderingsystem.data.supabase.itemOrder

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from

interface ItemOrderRepository {
    suspend fun getAll(): List<ItemOrder>
    suspend fun getById(id: Int): ItemOrder?
    suspend fun insert(itemOrder: NewItemOrder)
    suspend fun updateQuantity(id: Int, quantity: Int)
    suspend fun updateRemark(id: Int, remark: String)
    suspend fun delete(id: Int)
}

class ItemOrderRepositoryImpl (
    private val client: SupabaseClient
): ItemOrderRepository {

    override suspend fun getAll(): List<ItemOrder> =
        client.from("itemOrder")
            .select()
            .decodeList<ItemOrder>()

    override suspend fun getById(id: Int): ItemOrder? =
        client.from("itemOrder")
            .select { filter { eq("id", id) } }
            .decodeSingleOrNull<ItemOrder>()

    override suspend fun insert(itemOrder: NewItemOrder) {
        client.from("itemOrder")
            .insert(itemOrder)
    }

    override suspend fun updateQuantity(id: Int, quantity: Int) {
        client.from("itemOrder")
            .update({ set("quantity", quantity) }) {
                filter { eq("id", id) }
            }
    }

    override suspend fun updateRemark(id: Int, remark: String) {
        client.from("itemOrder")
            .update({ set("remark", remark) }) {
                filter { eq("id", id) }
            }
    }

    override suspend fun delete(id: Int) {
        client.from("itemOrder")
            .delete {
                filter { eq("id", id) }
            }
    }
}