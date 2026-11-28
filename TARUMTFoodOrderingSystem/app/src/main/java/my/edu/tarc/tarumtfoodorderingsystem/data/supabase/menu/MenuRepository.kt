package my.edu.tarc.tarumtfoodorderingsystem.data.supabase.menu

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from

interface MenuRepository {
    suspend fun getAll(): List<Menu>
    suspend fun getById(id: Int): Menu?
    suspend fun insert(menu: NewMenu)
    suspend fun delete(id: Int)
}

class MenuRepositoryImpl (
    private val client: SupabaseClient
): MenuRepository {

    override suspend fun getAll(): List<Menu> =
        client.from("menu")
            .select()
            .decodeList<Menu>()

    override suspend fun getById(id: Int): Menu? =
        client.from("menu")
            .select { filter { eq("id", id) } }
            .decodeSingleOrNull<Menu>()

    override suspend fun insert(menu: NewMenu) {
        client.from("menu")
            .insert(menu)
    }

    override suspend fun delete(id: Int) {
        client.from("menu")
            .delete {
                filter { eq("id", id) }
            }
    }
}