package my.edu.tarc.tarumtfoodorderingsystem.data.supabase.order

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from

interface OrderRepository {
    suspend fun getAll(): List<Order>
    suspend fun getById(id: Int): Order?
    suspend fun insert(order: NewOrder)
    suspend fun updateStatus(id: Int, status: String)
    suspend fun updateTotalAmount(id: Int, totalAmount: Double)
    suspend fun delete(id: Int)
}

class OrderRepositoryImpl (
    private val client: SupabaseClient
): OrderRepository {

    override suspend fun getAll(): List<Order> =
        client.from("order")
            .select()
            .decodeList<Order>()

    override suspend fun getById(id: Int): Order? =
        client.from("order")
            .select { filter { eq("id", id) } }
            .decodeSingleOrNull<Order>()

    override suspend fun insert(order: NewOrder) {
        client.from("order")
            .insert(order)
    }

    override suspend fun updateStatus(id: Int, status: String) {
        client.from("order")
            .update({ set("orderStatus", status) }) {
                filter { eq("id", id) }
            }
    }

    override suspend fun updateTotalAmount(id: Int, totalAmount: Double) {
        client.from("order")
            .update({ set("totalAmount", totalAmount) }) {
                filter { eq("id", id) }
            }
    }

    override suspend fun delete(id: Int) {
        client.from("order")
            .delete {
                filter { eq("id", id) }
            }
    }
}
