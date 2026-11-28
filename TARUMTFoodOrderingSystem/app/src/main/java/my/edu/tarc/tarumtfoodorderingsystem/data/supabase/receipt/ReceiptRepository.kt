package my.edu.tarc.tarumtfoodorderingsystem.data.supabase.receipt

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from

interface ReceiptRepository {
    suspend fun getAll(): List<Receipt>
    suspend fun getById(id: Int): Receipt?
    suspend fun insert(receipt: NewReceipt)
    suspend fun delete(id: Int)
}

class ReceiptRepositoryImpl (
    private val client: SupabaseClient
): ReceiptRepository {

    override suspend fun getAll(): List<Receipt> =
        client.from("receipt")
            .select()
            .decodeList<Receipt>()

    override suspend fun getById(id: Int): Receipt? =
        client.from("receipt")
            .select { filter { eq("id", id) } }
            .decodeSingleOrNull<Receipt>()

    override suspend fun insert(receipt: NewReceipt) {
        client.from("receipt")
            .insert(receipt)
    }

    override suspend fun delete(id: Int) {
        client.from("receipt")
            .delete {
                filter { eq("id", id) }
            }
    }
}