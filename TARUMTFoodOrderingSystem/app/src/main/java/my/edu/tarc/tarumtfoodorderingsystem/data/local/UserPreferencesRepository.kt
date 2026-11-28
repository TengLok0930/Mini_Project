package my.edu.tarc.tarumtfoodorderingsystem.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import java.io.IOException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

private val Context.userPreferencesDataStore by preferencesDataStore(
    name = "user_preferences"
)

class UserPreferencesRepository(
    context: Context
) {
    private val dataStore =
        context.applicationContext.userPreferencesDataStore

    private val preferencesFlow = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }

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

    val isDarkTheme: Flow<Boolean?> = preferencesFlow
        .map { preferences ->
            preferences[Keys.IS_DARK_THEME]
        }
        .distinctUntilChanged()

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

    suspend fun saveDarkTheme(isDarkTheme: Boolean) {
        dataStore.edit { preferences ->
            preferences[Keys.IS_DARK_THEME] = isDarkTheme
        }
    }

    private object Keys {
        val USER_ROLE = stringPreferencesKey("user_role")
        val IS_DARK_THEME = booleanPreferencesKey("is_dark_theme")
    }

    private companion object {
        val VALID_ROLES = setOf(
            "CUSTOMER",
            "WORKER",
            "ADMIN"
        )
    }
}