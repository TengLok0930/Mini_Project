package my.edu.tarc.tarumtfoodorderingsystem.data.supabase.customer

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from

interface CustomerRepository {
    suspend fun getAll(): List<Customer>
    suspend fun getById(id: Int): Customer?
    suspend fun insert(customer: NewCustomer)
    suspend fun updateName(id: Int, name: String)
    suspend fun delete(id: Int)
}

class CustomerRepositoryImpl (
    private val client: SupabaseClient
): CustomerRepository {

    override suspend fun getAll(): List<Customer> =
        client.from("customer")
            .select()
            .decodeList<Customer>()

    override suspend fun getById(id: Int): Customer? =
        client.from("customer")
            .select { filter { eq("id", id) } }
            .decodeSingleOrNull<Customer>()

    override suspend fun insert(customer: NewCustomer) {
        client.from("customer")
            .insert(customer)
    }

    override suspend fun updateName(id: Int, name: String) {
        client.from("customer")
            .update({ set("name", name) }) {
                filter { eq("id", id) }
            }
    }

    override suspend fun delete(id: Int) {
        client.from("customer")
            .delete {
                filter { eq("id", id) }
            }
    }
}