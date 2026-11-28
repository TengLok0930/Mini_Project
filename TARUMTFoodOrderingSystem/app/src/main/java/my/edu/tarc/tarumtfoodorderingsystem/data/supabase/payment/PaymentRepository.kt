package my.edu.tarc.tarumtfoodorderingsystem.data.supabase.payment

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from

interface PaymentRepository {
    suspend fun getAll(): List<Payment>
    suspend fun getById(id: Int): Payment?
    suspend fun insert(payment: NewPayment)
    suspend fun updateStatus(id: Int, status: String)
    suspend fun delete(id: Int)
}

class PaymentRepositoryImpl (
    private val client: SupabaseClient
): PaymentRepository {

    override suspend fun getAll(): List<Payment> =
        client.from("payment")
            .select()
            .decodeList<Payment>()

    override suspend fun getById(id: Int): Payment? =
        client.from("payment")
            .select { filter { eq("id", id) } }
            .decodeSingleOrNull<Payment>()

    override suspend fun insert(payment: NewPayment) {
        client.from("payment")
            .insert(payment)
    }

    override suspend fun updateStatus(id: Int, status: String) {
        client.from("payment")
            .update({ set("status", status) }) {
                filter { eq("id", id) }
            }
    }

    override suspend fun delete(id: Int) {
        client.from("payment")
            .delete {
                filter { eq("id", id) }
            }
    }
}