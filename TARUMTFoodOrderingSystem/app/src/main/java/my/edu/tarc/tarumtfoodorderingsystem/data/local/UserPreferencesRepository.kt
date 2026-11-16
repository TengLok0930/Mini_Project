package my.edu.tarc.tarumtfoodorderingsystem.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import java.io.IOException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

private val Context.userPreferencesDataStore by preferencesDataStore(
    name = "user_preferences"
)

class UserPreferencesRepository(
    context: Context
) {
    private val dataStore =
        context.applicationContext.userPreferencesDataStore

    val userRole: Flow<String?> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[Keys.USER_ROLE]
                ?.takeIf { it in VALID_ROLES }
        }

    suspend fun saveLogin(role: String) {
        require(role in VALID_ROLES) {
            "Unsupported role: $role"
        }

        dataStore.edit { preferences ->
            preferences[Keys.USER_ROLE] = role
        }
    }

    suspend fun clearLogin() {
        dataStore.edit { preferences ->
            preferences.remove(Keys.USER_ROLE)
        }
    }

    private object Keys {
        val USER_ROLE = stringPreferencesKey("user_role")
    }

    private companion object {
        val VALID_ROLES = setOf(
            "CUSTOMER",
            "WORKER",
            "ADMIN"
        )
    }
}